package ru.skypaws.domain.usecase.download

import ru.skypaws.domain.repository.DownloadLogbookRepository
import javax.inject.Inject

class SaveLogbookToChosenPathUseCase @Inject constructor(
    private val repository: DownloadLogbookRepository
) {
    suspend operator fun invoke(filename: String) {
        return repository.saveLogbookToChosenPath(filename)
    }
}