package com.example.data

import com.example.model.AuctionSet
import com.example.model.Nationality
import com.example.model.Player
import com.example.model.PlayerArchetype
import com.example.model.PlayerRole
import com.example.model.PlayerStatus

internal object PlayerDatabaseAllRounderWk {

    val cappedAllRoundersSet: List<Player> = listOf(
        Player(
            id = "ar_venkatesh", name = "Venkatesh Iyer", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 200, form = 88, hype = 90, age = 30, iplMatches = 50,
            primaryStatLabel = "RUNS", primaryStatValue = "1326",
            secondaryStatLabel = "SR", secondaryStatValue = "137.1",
            battingStyle = "LHB", bowlingStyle = "Right-Arm Med", isCaptainMaterial = true
        ),
        Player(
            id = "ar_nitishreddy", name = "Nitish Kumar Reddy", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 100, form = 88, hype = 89, age = 21, iplMatches = 13,
            primaryStatLabel = "RUNS", primaryStatValue = "303",
            secondaryStatLabel = "SR", secondaryStatValue = "142.9",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med-Fast"
        ),
        Player(
            id = "ar_camgreen", name = "Cameron Green", country = "Australia",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 200, form = 88, hype = 89, age = 25, iplMatches = 29,
            primaryStatLabel = "RUNS", primaryStatValue = "707",
            secondaryStatLabel = "WKTS", secondaryStatValue = "16",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast-Med"
        ),
        Player(
            id = "ar_livingstone", name = "Liam Livingstone", country = "England",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.SPIN_ALL_ROUNDER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 200, form = 87, hype = 88, age = 31, iplMatches = 39,
            primaryStatLabel = "RUNS", primaryStatValue = "939",
            secondaryStatLabel = "SR", secondaryStatValue = "162.5",
            battingStyle = "RHB", bowlingStyle = "Legbreak / Offbreak"
        ),
        Player(
            id = "ar_stoinis", name = "Marcus Stoinis", country = "Australia",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 200, form = 87, hype = 87, age = 35, iplMatches = 96,
            primaryStatLabel = "RUNS", primaryStatValue = "1866",
            secondaryStatLabel = "WKTS", secondaryStatValue = "43",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med"
        ),
        Player(
            id = "ar_samcurran", name = "Sam Curran", country = "England",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 200, form = 86, hype = 87, age = 26, iplMatches = 59,
            primaryStatLabel = "WKTS", primaryStatValue = "58",
            secondaryStatLabel = "RUNS", secondaryStatValue = "883",
            battingStyle = "LHB", bowlingStyle = "Left-Arm Med-Fast", isCaptainMaterial = true
        ),
        Player(
            id = "ar_maxwell", name = "Glenn Maxwell", country = "Australia",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.SPIN_ALL_ROUNDER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 200, form = 83, hype = 88, age = 36, iplMatches = 134,
            primaryStatLabel = "RUNS", primaryStatValue = "2771",
            secondaryStatLabel = "SR", secondaryStatValue = "156.7",
            battingStyle = "RHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "ar_jansen", name = "Marco Jansen", country = "South Africa",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 125, form = 87, hype = 86, age = 24, iplMatches = 21,
            primaryStatLabel = "WKTS", primaryStatValue = "20",
            secondaryStatLabel = "ECON", secondaryStatValue = "9.12",
            battingStyle = "RHB", bowlingStyle = "Left-Arm Fast"
        ),
        Player(
            id = "ar_marsh", name = "Mitchell Marsh", country = "Australia",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 200, form = 85, hype = 85, age = 33, iplMatches = 42,
            primaryStatLabel = "RUNS", primaryStatValue = "665",
            secondaryStatLabel = "WKTS", secondaryStatValue = "37",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med-Fast", isCaptainMaterial = true
        ),
        Player(
            id = "ar_hasaranga", name = "Wanindu Hasaranga", country = "Sri Lanka",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.SPIN_ALL_ROUNDER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 200, form = 86, hype = 84, age = 27, iplMatches = 26,
            primaryStatLabel = "WKTS", primaryStatValue = "35",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.13",
            battingStyle = "RHB", bowlingStyle = "Legbreak Googly"
        ),
        Player(
            id = "ar_sundar", name = "Washington Sundar", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.SPIN_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 200, form = 85, hype = 84, age = 25, iplMatches = 60,
            primaryStatLabel = "WKTS", primaryStatValue = "37",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.54",
            battingStyle = "LHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "ar_krunal", name = "Krunal Pandya", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.SPIN_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 200, form = 84, hype = 84, age = 33, iplMatches = 127,
            primaryStatLabel = "RUNS", primaryStatValue = "1647",
            secondaryStatLabel = "WKTS", secondaryStatValue = "76",
            battingStyle = "LHB", bowlingStyle = "Slow Left-Arm Orthodox"
        ),
        Player(
            id = "ar_deepakchahar", name = "Deepak Chahar", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 200, form = 84, hype = 85, age = 32, iplMatches = 81,
            primaryStatLabel = "WKTS", primaryStatValue = "77",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.98",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med-Fast"
        ),
        Player(
            id = "ar_harshal", name = "Harshal Patel", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 200, form = 85, hype = 84, age = 34, iplMatches = 106,
            primaryStatLabel = "WKTS", primaryStatValue = "135",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.74",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med-Fast"
        ),
        Player(
            id = "ar_rachin", name = "Rachin Ravindra", country = "New Zealand",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.SPIN_ALL_ROUNDER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 150, form = 85, hype = 85, age = 25, iplMatches = 10,
            primaryStatLabel = "RUNS", primaryStatValue = "222",
            secondaryStatLabel = "SR", secondaryStatValue = "160.9",
            battingStyle = "LHB", bowlingStyle = "Slow Left-Arm Orthodox"
        ),
        Player(
            id = "ar_santner", name = "Mitchell Santner", country = "New Zealand",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.SPIN_ALL_ROUNDER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 200, form = 85, hype = 81, age = 33, iplMatches = 18,
            primaryStatLabel = "WKTS", primaryStatValue = "15",
            secondaryStatLabel = "ECON", secondaryStatValue = "6.92",
            battingStyle = "LHB", bowlingStyle = "Slow Left-Arm Orthodox"
        ),
        Player(
            id = "ar_omarzai", name = "Azmatullah Omarzai", country = "Afghanistan",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 150, form = 83, hype = 80, age = 24, iplMatches = 7,
            primaryStatLabel = "WKTS", primaryStatValue = "4",
            secondaryStatLabel = "SR", secondaryStatValue = "128.0",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med-Fast"
        ),
        Player(
            id = "ar_shepherd", name = "Romario Shepherd", country = "West Indies",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 150, form = 81, hype = 80, age = 30, iplMatches = 10,
            primaryStatLabel = "SR", primaryStatValue = "182.9",
            secondaryStatLabel = "WKTS", secondaryStatValue = "4",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast-Med"
        ),
        Player(
            id = "ar_bethell", name = "Jacob Bethell", country = "England",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.SPIN_ALL_ROUNDER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 125, form = 82, hype = 81, age = 21, iplMatches = 0,
            primaryStatLabel = "T20I SR", primaryStatValue = "167.9",
            secondaryStatLabel = "AVG", secondaryStatValue = "57.7",
            battingStyle = "LHB", bowlingStyle = "Slow Left-Arm Orthodox"
        ),
        Player(
            id = "ar_shardul", name = "Shardul Thakur", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 150, form = 79, hype = 80, age = 33, iplMatches = 95,
            primaryStatLabel = "WKTS", primaryStatValue = "94",
            secondaryStatLabel = "ECON", secondaryStatValue = "9.23",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast-Med"
        ),
        Player(
            id = "ar_moeen", name = "Moeen Ali", country = "England",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.SPIN_ALL_ROUNDER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 200, form = 79, hype = 78, age = 37, iplMatches = 67,
            primaryStatLabel = "RUNS", primaryStatValue = "1162",
            secondaryStatLabel = "WKTS", secondaryStatValue = "35",
            battingStyle = "LHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "ar_raza", name = "Sikandar Raza", country = "Zimbabwe",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.SPIN_ALL_ROUNDER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 125, form = 80, hype = 78, age = 38, iplMatches = 9,
            primaryStatLabel = "T20I RUNS", primaryStatValue = "2245",
            secondaryStatLabel = "WKTS", secondaryStatValue = "76",
            battingStyle = "RHB", bowlingStyle = "Offbreak / Legbreak"
        ),
        Player(
            id = "ar_overton", name = "Jamie Overton", country = "England",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 150, form = 80, hype = 78, age = 30, iplMatches = 0,
            primaryStatLabel = "BBL SR", primaryStatValue = "162.4",
            secondaryStatLabel = "TOP SPD", secondaryStatValue = "146 kph",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast"
        ),
        Player(
            id = "ar_hardie", name = "Aaron Hardie", country = "Australia",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 125, form = 79, hype = 77, age = 26, iplMatches = 0,
            primaryStatLabel = "BBL RUNS", primaryStatValue = "1510",
            secondaryStatLabel = "SR", secondaryStatValue = "136.5",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med-Fast"
        ),
        Player(
            id = "ar_daryl", name = "Daryl Mitchell", country = "New Zealand",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 200, form = 79, hype = 77, age = 33, iplMatches = 15,
            primaryStatLabel = "RUNS", primaryStatValue = "351",
            secondaryStatLabel = "SR", secondaryStatValue = "138.2",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med"
        ),
        Player(
            id = "ar_holder", name = "Jason Holder", country = "West Indies",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 150, form = 77, hype = 75, age = 33, iplMatches = 46,
            primaryStatLabel = "WKTS", primaryStatValue = "53",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.82",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast-Med"
        ),
        Player(
            id = "ar_hooda", name = "Deepak Hooda", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.SPIN_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 75, form = 75, hype = 74, age = 29, iplMatches = 118,
            primaryStatLabel = "RUNS", primaryStatValue = "1465",
            secondaryStatLabel = "SR", secondaryStatValue = "129.5",
            battingStyle = "RHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "ar_shankar", name = "Vijay Shankar", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_ALL_ROUNDERS,
            basePriceLakhs = 50, form = 75, hype = 72, age = 34, iplMatches = 72,
            primaryStatLabel = "RUNS", primaryStatValue = "1115",
            secondaryStatLabel = "SR", secondaryStatValue = "128.4",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med"
        )
    )

