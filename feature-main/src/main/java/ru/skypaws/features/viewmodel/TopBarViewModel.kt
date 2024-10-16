package ru.skypaws.features.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.skypaws.mobileapp.domain.usecase.userPayInfo.GetLogbookExpDateUseCase
import ru.skypaws.features.model.TopBarState
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class TopBarViewModel @Inject constructor(
    private val getLogbookExpDateUseCase: GetLogbookExpDateUseCase,
) : ViewModel() {

    private val _topBarState = MutableStateFlow(ru.skypaws.features.model.TopBarState())
    val topBarState: StateFlow<ru.skypaws.features.model.TopBarState> = _topBarState.asStateFlow()

    /**
     * Gets user logbook payment data from local storage by
     * [UserPayInfoRepository.getLogbookExpDate()]
     * [ru.skypaws.mobileapp.repository.user.UserPayInfoRepository.getLogbookExpDate] and updates
     * [TopBarState()][_topBarState].
     *
     * Checks whether date is after current time.
     */
    fun logbookPaidStatus() {
        viewModelScope.launch {
            val logbookExpirationDate = getLogbookExpDateUseCase()
            val timeNow = Instant.now().epochSecond

            _topBarState.update { it.copy(isLogbookPaid = logbookExpirationDate >= timeNow) }
        }

    }
}

