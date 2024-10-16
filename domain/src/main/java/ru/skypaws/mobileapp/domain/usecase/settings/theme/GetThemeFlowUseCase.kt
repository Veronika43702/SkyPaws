package ru.skypaws.mobileapp.domain.usecase.settings.theme

import kotlinx.coroutines.flow.Flow
import ru.skypaws.mobileapp.domain.repository.settings.ThemeSettingRepository
import javax.inject.Inject

class GetThemeFlowUseCase @Inject constructor(
    private val repository: ThemeSettingRepository
){
    operator fun invoke(): Flow<Int> = repository.theme
}