package com.example.data

import com.example.model.AuctionSet
import com.example.model.Nationality
import com.example.model.Player
import com.example.model.PlayerArchetype
import com.example.model.PlayerRole
import com.example.model.PlayerStatus

internal object PlayerDatabaseBowling {

    val cappedFastBowlersSet: List<Player> = listOf(
        Player(
            id = "pace_shami", name = "Mohammed Shami", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.SWING_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 200, form = 89, hype = 91, age = 34, iplMatches = 110,
            primaryStatLabel = "WKTS", primaryStatValue = "127",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.44",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast"
        ),
        Player(
            id = "pace_siraj", name = "Mohammed Siraj", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.SWING_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 200, form = 88, hype = 90, age = 30, iplMatches = 93,
            primaryStatLabel = "WKTS", primaryStatValue = "93",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.65",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast"
        ),
        Player(
            id = "pace_hazlewood", name = "Josh Hazlewood", country = "Australia",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.DEATH_PACER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 200, form = 90, hype = 89, age = 34, iplMatches = 27,
            primaryStatLabel = "WKTS", primaryStatValue = "35",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.06",
            battingStyle = "LHB", bowlingStyle = "Right-Arm Fast-Med"
        ),
        Player(
            id = "pace_archer", name = "Jofra Archer", country = "England",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.EXPRESS_PACER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 200, form = 88, hype = 90, age = 29, iplMatches = 40,
            primaryStatLabel = "WKTS", primaryStatValue = "48",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.43",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast"
        ),
        Player(
            id = "pace_pathirana", name = "Matheesha Pathirana", country = "Sri Lanka",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.DEATH_PACER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 150, form = 90, hype = 89, age = 22, iplMatches = 20,
            primaryStatLabel = "WKTS", primaryStatValue = "34",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.88",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Slingy Fast"
        ),
        Player(
            id = "pace_mayankyadav", name = "Mayank Yadav", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.EXPRESS_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 100, form = 87, hype = 89, age = 22, iplMatches = 4,
            primaryStatLabel = "TOP SPD", primaryStatValue = "156.7 kph",
            secondaryStatLabel = "WKTS", secondaryStatValue = "7",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Express"
        ),
        Player(
            id = "pace_bhuvi", name = "Bhuvneshwar Kumar", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.SWING_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 200, form = 86, hype = 87, age = 35, iplMatches = 176,
            primaryStatLabel = "WKTS", primaryStatValue = "181",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.56",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med-Fast"
        ),
        Player(
            id = "pace_natarajan", name = "T Natarajan", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.DEATH_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 200, form = 87, hype = 86, age = 33, iplMatches = 61,
            primaryStatLabel = "WKTS", primaryStatValue = "67",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.84",
            battingStyle = "LHB", bowlingStyle = "Left-Arm Med-Fast"
        ),
        Player(
            id = "pace_prasidh", name = "Prasidh Krishna", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.EXPRESS_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 200, form = 86, hype = 85, age = 29, iplMatches = 51,
            primaryStatLabel = "WKTS", primaryStatValue = "49",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.92",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast"
        ),
        Player(
            id = "pace_avesh", name = "Avesh Khan", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.DEATH_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 200, form = 85, hype = 85, age = 28, iplMatches = 63,
            primaryStatLabel = "WKTS", primaryStatValue = "74",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.90",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast"
        ),
        Player(
            id = "pace_khaleel", name = "Khaleel Ahmed", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.LEFT_ARM_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 200, form = 85, hype = 83, age = 27, iplMatches = 57,
            primaryStatLabel = "WKTS", primaryStatValue = "74",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.84",
            battingStyle = "RHB", bowlingStyle = "Left-Arm Fast-Med"
        ),
        Player(
            id = "pace_mukesh", name = "Mukesh Kumar", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.DEATH_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 200, form = 84, hype = 83, age = 31, iplMatches = 20,
            primaryStatLabel = "WKTS", primaryStatValue = "24",
            secondaryStatLabel = "ECON", secondaryStatValue = "10.37",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med-Fast"
        ),
        Player(
            id = "pace_akashdeep", name = "Akash Deep", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.SWING_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 100, form = 84, hype = 83, age = 28, iplMatches = 8,
            primaryStatLabel = "WKTS", primaryStatValue = "7",
            secondaryStatLabel = "FC WKTS", secondaryStatValue = "128",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast-Med"
        ),
        Player(
            id = "pace_sandeep", name = "Sandeep Sharma", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.DEATH_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 100, form = 85, hype = 81, age = 31, iplMatches = 127,
            primaryStatLabel = "WKTS", primaryStatValue = "137",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.88",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med"
        ),
        Player(
            id = "pace_nortje", name = "Anrich Nortje", country = "South Africa",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.EXPRESS_PACER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 200, form = 83, hype = 84, age = 31, iplMatches = 46,
            primaryStatLabel = "WKTS", primaryStatValue = "60",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.96",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Express"
        ),
        Player(
            id = "pace_wood", name = "Mark Wood", country = "England",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.EXPRESS_PACER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 200, form = 85, hype = 83, age = 35, iplMatches = 5,
            primaryStatLabel = "WKTS", primaryStatValue = "11",
            secondaryStatLabel = "TOP SPD", secondaryStatValue = "154 kph",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Express"
        ),
        Player(
            id = "pace_coetzee", name = "Gerald Coetzee", country = "South Africa",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.EXPRESS_PACER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 125, form = 83, hype = 82, age = 24, iplMatches = 10,
            primaryStatLabel = "WKTS", primaryStatValue = "13",
            secondaryStatLabel = "SR", secondaryStatValue = "15.2",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast"
        ),
        Player(
            id = "pace_ellis", name = "Nathan Ellis", country = "Australia",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.DEATH_PACER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 125, form = 84, hype = 80, age = 30, iplMatches = 16,
            primaryStatLabel = "WKTS", primaryStatValue = "18",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.62",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast-Med"
        ),
        Player(
            id = "pace_ferguson", name = "Lockie Ferguson", country = "New Zealand",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.EXPRESS_PACER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 200, form = 82, hype = 81, age = 33, iplMatches = 45,
            primaryStatLabel = "WKTS", primaryStatValue = "46",
            secondaryStatLabel = "ECON", secondaryStatValue = "9.00",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Express"
        ),
        Player(
            id = "pace_spencer", name = "Spencer Johnson", country = "Australia",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.LEFT_ARM_PACER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 150, form = 82, hype = 80, age = 29, iplMatches = 5,
            primaryStatLabel = "WKTS", primaryStatValue = "4",
            secondaryStatLabel = "BBL ECON", secondaryStatValue = "7.45",
            battingStyle = "LHB", bowlingStyle = "Left-Arm Fast"
        ),
        Player(
            id = "pace_fizz", name = "Mustafizur Rahman", country = "Bangladesh",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.LEFT_ARM_PACER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 150, form = 82, hype = 79, age = 29, iplMatches = 57,
            primaryStatLabel = "WKTS", primaryStatValue = "61",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.14",
            battingStyle = "LHB", bowlingStyle = "Left-Arm Cutter"
        ),
        Player(
            id = "pace_farooqi", name = "Fazalhaq Farooqi", country = "Afghanistan",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.LEFT_ARM_PACER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 150, form = 83, hype = 79, age = 24, iplMatches = 7,
            primaryStatLabel = "T20I WKTS", primaryStatValue = "54",
            secondaryStatLabel = "ECON", secondaryStatValue = "6.72",
            battingStyle = "RHB", bowlingStyle = "Left-Arm Fast-Med"
        ),
        Player(
            id = "pace_thushara", name = "Nuwan Thushara", country = "Sri Lanka",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.DEATH_PACER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 75, form = 81, hype = 78, age = 30, iplMatches = 7,
            primaryStatLabel = "WKTS", primaryStatValue = "8",
            secondaryStatLabel = "T20I ECON", secondaryStatValue = "7.65",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Slingy"
        ),
        Player(
            id = "pace_naveen", name = "Naveen-ul-Haq", country = "Afghanistan",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.DEATH_PACER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 150, form = 80, hype = 77, age = 25, iplMatches = 18,
            primaryStatLabel = "WKTS", primaryStatValue = "25",
            secondaryStatLabel = "ECON", secondaryStatValue = "9.16",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med-Fast"
        ),
        Player(
            id = "pace_mohit", name = "Mohit Sharma", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.DEATH_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 50, form = 80, hype = 77, age = 36, iplMatches = 112,
            primaryStatLabel = "WKTS", primaryStatValue = "132",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.72",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med"
        ),
        Player(
            id = "pace_topley", name = "Reece Topley", country = "England",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.LEFT_ARM_PACER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 75, form = 79, hype = 76, age = 31, iplMatches = 5,
            primaryStatLabel = "WKTS", primaryStatValue = "5",
            secondaryStatLabel = "T20I WKTS", secondaryStatValue = "33",
            battingStyle = "RHB", bowlingStyle = "Left-Arm Fast-Med"
        ),
        Player(
            id = "pace_maphaka", name = "Kwena Maphaka", country = "South Africa",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.LEFT_ARM_PACER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 75, form = 78, hype = 79, age = 19, iplMatches = 2,
            primaryStatLabel = "U19 WKTS", primaryStatValue = "21",
            secondaryStatLabel = "TOP SPD", secondaryStatValue = "146 kph",
            battingStyle = "LHB", bowlingStyle = "Left-Arm Fast"
        ),
        Player(
            id = "pace_umran", name = "Umran Malik", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.EXPRESS_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 75, form = 74, hype = 79, age = 25, iplMatches = 26,
            primaryStatLabel = "WKTS", primaryStatValue = "29",
            secondaryStatLabel = "TOP SPD", secondaryStatValue = "157 kph",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Express"
        ),
        Player(
            id = "pace_alzarri", name = "Alzarri Joseph", country = "West Indies",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.EXPRESS_PACER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 150, form = 76, hype = 75, age = 28, iplMatches = 22,
            primaryStatLabel = "WKTS", primaryStatValue = "21",
            secondaryStatLabel = "BEST", secondaryStatValue = "6/12",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast"
        ),
        Player(
            id = "pace_ishant", name = "Ishant Sharma", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.SWING_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 75, form = 77, hype = 74, age = 36, iplMatches = 110,
            primaryStatLabel = "WKTS", primaryStatValue = "92",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.24",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast-Med"
        ),
        Player(
            id = "pace_mavi", name = "Shivam Mavi", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.SWING_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 75, form = 75, hype = 74, age = 26, iplMatches = 32,
            primaryStatLabel = "WKTS", primaryStatValue = "30",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.71",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast-Med"
        ),
        Player(
            id = "pace_sakariya", name = "Chetan Sakariya", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.LEFT_ARM_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 50, form = 74, hype = 72, age = 27, iplMatches = 19,
            primaryStatLabel = "WKTS", primaryStatValue = "20",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.43",
            battingStyle = "LHB", bowlingStyle = "Left-Arm Med-Fast"
        ),
        Player(
            id = "pace_saini", name = "Navdeep Saini", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.EXPRESS_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 75, form = 72, hype = 70, age = 32, iplMatches = 32,
            primaryStatLabel = "WKTS", primaryStatValue = "23",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.90",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast"
        ),
        Player(
            id = "pace_umesh", name = "Umesh Yadav", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.SWING_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_FAST_BOWLERS,
            basePriceLakhs = 150, form = 72, hype = 70, age = 37, iplMatches = 148,
            primaryStatLabel = "WKTS", primaryStatValue = "144",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.45",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast"
        )
    )

