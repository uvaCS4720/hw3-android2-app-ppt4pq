package edu.nd.pmcburne.hwapp.one.data.db

import androidx.room.Entity

@Entity(tableName = "games", primaryKeys = ["gameId", "gender"])
data class GameEntity(
    val gameId: String,
    val gender: String,           // "men" or "women"
    val date: String,             // "yyyy/MM/dd"
    val awayTeamName: String,
    val homeTeamName: String,
    val awayScore: String,
    val homeScore: String,
    val awayWinner: Boolean,
    val homeWinner: Boolean,
    val awayRank: String,
    val homeRank: String,
    val awaySeed: String,
    val homeSeed: String,
    val gameState: String,        // "pre", "live", "final"
    val currentPeriod: String,
    val contestClock: String,
    val startTime: String,
    val finalMessage: String,
    val network: String,
    val awayConference: String,
    val homeConference: String
)
