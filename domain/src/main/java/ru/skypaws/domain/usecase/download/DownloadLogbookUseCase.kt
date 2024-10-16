package ru.skypaws.domain.usecase.download

import ru.skypaws.domain.repository.DownloadLogbookRepository
import javax.inject.Inject

class DownloadLogbookUseCase @Inject constructor(
    private val repository: DownloadLogbookRepository
) {
    suspend operator fun invoke(): String {
        return repository.downloadLogbook()
    }
}