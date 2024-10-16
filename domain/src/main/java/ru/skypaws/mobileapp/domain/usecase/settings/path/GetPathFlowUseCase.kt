package ru.skypaws.mobileapp.domain.usecase.settings.path

import kotlinx.coroutines.flow.Flow
import ru.skypaws.mobileapp.domain.repository.settings.PathSettingRepository
import javax.inject.Inject

class GetPathFlowUseCase @Inject constructor(
    private val repository: PathSettingRepository
){
    operator fun invoke(): Flow<String?> = repository.path

}