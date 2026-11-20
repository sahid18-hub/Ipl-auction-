package com.example.data

import android.content.Context
import com.example.model.AcquiredPlayer
import com.example.model.AuctionPhase
import com.example.model.AuctionSet
import com.example.model.FranchiseId
import com.example.model.FranchiseState
import com.example.model.PaddleEvent
import com.example.model.PaddleEventType
import com.example.model.Player
import com.example.model.SavedAuctionSummary
import com.example.model.SoldRecord
import org.json.JSONArray
import org.json.JSONObject

data class PersistedAuctionSnapshot(
    val phase: AuctionPhase,
    val selectedFranchiseId: FranchiseId,
    val franchises: Map<FranchiseId, FranchiseState>,
    val currentSet: AuctionSet,
    val currentSetPlayers: List<Player>,
    val currentPlayerIndex: Int,
    val currentSetRecords: List<SoldRecord>,
    val allRecords: List<SoldRecord>,
    val unsoldPool: List<Player>,
    val paddleFeed: List<PaddleEvent>,
    val sessionSeed: Int,
    val savedAtMillis: Long
)

/**
 * Local on-device persistence store (Android SharedPreferences JSON) mirroring localStorage
 * for auto-saving & resuming in-progress IPL Mega Auctions plus mute preference.
 */
class AuctionPersistence(context: Context) {

    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val playerById: Map<String, Player> by lazy {
        PlayerDatabase.allPlayers.associateBy { it.id }
    }

    fun isSoundMuted(): Boolean = prefs.getBoolean(KEY_SOUND_MUTED, false)

    fun setSoundMuted(muted: Boolean) {
        prefs.edit().putBoolean(KEY_SOUND_MUTED, muted).apply()
    }

    fun saveSnapshot(snapshot: PersistedAuctionSnapshot) {
        try {
            val root = JSONObject().apply {
                put("version", SCHEMA_VERSION)
                put("phase", snapshot.phase.name)
                put("selectedFranchiseId", snapshot.selectedFranchiseId.name)
                put("currentSet", snapshot.currentSet.name)
                put("currentPlayerIndex", snapshot.currentPlayerIndex)
                put("sessionSeed", snapshot.sessionSeed)
                put("savedAtMillis", snapshot.savedAtMillis)

                put("currentSetPlayerIds", JSONArray().apply {
                    snapshot.currentSetPlayers.forEach { put(it.id) }
                })

                put("unsoldPoolIds", JSONArray().apply {
                    snapshot.unsoldPool.forEach { put(it.id) }
                })

                val franchisesObj = JSONObject()
                snapshot.franchises.forEach { (id, state) ->
                    val fObj = JSONObject().apply {
                        put("purseLakhs", state.purseLakhs)
                        put("isUserControlled", state.isUserControlled)
                        put("squad", JSONArray().apply {
                            state.squad.forEach { acq ->
                                put(JSONObject().apply {
                                    put("playerId", acq.player.id)
                                    put("boughtPriceLakhs", acq.boughtPriceLakhs)
                                    put("trueMarketValueLakhs", acq.trueMarketValueLakhs)
                                    put("setAcquired", acq.setAcquired.name)
                                })
                            }
                        })
                    }
                    franchisesObj.put(id.name, fObj)
                }
                put("franchises", franchisesObj)

                put("currentSetRecords", encodeSoldRecords(snapshot.currentSetRecords))
                put("allRecords", encodeSoldRecords(snapshot.allRecords))
                put("paddleFeed", JSONArray().apply {
                    snapshot.paddleFeed.take(24).forEach { evt ->
                        put(JSONObject().apply {
                            put("id", evt.id)
                            put("franchiseId", evt.franchiseId?.name ?: JSONObject.NULL)
                            put("type", evt.type.name)
                            put("amountLakhs", evt.amountLakhs ?: JSONObject.NULL)
                            put("message", evt.message)
                        })
                    }
                })
            }

            prefs.edit().putString(KEY_SAVED_AUCTION_JSON, root.toString()).apply()
        } catch (_: Throwable) {
            // Never crash gameplay on storage serialization error
        }
    }

