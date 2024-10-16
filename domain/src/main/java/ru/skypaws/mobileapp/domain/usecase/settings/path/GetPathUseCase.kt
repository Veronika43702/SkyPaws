package ru.skypaws.mobileapp.domain.usecase.settings.path

import ru.skypaws.mobileapp.domain.repository.settings.PathSettingRepository
import javax.inject.Inject

class GetPathUseCase @Inject constructor(
    private val repository: PathSettingRepository
){
    suspend operator fun invoke(): String? = repository.getPath()
}