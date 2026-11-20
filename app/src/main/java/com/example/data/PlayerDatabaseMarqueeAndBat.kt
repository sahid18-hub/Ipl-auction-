package com.example.data

import com.example.model.AuctionSet
import com.example.model.Nationality
import com.example.model.Player
import com.example.model.PlayerArchetype
import com.example.model.PlayerRole
import com.example.model.PlayerStatus

internal object PlayerDatabaseMarqueeAndBat {

    val marquee1Set: List<Player> = listOf(
        Player(
            id = "m1_kohli", name = "Virat Kohli", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.TOP_ORDER_ANCHOR,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_1,
            basePriceLakhs = 200, form = 96, hype = 99, age = 36, iplMatches = 252,
            primaryStatLabel = "RUNS", primaryStatValue = "8004",
            secondaryStatLabel = "SR", secondaryStatValue = "132.0",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med", isCaptainMaterial = true
        ),
        Player(
            id = "m1_bumrah", name = "Jasprit Bumrah", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.DEATH_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_1,
            basePriceLakhs = 200, form = 99, hype = 99, age = 31, iplMatches = 133,
            primaryStatLabel = "WKTS", primaryStatValue = "165",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.30",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast", isCaptainMaterial = true
        ),
        Player(
            id = "m1_rohit", name = "Rohit Sharma", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.POWERPLAY_OPENER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_1,
            basePriceLakhs = 200, form = 91, hype = 98, age = 37, iplMatches = 257,
            primaryStatLabel = "RUNS", primaryStatValue = "6628",
            secondaryStatLabel = "SR", secondaryStatValue = "131.1",
            battingStyle = "RHB", bowlingStyle = "Offbreak", isCaptainMaterial = true
        ),
        Player(
            id = "m1_hardik", name = "Hardik Pandya", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_1,
            basePriceLakhs = 200, form = 93, hype = 97, age = 31, iplMatches = 137,
            primaryStatLabel = "RUNS", primaryStatValue = "2525",
            secondaryStatLabel = "WKTS", secondaryStatValue = "64",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast-Med", isCaptainMaterial = true
        ),
        Player(
            id = "m1_suryakumar", name = "Suryakumar Yadav", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.MIDDLE_ORDER_ENFORCER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_1,
            basePriceLakhs = 200, form = 96, hype = 97, age = 34, iplMatches = 150,
            primaryStatLabel = "RUNS", primaryStatValue = "3594",
            secondaryStatLabel = "SR", secondaryStatValue = "145.3",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med", isCaptainMaterial = true
        ),
        Player(
            id = "m1_rashid", name = "Rashid Khan", country = "Afghanistan",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.WRIST_SPINNER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_1,
            basePriceLakhs = 200, form = 95, hype = 97, age = 26, iplMatches = 121,
            primaryStatLabel = "WKTS", primaryStatValue = "149",
            secondaryStatLabel = "ECON", secondaryStatValue = "6.82",
            battingStyle = "RHB", bowlingStyle = "Legbreak Googly", isCaptainMaterial = true
        ),
        Player(
            id = "m1_klaasen", name = "Heinrich Klaasen", country = "South Africa",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_1,
            basePriceLakhs = 200, form = 96, hype = 96, age = 33, iplMatches = 35,
            primaryStatLabel = "RUNS", primaryStatValue = "993",
            secondaryStatLabel = "SR", secondaryStatValue = "168.3",
            battingStyle = "RHB", bowlingStyle = "Wicketkeeper", isCaptainMaterial = false
        ),
        Player(
            id = "m1_cummins", name = "Pat Cummins", country = "Australia",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.EXPRESS_PACER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_1,
            basePriceLakhs = 200, form = 93, hype = 95, age = 31, iplMatches = 58,
            primaryStatLabel = "WKTS", primaryStatValue = "63",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.75",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast", isCaptainMaterial = true
        ),
        Player(
            id = "m1_pant", name = "Rishabh Pant", country = "India",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_1,
            basePriceLakhs = 200, form = 94, hype = 98, age = 27, iplMatches = 111,
            primaryStatLabel = "RUNS", primaryStatValue = "3284",
            secondaryStatLabel = "SR", secondaryStatValue = "148.9",
            battingStyle = "LHB", bowlingStyle = "Wicketkeeper", isCaptainMaterial = true
        ),
        Player(
            id = "m1_klrahul", name = "KL Rahul", country = "India",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.ANCHOR_WK,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_1,
            basePriceLakhs = 200, form = 91, hype = 95, age = 32, iplMatches = 132,
            primaryStatLabel = "RUNS", primaryStatValue = "4683",
            secondaryStatLabel = "SR", secondaryStatValue = "134.6",
            battingStyle = "RHB", bowlingStyle = "Wicketkeeper", isCaptainMaterial = true
        ),
        Player(
            id = "m1_shreyas", name = "Shreyas Iyer", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.MIDDLE_ORDER_ENFORCER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_1,
            basePriceLakhs = 200, form = 93, hype = 96, age = 30, iplMatches = 115,
            primaryStatLabel = "RUNS", primaryStatValue = "3127",
            secondaryStatLabel = "SR", secondaryStatValue = "127.5",
            battingStyle = "RHB", bowlingStyle = "Legbreak", isCaptainMaterial = true
        ),
        Player(
            id = "m1_samson", name = "Sanju Samson", country = "India",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_1,
            basePriceLakhs = 200, form = 93, hype = 94, age = 30, iplMatches = 168,
            primaryStatLabel = "RUNS", primaryStatValue = "4419",
            secondaryStatLabel = "SR", secondaryStatValue = "139.0",
            battingStyle = "RHB", bowlingStyle = "Wicketkeeper", isCaptainMaterial = true
        )
    )

