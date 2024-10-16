package ru.skypaws.features.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.skypaws.domain.usecase.userPayInfo.GetPayInfoUseCase

abstract class UserPayInfoViewModel (
    private val getPayInfoUseCase: GetPayInfoUseCase,
) : ViewModel() {
    /**
     * Calls function [UserPayInfoRepositoryImpl.getPayInfo()][ru.skypaws.data.repository.UserPayInfoRepositoryImpl.getPayInfo]
     * to get user [PayInfo][ru.skypaws.data.source.dto.PayInfoDto] from server
     */
    fun getPayInfo() {
        viewModelScope.launch {
            getPayInfoUseCase
        }
    }
}
