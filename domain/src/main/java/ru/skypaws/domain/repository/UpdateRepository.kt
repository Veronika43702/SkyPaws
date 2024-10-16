package ru.skypaws.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.skypaws.domain.model.Updates

interface UpdateRepository {
    suspend fun checkUpdate(): Updates
    suspend fun downloadApk(): Flow<Float>
}
