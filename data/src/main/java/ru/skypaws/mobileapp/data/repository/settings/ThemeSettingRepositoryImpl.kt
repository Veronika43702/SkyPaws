package ru.skypaws.mobileapp.data.repository.settings

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.skypaws.mobileapp.data.datasource.local.settings.SettingLocalDataSource
import ru.skypaws.mobileapp.data.di.storage.ThemeSetting
import ru.skypaws.mobileapp.data.di.utils.DispatcherIO
import ru.skypaws.mobileapp.domain.repository.settings.ThemeSettingRepository
import javax.inject.Inject

class ThemeSettingRepositoryImpl @Inject constructor(
    @ThemeSetting private val themeSettingLocalDataSource: SettingLocalDataSource<Int>,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
) : ThemeSettingRepository {
    /**
     * Gets theme flow from local storage.
     * @return  Flow<[Int]>
     */
    override val theme: Flow<Int> = themeSettingLocalDataSource.dataFlow

    /**
     * Saves [newTheme] in local storage.
     */
    override suspend fun saveTheme(newTheme: Int) =
        withContext(dispatcherIO) {
            themeSettingLocalDataSource.save(newTheme)
        }
}
