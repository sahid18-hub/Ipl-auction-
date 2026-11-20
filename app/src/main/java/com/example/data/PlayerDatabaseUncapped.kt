package com.example.data

import com.example.model.AuctionSet
import com.example.model.Nationality
import com.example.model.Player
import com.example.model.PlayerArchetype
import com.example.model.PlayerRole
import com.example.model.PlayerStatus

internal object PlayerDatabaseUncapped {

    val uncappedPlayersSet: List<Player> = listOf(
        // ===================== UNCAPPED BATTERS (18) =====================
        Player(
            id = "unc_shashank", name = "Shashank Singh", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.FINISHER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 50, form = 85, hype = 84, age = 33, iplMatches = 24,
            primaryStatLabel = "RUNS", primaryStatValue = "423",
            secondaryStatLabel = "SR", secondaryStatValue = "161.5",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med"
        ),
        Player(
            id = "unc_ashutosh", name = "Ashutosh Sharma", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.FINISHER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 84, hype = 84, age = 26, iplMatches = 11,
            primaryStatLabel = "RUNS", primaryStatValue = "189",
            secondaryStatLabel = "SR", secondaryStatValue = "167.3",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med"
        ),
        Player(
            id = "unc_wadhera", name = "Nehal Wadhera", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.MIDDLE_ORDER_ENFORCER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 83, hype = 84, age = 24, iplMatches = 20,
            primaryStatLabel = "RUNS", primaryStatValue = "350",
            secondaryStatLabel = "SR", secondaryStatValue = "140.0",
            battingStyle = "LHB", bowlingStyle = "Legbreak"
        ),
        Player(
            id = "unc_raghuvanshi", name = "Angkrish Raghuvanshi", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.TOP_ORDER_ANCHOR,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 82, hype = 83, age = 20, iplMatches = 10,
            primaryStatLabel = "RUNS", primaryStatValue = "163",
            secondaryStatLabel = "SR", secondaryStatValue = "155.2",
            battingStyle = "RHB", bowlingStyle = "Left-Arm Orthodox"
        ),
        Player(
            id = "unc_namandhir", name = "Naman Dhir", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.FINISHER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 82, hype = 83, age = 25, iplMatches = 7,
            primaryStatLabel = "RUNS", primaryStatValue = "140",
            secondaryStatLabel = "SR", secondaryStatValue = "177.2",
            battingStyle = "RHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "unc_priyansh", name = "Priyansh Arya", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.POWERPLAY_OPENER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 83, hype = 84, age = 24, iplMatches = 0,
            primaryStatLabel = "SMAT SR", primaryStatValue = "176.4",
            secondaryStatLabel = "RUNS", secondaryStatValue = "325",
            battingStyle = "LHB", bowlingStyle = "Right-Arm Med"
        ),
        Player(
            id = "unc_badoni", name = "Ayush Badoni", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.FINISHER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 81, hype = 81, age = 25, iplMatches = 42,
            primaryStatLabel = "RUNS", primaryStatValue = "634",
            secondaryStatLabel = "SR", secondaryStatValue = "134.0",
            battingStyle = "RHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "unc_abhinav", name = "Abhinav Manohar", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.FINISHER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 80, hype = 80, age = 30, iplMatches = 19,
            primaryStatLabel = "RUNS", primaryStatValue = "231",
            secondaryStatLabel = "SR", secondaryStatValue = "132.8",
            battingStyle = "RHB", bowlingStyle = "Legbreak"
        ),
        Player(
            id = "unc_rizvi", name = "Sameer Rizvi", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.FINISHER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 78, hype = 80, age = 21, iplMatches = 8,
            primaryStatLabel = "RUNS", primaryStatValue = "51",
            secondaryStatLabel = "UPT20 SR", secondaryStatValue = "188.0",
            battingStyle = "RHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "unc_vaibhav", name = "Vaibhav Suryavanshi", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.POWERPLAY_OPENER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 77, hype = 83, age = 15, iplMatches = 0,
            primaryStatLabel = "U19 SR", primaryStatValue = "164.0",
            secondaryStatLabel = "HS", secondaryStatValue = "104",
            battingStyle = "LHB", bowlingStyle = "Left-Arm Orthodox"
        ),
        Player(
            id = "unc_musheer", name = "Musheer Khan", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.MIDDLE_ORDER_ENFORCER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 77, hype = 78, age = 20, iplMatches = 0,
            primaryStatLabel = "FC AVG", primaryStatValue = "51.1",
            secondaryStatLabel = "HS", secondaryStatValue = "203*",
            battingStyle = "RHB", bowlingStyle = "Left-Arm Orthodox"
        ),
        Player(
            id = "unc_taide", name = "Atharva Taide", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.POWERPLAY_OPENER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 76, hype = 75, age = 24, iplMatches = 9,
            primaryStatLabel = "RUNS", primaryStatValue = "247",
            secondaryStatLabel = "SR", secondaryStatValue = "147.0",
            battingStyle = "LHB", bowlingStyle = "Left-Arm Orthodox"
        ),
        Player(
            id = "unc_rasheed", name = "Shaik Rasheed", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.TOP_ORDER_ANCHOR,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 75, hype = 75, age = 20, iplMatches = 0,
            primaryStatLabel = "SMAT AVG", primaryStatValue = "39.4",
            secondaryStatLabel = "SR", secondaryStatValue = "134.2",
            battingStyle = "RHB", bowlingStyle = "Legbreak"
        ),
        Player(
            id = "unc_chikara", name = "Swastik Chikara", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.POWERPLAY_OPENER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 75, hype = 74, age = 19, iplMatches = 0,
            primaryStatLabel = "UPT20 SR", primaryStatValue = "185.7",
            secondaryStatLabel = "RUNS", secondaryStatValue = "499",
            battingStyle = "RHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "unc_shubhamdubey", name = "Shubham Dubey", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.FINISHER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 74, hype = 73, age = 30, iplMatches = 4,
            primaryStatLabel = "SMAT SR", primaryStatValue = "187.2",
            secondaryStatLabel = "RUNS", secondaryStatValue = "221",
            battingStyle = "LHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "unc_anmolpreet", name = "Anmolpreet Singh", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.POWERPLAY_OPENER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 73, hype = 71, age = 26, iplMatches = 9,
            primaryStatLabel = "RUNS", primaryStatValue = "139",
            secondaryStatLabel = "SMAT SR", secondaryStatValue = "144.8",
            battingStyle = "RHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "unc_harnoor", name = "Harnoor Pannu", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.TOP_ORDER_ANCHOR,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 72, hype = 71, age = 22, iplMatches = 0,
            primaryStatLabel = "SMAT SR", primaryStatValue = "138.0",
            secondaryStatLabel = "AVG", secondaryStatValue = "34.2",
            battingStyle = "LHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "unc_andresiddarth", name = "Andre Siddarth", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.MIDDLE_ORDER_ENFORCER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 73, hype = 72, age = 19, iplMatches = 0,
            primaryStatLabel = "FC AVG", primaryStatValue = "68.2",
            secondaryStatLabel = "TNPL SR", secondaryStatValue = "141.5",
            battingStyle = "RHB", bowlingStyle = "Offbreak"
        ),

        // ===================== UNCAPPED ALL-ROUNDERS (14) =====================
        Player(
            id = "unc_ramandeep", name = "Ramandeep Singh", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 50, form = 84, hype = 84, age = 27, iplMatches = 20,
            primaryStatLabel = "SR", primaryStatValue = "170.0",
            secondaryStatLabel = "WKTS", secondaryStatValue = "6",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med"
        ),
        Player(
            id = "unc_tewatia", name = "Rahul Tewatia", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.SPIN_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 50, form = 84, hype = 84, age = 31, iplMatches = 93,
            primaryStatLabel = "RUNS", primaryStatValue = "1013",
            secondaryStatLabel = "SR", secondaryStatValue = "134.9",
            battingStyle = "LHB", bowlingStyle = "Legbreak"
        ),
        Player(
            id = "unc_shahrukh", name = "Shahrukh Khan", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.SPIN_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 50, form = 82, hype = 82, age = 29, iplMatches = 40,
            primaryStatLabel = "RUNS", primaryStatValue = "553",
            secondaryStatLabel = "SR", secondaryStatValue = "141.8",
            battingStyle = "RHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "unc_samad", name = "Abdul Samad", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.SPIN_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 81, hype = 81, age = 23, iplMatches = 50,
            primaryStatLabel = "RUNS", primaryStatValue = "577",
            secondaryStatLabel = "SR", secondaryStatValue = "146.1",
            battingStyle = "RHB", bowlingStyle = "Legbreak"
        ),
        Player(
            id = "unc_kamboj", name = "Anshul Kamboj", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 82, hype = 82, age = 24, iplMatches = 3,
            primaryStatLabel = "FC WKTS", primaryStatValue = "56",
            secondaryStatLabel = "BEST", secondaryStatValue = "10/49",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast-Med"
        ),
        Player(
            id = "unc_lomror", name = "Mahipal Lomror", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.SPIN_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 50, form = 79, hype = 79, age = 25, iplMatches = 40,
            primaryStatLabel = "RUNS", primaryStatValue = "527",
            secondaryStatLabel = "SR", secondaryStatValue = "141.3",
            battingStyle = "LHB", bowlingStyle = "Slow Left-Arm Orthodox"
        ),
        Player(
            id = "unc_swapnil", name = "Swapnil Singh", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.SPIN_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 78, hype = 77, age = 34, iplMatches = 14,
            primaryStatLabel = "WKTS", primaryStatValue = "7",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.72",
            battingStyle = "RHB", bowlingStyle = "Slow Left-Arm Orthodox"
        ),
        Player(
            id = "unc_shedge", name = "Suryansh Shedge", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 79, hype = 78, age = 22, iplMatches = 0,
            primaryStatLabel = "SMAT SR", primaryStatValue = "251.9",
            secondaryStatLabel = "WKTS", secondaryStatValue = "8",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med-Fast"
        ),
        Player(
            id = "unc_vipraj", name = "Vipraj Nigam", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.SPIN_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 78, hype = 77, age = 20, iplMatches = 0,
            primaryStatLabel = "UPT20 WKTS", primaryStatValue = "20",
            secondaryStatLabel = "SR", secondaryStatValue = "158.0",
            battingStyle = "RHB", bowlingStyle = "Legbreak"
        ),
        Player(
            id = "unc_sindhu", name = "Nishant Sindhu", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.SPIN_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 76, hype = 75, age = 20, iplMatches = 0,
            primaryStatLabel = "SMAT AVG", primaryStatValue = "34.8",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.20",
            battingStyle = "LHB", bowlingStyle = "Slow Left-Arm Orthodox"
        ),
        Player(
            id = "unc_bawa", name = "Raj Angad Bawa", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 75, hype = 74, age = 22, iplMatches = 2,
            primaryStatLabel = "U19 WKTS", primaryStatValue = "9",
            secondaryStatLabel = "SMAT SR", secondaryStatValue = "141.0",
            battingStyle = "LHB", bowlingStyle = "Right-Arm Med-Fast"
        ),
        Player(
            id = "unc_kulkarni", name = "Arshin Kulkarni", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 75, hype = 75, age = 20, iplMatches = 2,
            primaryStatLabel = "MPL SR", primaryStatValue = "168.4",
            secondaryStatLabel = "WKTS", secondaryStatValue = "10",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med-Fast"
        ),
        Player(
            id = "unc_bhandage", name = "Manoj Bhandage", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 74, hype = 73, age = 26, iplMatches = 0,
            primaryStatLabel = "KSCA SR", primaryStatValue = "172.0",
            secondaryStatLabel = "WKTS", secondaryStatValue = "14",
            battingStyle = "LHB", bowlingStyle = "Right-Arm Med"
        ),
        Player(
            id = "unc_mankad", name = "Prerak Mankad", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 73, hype = 71, age = 30, iplMatches = 6,
            primaryStatLabel = "RUNS", primaryStatValue = "97",
            secondaryStatLabel = "SMAT SR", secondaryStatValue = "146.5",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med"
        ),

        // ===================== UNCAPPED WICKETKEEPERS (7) =====================
        Player(
            id = "unc_porel", name = "Abishek Porel", country = "India",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 50, form = 83, hype = 83, age = 22, iplMatches = 18,
            primaryStatLabel = "RUNS", primaryStatValue = "360",
            secondaryStatLabel = "SR", secondaryStatValue = "156.5",
            battingStyle = "LHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "unc_urvil", name = "Urvil Patel", country = "India",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 79, hype = 78, age = 26, iplMatches = 0,
            primaryStatLabel = "SMAT SR", primaryStatValue = "229.9",
            secondaryStatLabel = "100s", secondaryStatValue = "2",
            battingStyle = "RHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "unc_minz", name = "Robin Minz", country = "India",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 77, hype = 79, age = 22, iplMatches = 0,
            primaryStatLabel = "T20 SR", primaryStatValue = "162.0",
            secondaryStatLabel = "SIXES", secondaryStatValue = "24",
            battingStyle = "LHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "unc_kushagra", name = "Kumar Kushagra", country = "India",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.ANCHOR_WK,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 75, hype = 75, age = 20, iplMatches = 4,
            primaryStatLabel = "SMAT SR", primaryStatValue = "141.2",
            secondaryStatLabel = "AVG", secondaryStatValue = "36.5",
            battingStyle = "RHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "unc_juyal", name = "Aryan Juyal", country = "India",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.ANCHOR_WK,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 74, hype = 72, age = 23, iplMatches = 0,
            primaryStatLabel = "SMAT AVG", primaryStatValue = "42.3",
            secondaryStatLabel = "SR", secondaryStatValue = "136.0",
            battingStyle = "RHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "unc_sisodia", name = "Luvnith Sisodia", country = "India",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 73, hype = 72, age = 25, iplMatches = 0,
            primaryStatLabel = "KSCA SR", primaryStatValue = "164.5",
            secondaryStatLabel = "RUNS", secondaryStatValue = "315",
            battingStyle = "LHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "unc_vanshbedi", name = "Vansh Bedi", country = "India",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 73, hype = 73, age = 22, iplMatches = 0,
            primaryStatLabel = "DPL SR", primaryStatValue = "185.7",
            secondaryStatLabel = "SIXES", secondaryStatValue = "18",
            battingStyle = "RHB", bowlingStyle = "Wicketkeeper"
        ),

        // ===================== UNCAPPED FAST BOWLERS (13) =====================
        Player(
            id = "unc_harshitrana", name = "Harshit Rana", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.DEATH_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 50, form = 85, hype = 85, age = 23, iplMatches = 21,
            primaryStatLabel = "WKTS", primaryStatValue = "25",
            secondaryStatLabel = "SR", secondaryStatValue = "15.1",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast"
        ),
        Player(
            id = "unc_yashdayal", name = "Yash Dayal", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.LEFT_ARM_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 50, form = 84, hype = 83, age = 27, iplMatches = 28,
            primaryStatLabel = "WKTS", primaryStatValue = "28",
            secondaryStatLabel = "ECON", secondaryStatValue = "9.53",
            battingStyle = "LHB", bowlingStyle = "Left-Arm Fast-Med"
        ),
        Player(
            id = "unc_tushar", name = "Tushar Deshpande", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.DEATH_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 50, form = 83, hype = 83, age = 29, iplMatches = 36,
            primaryStatLabel = "WKTS", primaryStatValue = "42",
            secondaryStatLabel = "BEST", secondaryStatValue = "4/27",
            battingStyle = "LHB", bowlingStyle = "Right-Arm Med-Fast"
        ),
        Player(
            id = "unc_rasikh", name = "Rasikh Salam", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.DEATH_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 83, hype = 83, age = 24, iplMatches = 11,
            primaryStatLabel = "WKTS", primaryStatValue = "9",
            secondaryStatLabel = "ECON", secondaryStatValue = "10.6",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast-Med"
        ),
        Player(
            id = "unc_vaibhavarora", name = "Vaibhav Arora", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.SWING_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 82, hype = 81, age = 27, iplMatches = 20,
            primaryStatLabel = "WKTS", primaryStatValue = "19",
            secondaryStatLabel = "ECON", secondaryStatValue = "9.18",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast-Med"
        ),
        Player(
            id = "unc_vyshak", name = "Vijaykumar Vyshak", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.DEATH_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 80, hype = 79, age = 28, iplMatches = 11,
            primaryStatLabel = "WKTS", primaryStatValue = "13",
            secondaryStatLabel = "ECON", secondaryStatValue = "10.0",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med-Fast"
        ),
        Player(
            id = "unc_simarjeet", name = "Simarjeet Singh", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.EXPRESS_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 79, hype = 78, age = 27, iplMatches = 10,
            primaryStatLabel = "WKTS", primaryStatValue = "9",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.72",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast"
        ),
        Player(
            id = "unc_madhwal", name = "Akash Madhwal", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.DEATH_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 79, hype = 79, age = 31, iplMatches = 13,
            primaryStatLabel = "WKTS", primaryStatValue = "19",
            secondaryStatLabel = "BEST", secondaryStatValue = "5/5",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast-Med"
        ),
        Player(
            id = "unc_yashthakur", name = "Yash Thakur", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.DEATH_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 79, hype = 77, age = 26, iplMatches = 19,
            primaryStatLabel = "WKTS", primaryStatValue = "24",
            secondaryStatLabel = "BEST", secondaryStatValue = "5/30",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med-Fast"
        ),
        Player(
            id = "unc_kaverappa", name = "Vidwath Kaverappa", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.SWING_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 78, hype = 77, age = 26, iplMatches = 1,
            primaryStatLabel = "SMAT WKTS", primaryStatValue = "34",
            secondaryStatLabel = "ECON", secondaryStatValue = "6.84",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast-Med"
        ),
        Player(
            id = "unc_mukeshch", name = "Mukesh Choudhary", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.LEFT_ARM_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 77, hype = 76, age = 28, iplMatches = 14,
            primaryStatLabel = "WKTS", primaryStatValue = "16",
            secondaryStatLabel = "ECON", secondaryStatValue = "9.31",
            battingStyle = "LHB", bowlingStyle = "Left-Arm Med-Fast"
        ),
        Player(
            id = "unc_gurjapneet", name = "Gurjapneet Singh", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.LEFT_ARM_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 78, hype = 77, age = 26, iplMatches = 0,
            primaryStatLabel = "TNPL WKTS", primaryStatValue = "17",
            secondaryStatLabel = "HEIGHT", secondaryStatValue = "6'3\"",
            battingStyle = "RHB", bowlingStyle = "Left-Arm Fast-Med"
        ),
        Player(
            id = "unc_tyagi", name = "Kartik Tyagi", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.EXPRESS_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 75, hype = 75, age = 24, iplMatches = 20,
            primaryStatLabel = "WKTS", primaryStatValue = "15",
            secondaryStatLabel = "TOP SPD", secondaryStatValue = "147 kph",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast"
        ),

        // ===================== UNCAPPED SPINNERS (8) =====================
        Player(
            id = "unc_suyash", name = "Suyash Sharma", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.WRIST_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 81, hype = 82, age = 21, iplMatches = 13,
            primaryStatLabel = "WKTS", primaryStatValue = "10",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.63",
            battingStyle = "RHB", bowlingStyle = "Legbreak Googly"
        ),
        Player(
            id = "unc_suthar", name = "Manav Suthar", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.FINGER_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 79, hype = 78, age = 22, iplMatches = 1,
            primaryStatLabel = "FC WKTS", primaryStatValue = "95",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.12",
            battingStyle = "LHB", bowlingStyle = "Slow Left-Arm Orthodox"
        ),
        Player(
            id = "unc_karnsharma", name = "Karn Sharma", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.WRIST_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 50, form = 78, hype = 76, age = 37, iplMatches = 84,
            primaryStatLabel = "WKTS", primaryStatValue = "76",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.36",
            battingStyle = "LHB", bowlingStyle = "Legbreak"
        ),
        Player(
            id = "unc_digvesh", name = "Digvesh Rathi", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.MYSTERY_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 79, hype = 78, age = 25, iplMatches = 0,
            primaryStatLabel = "DPL WKTS", primaryStatValue = "14",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.82",
            battingStyle = "RHB", bowlingStyle = "Legbreak Mystery"
        ),
        Player(
            id = "unc_siddharth", name = "Manimaran Siddharth", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.FINGER_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 78, hype = 77, age = 26, iplMatches = 3,
            primaryStatLabel = "ECON", primaryStatValue = "7.89",
            secondaryStatLabel = "SMAT WKTS", secondaryStatValue = "26",
            battingStyle = "RHB", bowlingStyle = "Slow Left-Arm Orthodox"
        ),
        Player(
            id = "unc_kartikeya", name = "Kumar Kartikeya", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.WRIST_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 77, hype = 76, age = 27, iplMatches = 12,
            primaryStatLabel = "WKTS", primaryStatValue = "10",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.52",
            battingStyle = "RHB", bowlingStyle = "Left-Arm Wrist Spin"
        ),
        Player(
            id = "unc_puthur", name = "Vignesh Puthur", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.WRIST_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 76, hype = 76, age = 23, iplMatches = 0,
            primaryStatLabel = "KCL ECON", primaryStatValue = "7.10",
            secondaryStatLabel = "STYLE", secondaryStatValue = "Chinaman",
            battingStyle = "RHB", bowlingStyle = "Left-Arm Wrist Spin"
        ),
        Player(
            id = "unc_kotian", name = "Tanush Kotian", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.FINGER_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.UNCAPPED_PLAYERS,
            basePriceLakhs = 30, form = 77, hype = 75, age = 26, iplMatches = 1,
            primaryStatLabel = "FC WKTS", primaryStatValue = "98",
            secondaryStatLabel = "ECON", secondaryStatValue = "6.85",
            battingStyle = "RHB", bowlingStyle = "Offbreak"
        )
    )
}
