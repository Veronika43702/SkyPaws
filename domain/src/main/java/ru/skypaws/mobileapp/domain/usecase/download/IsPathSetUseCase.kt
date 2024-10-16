package ru.skypaws.mobileapp.domain.usecase.download

import ru.skypaws.mobileapp.domain.repository.DownloadLogbookRepository
import javax.inject.Inject

class IsPathSetUseCase @Inject constructor(
    private val repository: DownloadLogbookRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.isPathSet()
    }
}