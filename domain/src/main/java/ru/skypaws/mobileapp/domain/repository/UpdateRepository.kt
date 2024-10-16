package ru.skypaws.mobileapp.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.skypaws.mobileapp.domain.model.Updates

interface UpdateRepository {
    /**
     * Fetches data of latest app and database version kept on server.
     * @return [Updates]
     */
    suspend fun fetchLatestVersionInfo(): Updates

    /**
     * Downloads apk file with new version from server and returns Flow of download progress (0..100).
     * @return [Flow]<[Float]>
     */
    suspend fun downloadApk(): Flow<Float>
}
