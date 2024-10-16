package ru.skypaws.domain.usecase.logbook

import ru.skypaws.domain.repository.LogbookRepository
import javax.inject.Inject

class DeleteLogbookUseCase @Inject constructor(
    private val repository: LogbookRepository
) {
    suspend operator fun invoke() {
        repository.delete()
    }
}