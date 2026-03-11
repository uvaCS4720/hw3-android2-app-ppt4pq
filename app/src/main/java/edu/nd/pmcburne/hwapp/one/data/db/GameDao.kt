package edu.nd.pmcburne.hwapp.one.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<GameEntity>)

    @Query("SELECT * FROM games WHERE date = :date AND gender = :gender")
    fun getGamesByDateAndGender(date: String, gender: String): Flow<List<GameEntity>>
}
