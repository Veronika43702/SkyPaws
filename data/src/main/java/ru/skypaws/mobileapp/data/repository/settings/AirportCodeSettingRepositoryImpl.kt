package ru.skypaws.mobileapp.data.repository.settings

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.skypaws.mobileapp.data.datasource.local.settings.AirportCodeSettingLocalDataSource
import ru.skypaws.mobileapp.data.datasource.local.settings.SettingGetLocalDataSource
import ru.skypaws.mobileapp.data.datasource.local.settings.SettingLocalDataSource
import ru.skypaws.mobileapp.data.di.storage.AirportCodeSetting
import ru.skypaws.mobileapp.data.di.utils.DispatcherIO
import ru.skypaws.mobileapp.domain.repository.settings.AirportCodeSettingRepository
import javax.inject.Inject

class AirportCodeSettingRepositoryImpl @Inject constructor(
    @AirportCodeSetting private val settingLocalDataSource: SettingLocalDataSource<Int>,
    @AirportCodeSetting private val settingGetLocalDataSource: SettingGetLocalDataSource<Int>,
    private val airportCodeSettingLocalDataSource: AirportCodeSettingLocalDataSource,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
) : AirportCodeSettingRepository {

    /**
     * Gets airport code flow from local storage.
     * @return  Flow<[Int]>
     */
    override val airportCode: Flow<Int> = settingLocalDataSource.dataFlow

    /**
     * Saves [newCode] in local storage.
     */
    override suspend fun saveNewAirportCode(newCode: Int) = withContext(dispatcherIO) {
        settingLocalDataSource.save(newCode)
    }

    /**
     * Gets airport code from local storage.
     * @return  [Int]
     */
    override suspend fun getAirportCode(): Int = withContext(dispatcherIO) {
        settingGetLocalDataSource.get()
    }

    /**
     * Updates old (previous) airport code with [newCode] in local storage.
     */
    override suspend fun updateOldAirportCodeWithNewValue(newCode: Int) =
        withContext(dispatcherIO) {
            airportCodeSettingLocalDataSource.updateAirportCodeWithNewValue(newCode)
        }

    /**
     * Checks whether new airport code is set in settings (saved locally)
     * @return true [Boolean], if newAirportCode != oldAirportCode
     */
    override suspend fun isNewCodeSet() =
        withContext(dispatcherIO) { airportCodeSettingLocalDataSource.isNewCodeSet() }
}