    val cappedSpinnersSet: List<Player> = listOf(
        Player(
            id = "spin_varun", name = "Varun Chakaravarthy", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.MYSTERY_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 200, form = 90, hype = 90, age = 33, iplMatches = 71,
            primaryStatLabel = "WKTS", primaryStatValue = "83",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.56",
            battingStyle = "RHB", bowlingStyle = "Mystery Spin"
        ),
        Player(
            id = "spin_bishnoi", name = "Ravi Bishnoi", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.WRIST_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 200, form = 88, hype = 88, age = 24, iplMatches = 66,
            primaryStatLabel = "WKTS", primaryStatValue = "63",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.80",
            battingStyle = "RHB", bowlingStyle = "Legbreak Googly"
        ),
        Player(
            id = "spin_noor", name = "Noor Ahmad", country = "Afghanistan",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.WRIST_SPINNER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 150, form = 89, hype = 88, age = 20, iplMatches = 23,
            primaryStatLabel = "WKTS", primaryStatValue = "24",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.04",
            battingStyle = "RHB", bowlingStyle = "Left-Arm Wrist Spin"
        ),
        Player(
            id = "spin_ashwin", name = "Ravichandran Ashwin", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.FINGER_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 200, form = 85, hype = 86, age = 38, iplMatches = 212,
            primaryStatLabel = "WKTS", primaryStatValue = "180",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.12",
            battingStyle = "RHB", bowlingStyle = "Offbreak", isCaptainMaterial = true
        ),
        Player(
            id = "spin_theekshana", name = "Maheesh Theekshana", country = "Sri Lanka",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.MYSTERY_SPINNER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 150, form = 85, hype = 83, age = 24, iplMatches = 27,
            primaryStatLabel = "WKTS", primaryStatValue = "25",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.66",
            battingStyle = "RHB", bowlingStyle = "Mystery Offspin"
        ),
        Player(
            id = "spin_zampa", name = "Adam Zampa", country = "Australia",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.WRIST_SPINNER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 200, form = 85, hype = 82, age = 32, iplMatches = 20,
            primaryStatLabel = "WKTS", primaryStatValue = "29",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.14",
            battingStyle = "RHB", bowlingStyle = "Legbreak Googly"
        ),
        Player(
            id = "spin_saikishore", name = "R Sai Kishore", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.FINGER_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 75, form = 84, hype = 82, age = 28, iplMatches = 10,
            primaryStatLabel = "WKTS", primaryStatValue = "13",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.32",
            battingStyle = "LHB", bowlingStyle = "Slow Left-Arm Orthodox"
        ),
        Player(
            id = "spin_ghazanfar", name = "AM Ghazanfar", country = "Afghanistan",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.MYSTERY_SPINNER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 75, form = 84, hype = 83, age = 19, iplMatches = 0,
            primaryStatLabel = "ODI WKTS", primaryStatValue = "21",
            secondaryStatLabel = "ECON", secondaryStatValue = "4.05",
            battingStyle = "RHB", bowlingStyle = "Mystery Offspin"
        ),
        Player(
            id = "spin_rahulchahar", name = "Rahul Chahar", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.WRIST_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 100, form = 82, hype = 81, age = 25, iplMatches = 78,
            primaryStatLabel = "WKTS", primaryStatValue = "75",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.72",
            battingStyle = "RHB", bowlingStyle = "Legbreak Googly"
        ),
        Player(
            id = "spin_shahbaz", name = "Shahbaz Ahmed", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.FINGER_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 100, form = 80, hype = 78, age = 30, iplMatches = 55,
            primaryStatLabel = "RUNS", primaryStatValue = "536",
            secondaryStatLabel = "WKTS", secondaryStatValue = "21",
            battingStyle = "LHB", bowlingStyle = "Slow Left-Arm Orthodox"
        ),
        Player(
            id = "spin_brar", name = "Harpreet Brar", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.FINGER_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.UNCAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 50, form = 81, hype = 80, age = 29, iplMatches = 41,
            primaryStatLabel = "WKTS", primaryStatValue = "25",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.88",
            battingStyle = "LHB", bowlingStyle = "Slow Left-Arm Orthodox"
        ),
        Player(
            id = "spin_mujeeb", name = "Mujeeb Ur Rahman", country = "Afghanistan",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.MYSTERY_SPINNER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 150, form = 80, hype = 79, age = 23, iplMatches = 19,
            primaryStatLabel = "WKTS", primaryStatValue = "19",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.18",
            battingStyle = "RHB", bowlingStyle = "Mystery Offspin"
        ),
        Player(
            id = "spin_adilrashid", name = "Adil Rashid", country = "England",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.WRIST_SPINNER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 200, form = 82, hype = 78, age = 37, iplMatches = 3,
            primaryStatLabel = "T20I WKTS", primaryStatValue = "126",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.38",
            battingStyle = "RHB", bowlingStyle = "Legbreak Googly"
        ),
        Player(
            id = "spin_hosein", name = "Akeal Hosein", country = "West Indies",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.FINGER_SPINNER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 100, form = 81, hype = 78, age = 31, iplMatches = 1,
            primaryStatLabel = "T20I WKTS", primaryStatValue = "60",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.02",
            battingStyle = "LHB", bowlingStyle = "Slow Left-Arm Orthodox"
        ),
        Player(
            id = "spin_maharaj", name = "Keshav Maharaj", country = "South Africa",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.FINGER_SPINNER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 75, form = 80, hype = 76, age = 35, iplMatches = 2,
            primaryStatLabel = "T20I ECON", primaryStatValue = "7.21",
            secondaryStatLabel = "WKTS", secondaryStatValue = "38",
            battingStyle = "RHB", bowlingStyle = "Slow Left-Arm Orthodox"
        ),
        Player(
            id = "spin_markande", name = "Mayank Markande", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.WRIST_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 50, form = 79, hype = 76, age = 27, iplMatches = 37,
            primaryStatLabel = "WKTS", primaryStatValue = "37",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.88",
            battingStyle = "RHB", bowlingStyle = "Legbreak"
        ),
        Player(
            id = "spin_chawla", name = "Piyush Chawla", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.WRIST_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 50, form = 77, hype = 75, age = 36, iplMatches = 192,
            primaryStatLabel = "WKTS", primaryStatValue = "192",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.96",
            battingStyle = "LHB", bowlingStyle = "Legbreak Googly"
        ),
        Player(
            id = "spin_shamsi", name = "Tabraiz Shamsi", country = "South Africa",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.WRIST_SPINNER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 100, form = 77, hype = 73, age = 34, iplMatches = 5,
            primaryStatLabel = "T20I WKTS", primaryStatValue = "89",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.39",
            battingStyle = "RHB", bowlingStyle = "Left-Arm Wrist Spin"
        ),
        Player(
            id = "spin_gopal", name = "Shreyas Gopal", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.WRIST_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 30, form = 75, hype = 72, age = 31, iplMatches = 52,
            primaryStatLabel = "WKTS", primaryStatValue = "52",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.16",
            battingStyle = "RHB", bowlingStyle = "Legbreak Googly"
        ),
        Player(
            id = "spin_jayant", name = "Jayant Yadav", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.FINGER_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 50, form = 72, hype = 69, age = 35, iplMatches = 20,
            primaryStatLabel = "WKTS", primaryStatValue = "8",
            secondaryStatLabel = "ECON", secondaryStatValue = "6.88",
            battingStyle = "RHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "spin_mashwin", name = "Murugan Ashwin", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.WRIST_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 30, form = 71, hype = 68, age = 34, iplMatches = 42,
            primaryStatLabel = "WKTS", primaryStatValue = "35",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.01",
            battingStyle = "RHB", bowlingStyle = "Legbreak"
        ),
        Player(
            id = "spin_mishra", name = "Amit Mishra", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.WRIST_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_SPINNERS,
            basePriceLakhs = 50, form = 70, hype = 69, age = 42, iplMatches = 162,
            primaryStatLabel = "WKTS", primaryStatValue = "174",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.38",
            battingStyle = "RHB", bowlingStyle = "Legbreak"
        )
    )
}
