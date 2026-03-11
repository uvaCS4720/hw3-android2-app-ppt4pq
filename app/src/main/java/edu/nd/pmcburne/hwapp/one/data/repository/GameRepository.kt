package edu.nd.pmcburne.hwapp.one.data.repository

import android.content.Context
import edu.nd.pmcburne.hwapp.one.data.api.NcaaApiService
import edu.nd.pmcburne.hwapp.one.data.api.models.Game
import edu.nd.pmcburne.hwapp.one.data.db.GameDao
import edu.nd.pmcburne.hwapp.one.data.db.GameEntity
import edu.nd.pmcburne.hwapp.one.util.NetworkUtils
import kotlinx.coroutines.flow.Flow

class GameRepository(
    private val apiService: NcaaApiService,
    private val gameDao: GameDao,
    private val context: Context
) {

    fun getGames(date: String, gender: String): Flow<List<GameEntity>> {
        return gameDao.getGamesByDateAndGender(date, gender)
    }

    suspend fun refreshGames(date: String, gender: String): Result<Unit> {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            return Result.failure(Exception("No internet connection"))
        }
        return try {
            val parts = date.split("/")
            val year = parts[0]
            val month = parts[1]
            val day = parts[2]

            val response = apiService.getScoreboard(gender, year, month, day)
            val entities = response.games.map { wrapper ->
                wrapper.game.toEntity(gender, date)
            }
            gameDao.insertGames(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun Game.toEntity(gender: String, date: String): GameEntity {
        return GameEntity(
            gameId = gameID,
            gender = gender,
            date = date,
            awayTeamName = away.names.short_,
            homeTeamName = home.names.short_,
            awayScore = away.score,
            homeScore = home.score,
            awayWinner = away.winner,
            homeWinner = home.winner,
            awayRank = away.rank,
            homeRank = home.rank,
            awaySeed = away.seed,
            homeSeed = home.seed,
            gameState = gameState,
            currentPeriod = currentPeriod,
            contestClock = contestClock,
            startTime = startTime,
            finalMessage = finalMessage,
            network = network,
            awayConference = away.conferences.firstOrNull()?.conferenceSeo ?: "",
            homeConference = home.conferences.firstOrNull()?.conferenceSeo ?: ""
        )
    }
}
