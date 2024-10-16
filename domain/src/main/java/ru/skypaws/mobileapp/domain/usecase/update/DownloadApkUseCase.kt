package ru.skypaws.mobileapp.domain.usecase.update

import kotlinx.coroutines.flow.Flow
import ru.skypaws.mobileapp.domain.repository.UpdateRepository
import javax.inject.Inject

class DownloadApkUseCase @Inject constructor(
    private val repository: UpdateRepository
) {
    suspend operator fun invoke(): Flow<Float> {
        return repository.downloadApk()
    }
}