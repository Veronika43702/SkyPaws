package ru.skypaws.mobileapp.domain.usecase.update

import ru.skypaws.mobileapp.domain.model.Updates
import ru.skypaws.mobileapp.domain.repository.UpdateRepository
import javax.inject.Inject

class CheckUpdatesUseCase @Inject constructor(
  private val repository: UpdateRepository
){
    suspend operator fun invoke(): Updates {
        return repository.fetchLatestVersionInfo()
    }
}