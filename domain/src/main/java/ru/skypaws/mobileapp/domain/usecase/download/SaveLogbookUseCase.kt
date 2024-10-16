package ru.skypaws.mobileapp.domain.usecase.download

import ru.skypaws.mobileapp.domain.repository.DownloadLogbookRepository
import javax.inject.Inject

class SaveLogbookUseCase @Inject constructor(
    private val repository: DownloadLogbookRepository
) {
    suspend operator fun invoke(uriString: String, filename: String) {
        return repository.saveLogbook(uriString, filename)
    }
}