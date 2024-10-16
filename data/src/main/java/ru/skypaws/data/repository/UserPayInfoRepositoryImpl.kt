package ru.skypaws.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.skypaws.data.api.ApiService
import ru.skypaws.data.di.api.MainApiService
import ru.skypaws.data.error.ApiErrors
import ru.skypaws.data.error.LogErrors
import ru.skypaws.data.storage.UserStorage
import ru.skypaws.domain.repository.UserPayInfoRepository
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class UserPayInfoRepositoryImpl @Inject constructor(
    @MainApiService private val apiService: ApiService,
    private val userStorageSharedPrefs: UserStorage
) : UserPayInfoRepository {

    /**
     * Sends request [ApiService.getPayInfo()][ru.skypaws.data.api.ApiService.getPayInfo]
     * to get [PayInfo][ru.skypaws.data.source.dto.PayInfoDto] from server
     * and save it to SharedPreferences
     * [userStorageSharedPrefs.savePayInfo(payInfo)][ru.skypaws.data.storage.UserStorageSharedPrefs.savePayInfo]
     */
    override suspend fun getPayInfo() {
        withContext(Dispatchers.IO) {
            try {
                val payInfo = apiService.getPayInfo()

                userStorageSharedPrefs.savePayInfo(payInfo)
            } catch (_: ApiErrors) {
            } catch (_: UnknownHostException) {
            } catch (_: SocketTimeoutException) {
            } catch (e: Exception) {
                apiService.sendError(
                    LogErrors.fromException(
                        "UserPayInfoRepository: getPayInfo: Exception", e
                    )
                )
            }
        }
    }

    /**
     * Gets [LogbookExpDate][ru.skypaws.data.source.dto.PayInfoDto.logbook_exp] from SharedPreferences
     * [userStorageSharedPrefs.getLogbookExpDate()]
     * [ru.skypaws.data.storage.UserStorageSharedPrefs.getLogbookExpDate]
     *
     * @return [Long]
     */
    override fun getLogbookExpDate(): Long = userStorageSharedPrefs.getLogbookExpDate()


    /**
     * Gets [CalendarExpDate][ru.skypaws.data.source.dto.PayInfoDto.sync_exp] from SharedPreferences
     * [userStorageSharedPrefs.getCalendarExpDate()]
     * [ru.skypaws.data.storage.UserStorageSharedPrefs.getCalendarExpDate]
     *
     * @return [Long]
     */
    override fun getCalendarExpDate(): Long = userStorageSharedPrefs.getCalendarExpDate()
}
