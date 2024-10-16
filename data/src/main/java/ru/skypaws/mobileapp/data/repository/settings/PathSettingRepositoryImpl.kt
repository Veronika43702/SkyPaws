package ru.skypaws.mobileapp.data.repository.settings

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.skypaws.mobileapp.data.datasource.local.settings.SettingGetLocalDataSource
import ru.skypaws.mobileapp.data.datasource.local.settings.SettingLocalDataSource
import ru.skypaws.mobileapp.data.di.storage.PathSetting
import ru.skypaws.mobileapp.data.di.utils.DispatcherIO
import ru.skypaws.mobileapp.domain.repository.settings.PathSettingRepository
import javax.inject.Inject

class PathSettingRepositoryImpl @Inject constructor(
    @PathSetting private val pathSettingLocalDataSource: SettingLocalDataSource<String?>,
    @PathSetting private val pathSettingGetLocalDataSource: SettingGetLocalDataSource<String?>,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
) : PathSettingRepository {
    /**
     * Gets path flow from local storage.
     * @return  Flow<[String]?>
     */
    override val path: Flow<String?> = pathSettingLocalDataSource.dataFlow

    /**
     * Saves [uri] (path) in local storage.
     */
    override suspend fun savePath(uri: String) = withContext(dispatcherIO){
        pathSettingLocalDataSource.save(uri)
    }

    override suspend fun getPath(): String? = withContext(dispatcherIO){
        pathSettingGetLocalDataSource.get()
    }
}
