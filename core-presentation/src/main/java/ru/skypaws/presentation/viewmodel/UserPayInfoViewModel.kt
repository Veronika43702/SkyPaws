package ru.skypaws.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.skypaws.mobileapp.domain.usecase.userPayInfo.GetPayInfoUseCase

abstract class UserPayInfoViewModel (
    private val getPayInfoUseCase: GetPayInfoUseCase,
) : ViewModel() {
    /**
     * Gets user [PayInfo][ru.skypaws.mobileapp.model.dto.PayInfoDto] from server by
     * [UserPayInfoRepository.getPayInfo()][ru.skypaws.mobileapp.repository.user.UserPayInfoRepository.fetchPayInfoAndSave]
     */
    fun getPayInfo() {
        viewModelScope.launch {
            getPayInfoUseCase()
        }
    }
}
