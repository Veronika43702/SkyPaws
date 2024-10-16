package ru.skypaws.mobileapp.domain.usecase.settings.path

import ru.skypaws.mobileapp.domain.repository.settings.PathSettingRepository
import javax.inject.Inject

class SavePathUseCase @Inject constructor(
    private val repository: PathSettingRepository
) {
    suspend operator fun invoke(uri: String) {
        repository.savePath(uri)
    }
}