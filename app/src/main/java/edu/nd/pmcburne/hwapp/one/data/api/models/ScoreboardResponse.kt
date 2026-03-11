package edu.nd.pmcburne.hwapp.one.data.api.models

import com.google.gson.annotations.SerializedName

data class ScoreboardResponse(
    @SerializedName("games") val games: List<GameWrapper> = emptyList()
)

data class GameWrapper(
    @SerializedName("game") val game: Game
)

data class Game(
    @SerializedName("gameID") val gameID: String,
    @SerializedName("away") val away: Team,
    @SerializedName("home") val home: Team,
    @SerializedName("gameState") val gameState: String,       // "pre", "live", "final"
    @SerializedName("currentPeriod") val currentPeriod: String,
    @SerializedName("contestClock") val contestClock: String,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("startTimeEpoch") val startTimeEpoch: String,
    @SerializedName("finalMessage") val finalMessage: String,
    @SerializedName("network") val network: String,
    @SerializedName("title") val title: String
)

data class Team(
    @SerializedName("score") val score: String,
    @SerializedName("names") val names: TeamNames,
    @SerializedName("winner") val winner: Boolean,
    @SerializedName("seed") val seed: String,
    @SerializedName("rank") val rank: String,
    @SerializedName("description") val description: String,
    @SerializedName("conferences") val conferences: List<Conference> = emptyList()
)

data class TeamNames(
    @SerializedName("char6") val char6: String,
    @SerializedName("short") val short_: String,
    @SerializedName("seo") val seo: String,
    @SerializedName("full") val full: String
)

data class Conference(
    @SerializedName("conferenceName") val conferenceName: String,
    @SerializedName("conferenceSeo") val conferenceSeo: String
)