    fun loadSnapshot(): PersistedAuctionSnapshot? {
        val raw = prefs.getString(KEY_SAVED_AUCTION_JSON, null) ?: return null
        return try {
            val root = JSONObject(raw)
            if (root.optInt("version", 0) != SCHEMA_VERSION) return null

            val phase = AuctionPhase.valueOf(root.getString("phase"))
            if (phase != AuctionPhase.LIVE_AUCTION &&
                phase != AuctionPhase.SET_SUMMARY &&
                phase != AuctionPhase.ACCELERATED_NOMINATION
            ) {
                return null
            }

            val selectedFranchiseId = FranchiseId.valueOf(root.getString("selectedFranchiseId"))
            val currentSet = AuctionSet.valueOf(root.getString("currentSet"))
            val currentPlayerIndex = root.getInt("currentPlayerIndex")
            val sessionSeed = root.optInt("sessionSeed", 42)
            val savedAtMillis = root.optLong("savedAtMillis", System.currentTimeMillis())

            val setPlayers = decodePlayerList(root.getJSONArray("currentSetPlayerIds")).map { p ->
                if (currentSet == AuctionSet.ACCELERATED_ROUND) p.copy(set = AuctionSet.ACCELERATED_ROUND) else p
            }
            val unsoldPool = decodePlayerList(root.getJSONArray("unsoldPoolIds"))

            val franchisesObj = root.getJSONObject("franchises")
            val franchises = FranchiseId.entries.associateWith { id ->
                val fObj = franchisesObj.getJSONObject(id.name)
                val squadArr = fObj.getJSONArray("squad")
                val squad = buildList {
                    for (i in 0 until squadArr.length()) {
                        val item = squadArr.getJSONObject(i)
                        val player = playerById[item.getString("playerId")] ?: continue
                        add(
                            AcquiredPlayer(
                                player = player,
                                boughtPriceLakhs = item.getInt("boughtPriceLakhs"),
                                trueMarketValueLakhs = item.getInt("trueMarketValueLakhs"),
                                setAcquired = AuctionSet.valueOf(item.getString("setAcquired"))
                            )
                        )
                    }
                }
                FranchiseState(
                    id = id,
                    purseLakhs = fObj.getInt("purseLakhs"),
                    squad = squad,
                    isUserControlled = fObj.optBoolean("isUserControlled", id == selectedFranchiseId)
                )
            }

            val currentSetRecords = decodeSoldRecords(root.getJSONArray("currentSetRecords"))
            val allRecords = decodeSoldRecords(root.getJSONArray("allRecords"))

            val feedArr = root.optJSONArray("paddleFeed") ?: JSONArray()
            val paddleFeed = buildList {
                for (i in 0 until feedArr.length()) {
                    val item = feedArr.getJSONObject(i)
                    val fStr = if (item.isNull("franchiseId")) null else item.optString("franchiseId", "")
                    val fId = fStr?.takeIf { it.isNotEmpty() }?.let { FranchiseId.valueOf(it) }
                    val amount = if (item.isNull("amountLakhs")) null else item.optInt("amountLakhs")
                    add(
                        PaddleEvent(
                            id = item.getLong("id"),
                            franchiseId = fId,
                            type = PaddleEventType.valueOf(item.getString("type")),
                            amountLakhs = amount,
                            message = item.getString("message")
                        )
                    )
                }
            }

            PersistedAuctionSnapshot(
                phase = phase,
                selectedFranchiseId = selectedFranchiseId,
                franchises = franchises,
                currentSet = currentSet,
                currentSetPlayers = setPlayers,
                currentPlayerIndex = currentPlayerIndex,
                currentSetRecords = currentSetRecords,
                allRecords = allRecords,
                unsoldPool = unsoldPool,
                paddleFeed = paddleFeed,
                sessionSeed = sessionSeed,
                savedAtMillis = savedAtMillis
            )
        } catch (_: Throwable) {
            clearSavedAuction()
            null
        }
    }

    fun loadSummary(): SavedAuctionSummary? {
        val snap = loadSnapshot() ?: return null
        val userState = snap.franchises[snap.selectedFranchiseId] ?: return null
        return SavedAuctionSummary(
            userFranchiseId = snap.selectedFranchiseId,
            currentSet = snap.currentSet,
            currentPlayerIndex = snap.currentPlayerIndex,
            totalInCurrentSet = snap.currentSetPlayers.size,
            userSquadCount = userState.squadCount,
            userPurseLakhs = userState.purseLakhs,
            totalProcessedPlayers = snap.allRecords.size,
            savedAtMillis = snap.savedAtMillis
        )
    }

    fun clearSavedAuction() {
        prefs.edit().remove(KEY_SAVED_AUCTION_JSON).apply()
    }

    private fun encodeSoldRecords(records: List<SoldRecord>): JSONArray {
        return JSONArray().apply {
            records.forEach { rec ->
                put(JSONObject().apply {
                    put("playerId", rec.player.id)
                    put("winningFranchiseId", rec.winningFranchiseId?.name ?: JSONObject.NULL)
                    put("soldPriceLakhs", rec.soldPriceLakhs)
                    put("trueMarketValueLakhs", rec.trueMarketValueLakhs)
                    put("setSoldIn", rec.setSoldIn.name)
                })
            }
        }
    }

    private fun decodeSoldRecords(array: JSONArray): List<SoldRecord> {
        return buildList {
            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)
                val player = playerById[item.getString("playerId")] ?: continue
                val winStr = if (item.isNull("winningFranchiseId")) null else item.optString("winningFranchiseId", "")
                val winId = winStr?.takeIf { it.isNotEmpty() }?.let { FranchiseId.valueOf(it) }
                add(
                    SoldRecord(
                        player = player,
                        winningFranchiseId = winId,
                        soldPriceLakhs = item.getInt("soldPriceLakhs"),
                        trueMarketValueLakhs = item.getInt("trueMarketValueLakhs"),
                        setSoldIn = AuctionSet.valueOf(item.getString("setSoldIn"))
                    )
                )
            }
        }
    }

    private fun decodePlayerList(array: JSONArray): List<Player> {
        return buildList {
            for (i in 0 until array.length()) {
                playerById[array.getString(i)]?.let { add(it) }
            }
        }
    }

    companion object {
        private const val PREFS_NAME = "ipl_mega_auction_local_store"
        private const val KEY_SAVED_AUCTION_JSON = "saved_auction_snapshot_v1"
        private const val KEY_SOUND_MUTED = "broadcast_sound_muted"
        private const val SCHEMA_VERSION = 1
    }
}
