package ru.skypaws.domain.usecase.update

import kotlinx.coroutines.flow.Flow
import ru.skypaws.domain.repository.UpdateRepository
import javax.inject.Inject

class DownloadApkUseCase @Inject constructor(
    private val repository: UpdateRepository
) {
    suspend operator fun invoke(): Flow<Float> {
        return repository.downloadApk()
    }
}