    val cappedWicketkeepersSet: List<Player> = listOf(
        Player(
            id = "wk_ishan", name = "Ishan Kishan", country = "India",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_WICKETKEEPERS,
            basePriceLakhs = 200, form = 88, hype = 90, age = 26, iplMatches = 105,
            primaryStatLabel = "RUNS", primaryStatValue = "2644",
            secondaryStatLabel = "SR", secondaryStatValue = "135.9",
            battingStyle = "LHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "wk_jurel", name = "Dhruv Jurel", country = "India",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.ANCHOR_WK,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_WICKETKEEPERS,
            basePriceLakhs = 150, form = 87, hype = 87, age = 24, iplMatches = 27,
            primaryStatLabel = "RUNS", primaryStatValue = "347",
            secondaryStatLabel = "SR", secondaryStatValue = "151.5",
            battingStyle = "RHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "wk_jitesh", name = "Jitesh Sharma", country = "India",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_WICKETKEEPERS,
            basePriceLakhs = 100, form = 85, hype = 86, age = 31, iplMatches = 40,
            primaryStatLabel = "RUNS", primaryStatValue = "730",
            secondaryStatLabel = "SR", secondaryStatValue = "151.1",
            battingStyle = "RHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "wk_qdk", name = "Quinton de Kock", country = "South Africa",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_WICKETKEEPERS,
            basePriceLakhs = 200, form = 85, hype = 85, age = 32, iplMatches = 107,
            primaryStatLabel = "RUNS", primaryStatValue = "3157",
            secondaryStatLabel = "SR", secondaryStatValue = "134.2",
            battingStyle = "LHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "wk_inglis", name = "Josh Inglis", country = "Australia",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_WICKETKEEPERS,
            basePriceLakhs = 150, form = 85, hype = 83, age = 29, iplMatches = 0,
            primaryStatLabel = "T20I SR", primaryStatValue = "157.8",
            secondaryStatLabel = "RUNS", secondaryStatValue = "679",
            battingStyle = "RHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "wk_rickelton", name = "Ryan Rickelton", country = "South Africa",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_WICKETKEEPERS,
            basePriceLakhs = 100, form = 84, hype = 81, age = 28, iplMatches = 0,
            primaryStatLabel = "SA20 SR", primaryStatValue = "173.8",
            secondaryStatLabel = "RUNS", secondaryStatValue = "530",
            battingStyle = "LHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "wk_gurbaz", name = "Rahmanullah Gurbaz", country = "Afghanistan",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_WICKETKEEPERS,
            basePriceLakhs = 150, form = 83, hype = 81, age = 23, iplMatches = 14,
            primaryStatLabel = "RUNS", primaryStatValue = "289",
            secondaryStatLabel = "SR", secondaryStatValue = "133.8",
            battingStyle = "RHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "wk_bairstow", name = "Jonny Bairstow", country = "England",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_WICKETKEEPERS,
            basePriceLakhs = 200, form = 80, hype = 80, age = 35, iplMatches = 50,
            primaryStatLabel = "RUNS", primaryStatValue = "1589",
            secondaryStatLabel = "SR", secondaryStatValue = "144.5",
            battingStyle = "RHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "wk_prabhsimran", name = "Prabhsimran Singh", country = "India",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.CAPPED_WICKETKEEPERS,
            basePriceLakhs = 50, form = 83, hype = 83, age = 24, iplMatches = 34,
            primaryStatLabel = "RUNS", primaryStatValue = "756",
            secondaryStatLabel = "SR", secondaryStatValue = "146.2",
            battingStyle = "RHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "wk_ferreira", name = "Donovan Ferreira", country = "South Africa",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_WICKETKEEPERS,
            basePriceLakhs = 75, form = 78, hype = 76, age = 26, iplMatches = 2,
            primaryStatLabel = "T20 SR", primaryStatValue = "159.4",
            secondaryStatLabel = "AVG", secondaryStatValue = "27.1",
            battingStyle = "RHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "wk_hope", name = "Shai Hope", country = "West Indies",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.ANCHOR_WK,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_WICKETKEEPERS,
            basePriceLakhs = 125, form = 78, hype = 75, age = 31, iplMatches = 9,
            primaryStatLabel = "RUNS", primaryStatValue = "183",
            secondaryStatLabel = "SR", secondaryStatValue = "150.0",
            battingStyle = "RHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "wk_seifert", name = "Tim Seifert", country = "New Zealand",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_WICKETKEEPERS,
            basePriceLakhs = 100, form = 77, hype = 74, age = 30, iplMatches = 3,
            primaryStatLabel = "T20I SR", primaryStatValue = "137.4",
            secondaryStatLabel = "RUNS", secondaryStatValue = "1365",
            battingStyle = "RHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "wk_rawat", name = "Anuj Rawat", country = "India",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.CAPPED_WICKETKEEPERS,
            basePriceLakhs = 30, form = 76, hype = 75, age = 25, iplMatches = 24,
            primaryStatLabel = "RUNS", primaryStatValue = "318",
            secondaryStatLabel = "SR", secondaryStatValue = "119.1",
            battingStyle = "LHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "wk_bharat", name = "KS Bharat", country = "India",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.ANCHOR_WK,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_WICKETKEEPERS,
            basePriceLakhs = 75, form = 74, hype = 73, age = 31, iplMatches = 10,
            primaryStatLabel = "RUNS", primaryStatValue = "199",
            secondaryStatLabel = "AVG", secondaryStatValue = "33.2",
            battingStyle = "RHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "wk_vinod", name = "Vishnu Vinod", country = "India",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.CAPPED_WICKETKEEPERS,
            basePriceLakhs = 30, form = 75, hype = 73, age = 31, iplMatches = 6,
            primaryStatLabel = "SMAT SR", primaryStatValue = "148.5",
            secondaryStatLabel = "RUNS", secondaryStatValue = "1420",
            battingStyle = "RHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "wk_billings", name = "Sam Billings", country = "England",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.ANCHOR_WK,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_WICKETKEEPERS,
            basePriceLakhs = 125, form = 75, hype = 72, age = 33, iplMatches = 30,
            primaryStatLabel = "RUNS", primaryStatValue = "503",
            secondaryStatLabel = "SR", secondaryStatValue = "129.6",
            battingStyle = "RHB", bowlingStyle = "Wicketkeeper"
        ),
        Player(
            id = "wk_carey", name = "Alex Carey", country = "Australia",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.ANCHOR_WK,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_WICKETKEEPERS,
            basePriceLakhs = 100, form = 74, hype = 71, age = 33, iplMatches = 3,
            primaryStatLabel = "BBL SR", primaryStatValue = "131.5",
            secondaryStatLabel = "AVG", secondaryStatValue = "32.1",
            battingStyle = "LHB", bowlingStyle = "Wicketkeeper"
        )
    )
}
