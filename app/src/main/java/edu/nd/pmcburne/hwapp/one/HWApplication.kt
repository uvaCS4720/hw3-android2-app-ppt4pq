package edu.nd.pmcburne.hwapp.one

import android.app.Application
import edu.nd.pmcburne.hwapp.one.data.api.RetrofitClient
import edu.nd.pmcburne.hwapp.one.data.db.AppDatabase
import edu.nd.pmcburne.hwapp.one.data.repository.GameRepository

class HWApplication : Application() {

    val repository: GameRepository by lazy {
        val db = AppDatabase.getInstance(this)
        GameRepository(
            apiService = RetrofitClient.apiService,
            gameDao = db.gameDao(),
            context = this
        )
    }
}
