package ru.skypaws.mobileapp.domain.usecase.download

import ru.skypaws.mobileapp.domain.repository.DownloadLogbookRepository
import javax.inject.Inject

class SaveLogbookToChosenPathUseCase @Inject constructor(
    private val repository: DownloadLogbookRepository
) {
    suspend operator fun invoke(filename: String) {
        return repository.saveLogbookToChosenPath(filename)
    }
}