    val marquee2Set: List<Player> = listOf(
        Player(
            id = "m2_buttler", name = "Jos Buttler", country = "England",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_2,
            basePriceLakhs = 200, form = 94, hype = 95, age = 34, iplMatches = 107,
            primaryStatLabel = "RUNS", primaryStatValue = "3582",
            secondaryStatLabel = "SR", secondaryStatValue = "147.5",
            battingStyle = "RHB", bowlingStyle = "Wicketkeeper", isCaptainMaterial = true
        ),
        Player(
            id = "m2_head", name = "Travis Head", country = "Australia",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.POWERPLAY_OPENER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_2,
            basePriceLakhs = 200, form = 96, hype = 95, age = 31, iplMatches = 25,
            primaryStatLabel = "RUNS", primaryStatValue = "772",
            secondaryStatLabel = "SR", secondaryStatValue = "173.9",
            battingStyle = "LHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "m2_pooran", name = "Nicholas Pooran", country = "West Indies",
            role = PlayerRole.WICKETKEEPER, archetype = PlayerArchetype.EXPLOSIVE_WK,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_2,
            basePriceLakhs = 200, form = 95, hype = 95, age = 29, iplMatches = 76,
            primaryStatLabel = "RUNS", primaryStatValue = "1769",
            secondaryStatLabel = "SR", secondaryStatValue = "162.3",
            battingStyle = "LHB", bowlingStyle = "Wicketkeeper", isCaptainMaterial = true
        ),
        Player(
            id = "m2_russell", name = "Andre Russell", country = "West Indies",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.PACE_ALL_ROUNDER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_2,
            basePriceLakhs = 200, form = 92, hype = 94, age = 36, iplMatches = 127,
            primaryStatLabel = "RUNS", primaryStatValue = "2484",
            secondaryStatLabel = "SR", secondaryStatValue = "174.9",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Fast"
        ),
        Player(
            id = "m2_narine", name = "Sunil Narine", country = "West Indies",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.SPIN_ALL_ROUNDER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_2,
            basePriceLakhs = 200, form = 94, hype = 95, age = 36, iplMatches = 177,
            primaryStatLabel = "WKTS", primaryStatValue = "180",
            secondaryStatLabel = "ECON", secondaryStatValue = "6.73",
            battingStyle = "LHB", bowlingStyle = "Mystery Offspin"
        ),
        Player(
            id = "m2_boult", name = "Trent Boult", country = "New Zealand",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.LEFT_ARM_PACER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_2,
            basePriceLakhs = 200, form = 92, hype = 93, age = 35, iplMatches = 104,
            primaryStatLabel = "WKTS", primaryStatValue = "121",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.29",
            battingStyle = "RHB", bowlingStyle = "Left-Arm Fast-Med"
        ),
        Player(
            id = "m2_starc", name = "Mitchell Starc", country = "Australia",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.LEFT_ARM_PACER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_2,
            basePriceLakhs = 200, form = 92, hype = 94, age = 35, iplMatches = 41,
            primaryStatLabel = "WKTS", primaryStatValue = "51",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.21",
            battingStyle = "LHB", bowlingStyle = "Left-Arm Fast"
        ),
        Player(
            id = "m2_rabada", name = "Kagiso Rabada", country = "South Africa",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.EXPRESS_PACER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_2,
            basePriceLakhs = 200, form = 91, hype = 92, age = 29, iplMatches = 80,
            primaryStatLabel = "WKTS", primaryStatValue = "117",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.48",
            battingStyle = "LHB", bowlingStyle = "Right-Arm Fast"
        ),
        Player(
            id = "m2_arshdeep", name = "Arshdeep Singh", country = "India",
            role = PlayerRole.FAST_BOWLER, archetype = PlayerArchetype.LEFT_ARM_PACER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_2,
            basePriceLakhs = 200, form = 94, hype = 95, age = 26, iplMatches = 65,
            primaryStatLabel = "WKTS", primaryStatValue = "76",
            secondaryStatLabel = "ECON", secondaryStatValue = "9.03",
            battingStyle = "LHB", bowlingStyle = "Left-Arm Fast-Med"
        ),
        Player(
            id = "m2_chahal", name = "Yuzvendra Chahal", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.WRIST_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_2,
            basePriceLakhs = 200, form = 93, hype = 94, age = 34, iplMatches = 160,
            primaryStatLabel = "WKTS", primaryStatValue = "205",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.84",
            battingStyle = "RHB", bowlingStyle = "Legbreak Googly"
        ),
        Player(
            id = "m2_kuldeep", name = "Kuldeep Yadav", country = "India",
            role = PlayerRole.SPINNER, archetype = PlayerArchetype.WRIST_SPINNER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_2,
            basePriceLakhs = 200, form = 93, hype = 93, age = 30, iplMatches = 84,
            primaryStatLabel = "WKTS", primaryStatValue = "87",
            secondaryStatLabel = "ECON", secondaryStatValue = "8.27",
            battingStyle = "LHB", bowlingStyle = "Left-Arm Wrist Spin"
        ),
        Player(
            id = "m2_jadeja", name = "Ravindra Jadeja", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.SPIN_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_2,
            basePriceLakhs = 200, form = 91, hype = 95, age = 36, iplMatches = 240,
            primaryStatLabel = "RUNS", primaryStatValue = "2959",
            secondaryStatLabel = "WKTS", secondaryStatValue = "160",
            battingStyle = "LHB", bowlingStyle = "Slow Left-Arm Orthodox", isCaptainMaterial = true
        ),
        Player(
            id = "m2_axar", name = "Axar Patel", country = "India",
            role = PlayerRole.ALL_ROUNDER, archetype = PlayerArchetype.SPIN_ALL_ROUNDER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_2,
            basePriceLakhs = 200, form = 93, hype = 93, age = 31, iplMatches = 150,
            primaryStatLabel = "WKTS", primaryStatValue = "123",
            secondaryStatLabel = "ECON", secondaryStatValue = "7.28",
            battingStyle = "LHB", bowlingStyle = "Slow Left-Arm Orthodox", isCaptainMaterial = true
        ),
        Player(
            id = "m2_ruturaj", name = "Ruturaj Gaikwad", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.TOP_ORDER_ANCHOR,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.MARQUEE_2,
            basePriceLakhs = 200, form = 92, hype = 93, age = 28, iplMatches = 66,
            primaryStatLabel = "RUNS", primaryStatValue = "2380",
            secondaryStatLabel = "AVG", secondaryStatValue = "41.8",
            battingStyle = "RHB", bowlingStyle = "Offbreak", isCaptainMaterial = true
        )
    )

