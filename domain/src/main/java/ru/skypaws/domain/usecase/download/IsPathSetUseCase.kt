package ru.skypaws.domain.usecase.download

import ru.skypaws.domain.repository.DownloadLogbookRepository
import javax.inject.Inject

class IsPathSetUseCase @Inject constructor(
    private val repository: DownloadLogbookRepository
) {
    operator fun invoke(): Boolean {
        return repository.isPathSet()
    }
}