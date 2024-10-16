package ru.skypaws.mobileapp.data.repository.user

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.skypaws.mobileapp.data.datasource.local.user.UserPayInfoLocalDataSource
import ru.skypaws.mobileapp.data.datasource.remote.UserRemoteDataSource
import ru.skypaws.mobileapp.data.di.utils.DispatcherIO
import ru.skypaws.mobileapp.domain.repository.user.UserPayInfoRepository
import javax.inject.Inject

class UserPayInfoRepositoryImpl @Inject constructor(
    private val userPayInfoLocalDataSource: UserPayInfoLocalDataSource,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    private val userRemoteDataSource: UserRemoteDataSource,
) : UserPayInfoRepository {
    /**
     * Fetches [PayInfo][ru.skypaws.mobileapp.model.dto.PayInfoDto] from server and saves it by
     * [ApiService.getPayInfo()][ru.skypaws.mobileapp.datasource.remote.api.ApiService.getPayInfo] and
     * [UserPayInfoLocalDataSource.savePayInfo][ru.skypaws.mobileapp.datasource.local.user.UserPayInfoLocalDataSource.savePayInfo]
     */
    override suspend fun fetchPayInfoAndSave() {
        withContext(dispatcherIO) {
            try {
                val payInfo = userRemoteDataSource.getPayInfo()
                userPayInfoLocalDataSource.savePayInfo(payInfo)
            } catch (_: Exception) {
            }
        }
    }

    /**
     * Gets [LogbookExpDate][ru.skypaws.mobileapp.model.dto.PayInfoDto.logbook_exp] from local storage by
     * [UserPayInfoLocalDataSource.getLogbookExpDate]
     * [ru.skypaws.mobileapp.datasource.local.user.UserPayInfoLocalDataSource.getLogbookExpDate]
     *
     * @return [Long]
     */
    override suspend fun getLogbookExpDate(): Long = userPayInfoLocalDataSource.getLogbookExpDate()


    /**
     * Gets [CalendarExpDate][ru.skypaws.mobileapp.model.dto.PayInfoDto.sync_exp] from local storage by
     * [UserPayInfoLocalDataSource.getCalendarExpDate]
     * [ru.skypaws.mobileapp.datasource.local.user.UserPayInfoLocalDataSource.getCalendarExpDate]
     *
     * @return [Long]
     */
    override suspend fun getCalendarExpDate(): Long = userPayInfoLocalDataSource.getCalendarExpDate()
}
