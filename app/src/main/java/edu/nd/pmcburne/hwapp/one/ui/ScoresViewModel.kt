package edu.nd.pmcburne.hwapp.one.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.nd.pmcburne.hwapp.one.HWApplication
import edu.nd.pmcburne.hwapp.one.data.db.GameEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

enum class Gender(val apiValue: String, val displayName: String) {
    MEN("men", "MEN"),
    WOMEN("women", "WOMEN")
}

data class ScoresUiState(
    val games: List<GameEntity> = emptyList(),
    val isLoading: Boolean = false,
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedGender: Gender = Gender.MEN,
    val errorMessage: String? = null,
    val isOffline: Boolean = false
)

class ScoresViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as HWApplication).repository

    private val _uiState = MutableStateFlow(ScoresUiState())
    val uiState: StateFlow<ScoresUiState> = _uiState.asStateFlow()

    private var observeJob: Job? = null

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

    init {
        observeAndRefresh()
    }

    fun onDateChanged(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = date, errorMessage = null)
        observeAndRefresh()
    }

    fun onGenderChanged(gender: Gender) {
        _uiState.value = _uiState.value.copy(selectedGender = gender, errorMessage = null)
        observeAndRefresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val state = _uiState.value
            val dateStr = state.selectedDate.format(dateFormatter)
            val result = repository.refreshGames(dateStr, state.selectedGender.apiValue)
            result.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isOffline = e.message?.contains("No internet") == true,
                    errorMessage = e.message ?: "Failed to load scores"
                )
            }
            result.onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false, isOffline = false)
            }
        }
    }

    private fun observeAndRefresh() {
        observeJob?.cancel()
        val state = _uiState.value
        val dateStr = state.selectedDate.format(dateFormatter)
        observeJob = viewModelScope.launch {
            repository.getGames(dateStr, state.selectedGender.apiValue)
                .collectLatest { games ->
                    _uiState.value = _uiState.value.copy(games = games)
                }
        }
        refresh()
    }
}