    val cappedBattersSet: List<Player> = listOf(
        Player(
            id = "bat_jaiswal", name = "Yashasvi Jaiswal", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.POWERPLAY_OPENER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 200, form = 90, hype = 91, age = 23, iplMatches = 52,
            primaryStatLabel = "RUNS", primaryStatValue = "1607",
            secondaryStatLabel = "SR", secondaryStatValue = "150.6",
            battingStyle = "LHB", bowlingStyle = "Legbreak"
        ),
        Player(
            id = "bat_gill", name = "Shubman Gill", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.TOP_ORDER_ANCHOR,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 200, form = 90, hype = 91, age = 25, iplMatches = 103,
            primaryStatLabel = "RUNS", primaryStatValue = "3216",
            secondaryStatLabel = "SR", secondaryStatValue = "135.7",
            battingStyle = "RHB", bowlingStyle = "Offbreak", isCaptainMaterial = true
        ),
        Player(
            id = "bat_abhishek", name = "Abhishek Sharma", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.POWERPLAY_OPENER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 150, form = 89, hype = 90, age = 24, iplMatches = 63,
            primaryStatLabel = "RUNS", primaryStatValue = "1376",
            secondaryStatLabel = "SR", secondaryStatValue = "155.1",
            battingStyle = "LHB", bowlingStyle = "Left-Arm Orthodox"
        ),
        Player(
            id = "bat_tilak", name = "Tilak Varma", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.MIDDLE_ORDER_ENFORCER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 150, form = 89, hype = 88, age = 22, iplMatches = 38,
            primaryStatLabel = "RUNS", primaryStatValue = "1156",
            secondaryStatLabel = "SR", secondaryStatValue = "146.3",
            battingStyle = "LHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "bat_rinku", name = "Rinku Singh", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.FINISHER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 150, form = 88, hype = 90, age = 27, iplMatches = 45,
            primaryStatLabel = "RUNS", primaryStatValue = "893",
            secondaryStatLabel = "SR", secondaryStatValue = "143.3",
            battingStyle = "LHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "bat_parag", name = "Riyan Parag", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.MIDDLE_ORDER_ENFORCER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 150, form = 87, hype = 87, age = 23, iplMatches = 70,
            primaryStatLabel = "RUNS", primaryStatValue = "1173",
            secondaryStatLabel = "SR", secondaryStatValue = "135.2",
            battingStyle = "RHB", bowlingStyle = "Legbreak"
        ),
        Player(
            id = "bat_sudharsan", name = "Sai Sudharsan", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.TOP_ORDER_ANCHOR,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 100, form = 88, hype = 86, age = 23, iplMatches = 25,
            primaryStatLabel = "RUNS", primaryStatValue = "1034",
            secondaryStatLabel = "AVG", secondaryStatValue = "47.0",
            battingStyle = "LHB", bowlingStyle = "Legbreak"
        ),
        Player(
            id = "bat_patidar", name = "Rajat Patidar", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.MIDDLE_ORDER_ENFORCER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 150, form = 88, hype = 87, age = 31, iplMatches = 27,
            primaryStatLabel = "RUNS", primaryStatValue = "799",
            secondaryStatLabel = "SR", secondaryStatValue = "158.8",
            battingStyle = "RHB", bowlingStyle = "Offbreak", isCaptainMaterial = true
        ),
        Player(
            id = "bat_dube", name = "Shivam Dube", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.MIDDLE_ORDER_ENFORCER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 150, form = 87, hype = 87, age = 31, iplMatches = 65,
            primaryStatLabel = "RUNS", primaryStatValue = "1502",
            secondaryStatLabel = "SR", secondaryStatValue = "146.7",
            battingStyle = "LHB", bowlingStyle = "Right-Arm Med"
        ),
        Player(
            id = "bat_salt", name = "Phil Salt", country = "England",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.POWERPLAY_OPENER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 200, form = 90, hype = 89, age = 28, iplMatches = 21,
            primaryStatLabel = "RUNS", primaryStatValue = "653",
            secondaryStatLabel = "SR", secondaryStatValue = "175.5",
            battingStyle = "RHB", bowlingStyle = "Part-time WK"
        ),
        Player(
            id = "bat_stubbs", name = "Tristan Stubbs", country = "South Africa",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.FINISHER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 150, form = 88, hype = 87, age = 24, iplMatches = 18,
            primaryStatLabel = "RUNS", primaryStatValue = "405",
            secondaryStatLabel = "SR", secondaryStatValue = "174.6",
            battingStyle = "RHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "bat_jfm", name = "Jake Fraser-McGurk", country = "Australia",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.POWERPLAY_OPENER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 150, form = 85, hype = 88, age = 22, iplMatches = 9,
            primaryStatLabel = "RUNS", primaryStatValue = "330",
            secondaryStatLabel = "SR", secondaryStatValue = "234.0",
            battingStyle = "RHB", bowlingStyle = "Legbreak"
        ),
        Player(
            id = "bat_miller", name = "David Miller", country = "South Africa",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.FINISHER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 150, form = 86, hype = 85, age = 35, iplMatches = 130,
            primaryStatLabel = "RUNS", primaryStatValue = "2924",
            secondaryStatLabel = "SR", secondaryStatValue = "139.2",
            battingStyle = "LHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "bat_faf", name = "Faf du Plessis", country = "South Africa",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.POWERPLAY_OPENER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 200, form = 84, hype = 84, age = 40, iplMatches = 145,
            primaryStatLabel = "RUNS", primaryStatValue = "4571",
            secondaryStatLabel = "SR", secondaryStatValue = "136.4",
            battingStyle = "RHB", bowlingStyle = "Legbreak", isCaptainMaterial = true
        ),
        Player(
            id = "bat_hetmyer", name = "Shimron Hetmyer", country = "West Indies",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.FINISHER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 150, form = 85, hype = 84, age = 28, iplMatches = 72,
            primaryStatLabel = "RUNS", primaryStatValue = "1243",
            secondaryStatLabel = "SR", secondaryStatValue = "153.1",
            battingStyle = "LHB", bowlingStyle = "Right-Arm Med"
        ),
        Player(
            id = "bat_conway", name = "Devon Conway", country = "New Zealand",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.TOP_ORDER_ANCHOR,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 200, form = 85, hype = 83, age = 33, iplMatches = 23,
            primaryStatLabel = "RUNS", primaryStatValue = "924",
            secondaryStatLabel = "AVG", secondaryStatValue = "48.6",
            battingStyle = "LHB", bowlingStyle = "Right-Arm Med"
        ),
        Player(
            id = "bat_willjacks", name = "Will Jacks", country = "England",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.POWERPLAY_OPENER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 200, form = 85, hype = 85, age = 26, iplMatches = 8,
            primaryStatLabel = "RUNS", primaryStatValue = "230",
            secondaryStatLabel = "SR", secondaryStatValue = "175.6",
            battingStyle = "RHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "bat_phillips", name = "Glenn Phillips", country = "New Zealand",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.FINISHER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 150, form = 85, hype = 83, age = 28, iplMatches = 8,
            primaryStatLabel = "T20I SR", primaryStatValue = "141.8",
            secondaryStatLabel = "RUNS", secondaryStatValue = "1929",
            battingStyle = "RHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "bat_brook", name = "Harry Brook", country = "England",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.MIDDLE_ORDER_ENFORCER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 200, form = 84, hype = 84, age = 26, iplMatches = 11,
            primaryStatLabel = "RUNS", primaryStatValue = "190",
            secondaryStatLabel = "SR", secondaryStatValue = "123.4",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med"
        ),
        Player(
            id = "bat_markram", name = "Aiden Markram", country = "South Africa",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.TOP_ORDER_ANCHOR,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 200, form = 83, hype = 81, age = 30, iplMatches = 44,
            primaryStatLabel = "RUNS", primaryStatValue = "995",
            secondaryStatLabel = "SR", secondaryStatValue = "129.7",
            battingStyle = "RHB", bowlingStyle = "Offbreak", isCaptainMaterial = true
        ),
        Player(
            id = "bat_nitishrana", name = "Nitish Rana", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.MIDDLE_ORDER_ENFORCER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 150, form = 82, hype = 81, age = 31, iplMatches = 107,
            primaryStatLabel = "RUNS", primaryStatValue = "2636",
            secondaryStatLabel = "SR", secondaryStatValue = "135.0",
            battingStyle = "LHB", bowlingStyle = "Offbreak", isCaptainMaterial = true
        ),
        Player(
            id = "bat_timdavid", name = "Tim David", country = "Australia",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.FINISHER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 150, form = 82, hype = 82, age = 28, iplMatches = 38,
            primaryStatLabel = "RUNS", primaryStatValue = "659",
            secondaryStatLabel = "SR", secondaryStatValue = "170.3",
            battingStyle = "RHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "bat_powell", name = "Rovman Powell", country = "West Indies",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.FINISHER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 150, form = 80, hype = 80, age = 31, iplMatches = 27,
            primaryStatLabel = "RUNS", primaryStatValue = "360",
            secondaryStatLabel = "SR", secondaryStatValue = "147.5",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med", isCaptainMaterial = true
        ),
        Player(
            id = "bat_tripathi", name = "Rahul Tripathi", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.MIDDLE_ORDER_ENFORCER,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 75, form = 81, hype = 79, age = 33, iplMatches = 95,
            primaryStatLabel = "RUNS", primaryStatValue = "2236",
            secondaryStatLabel = "SR", secondaryStatValue = "139.3",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med"
        ),
        Player(
            id = "bat_rutherford", name = "Sherfane Rutherford", country = "West Indies",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.FINISHER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 150, form = 81, hype = 79, age = 26, iplMatches = 10,
            primaryStatLabel = "T20I SR", primaryStatValue = "144.6",
            secondaryStatLabel = "RUNS", secondaryStatValue = "106",
            battingStyle = "LHB", bowlingStyle = "Right-Arm Med"
        ),
        Player(
            id = "bat_padikkal", name = "Devdutt Padikkal", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.TOP_ORDER_ANCHOR,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 150, form = 77, hype = 76, age = 24, iplMatches = 64,
            primaryStatLabel = "RUNS", primaryStatValue = "1559",
            secondaryStatLabel = "SR", secondaryStatValue = "123.5",
            battingStyle = "LHB", bowlingStyle = "Offbreak"
        ),
        Player(
            id = "bat_rahane", name = "Ajinkya Rahane", country = "India",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.TOP_ORDER_ANCHOR,
            nationality = Nationality.INDIAN, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 150, form = 78, hype = 76, age = 36, iplMatches = 185,
            primaryStatLabel = "RUNS", primaryStatValue = "4642",
            secondaryStatLabel = "SR", secondaryStatValue = "123.4",
            battingStyle = "RHB", bowlingStyle = "Right-Arm Med", isCaptainMaterial = true
        ),
        Player(
            id = "bat_brevis", name = "Dewald Brevis", country = "South Africa",
            role = PlayerRole.BATTER, archetype = PlayerArchetype.MIDDLE_ORDER_ENFORCER,
            nationality = Nationality.OVERSEAS, status = PlayerStatus.CAPPED, set = AuctionSet.CAPPED_BATTERS,
            basePriceLakhs = 75, form = 77, hype = 78, age = 21, iplMatches = 10,
            primaryStatLabel = "RUNS", primaryStatValue = "230",
            secondaryStatLabel = "SR", secondaryStatValue = "133.7",
            battingStyle = "RHB", bowlingStyle = "Legbreak"
        )
    )
}
