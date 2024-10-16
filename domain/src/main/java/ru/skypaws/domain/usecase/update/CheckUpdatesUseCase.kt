package ru.skypaws.domain.usecase.update

import ru.skypaws.domain.model.Updates
import ru.skypaws.domain.repository.UpdateRepository
import javax.inject.Inject

class CheckUpdatesUseCase @Inject constructor(
  private val repository: UpdateRepository
){
    suspend operator fun invoke(): Updates {
        return repository.checkUpdate()
    }
}