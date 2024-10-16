package ru.skypaws.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.skypaws.data.api.ApiService
import ru.skypaws.data.di.api.MainApiService
import ru.skypaws.data.error.ApiErrors
import ru.skypaws.data.error.LogErrors
import ru.skypaws.data.storage.ServiceStorage
import ru.skypaws.domain.repository.ServiceDataRepository
import java.net.SocketException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

class ServiceDataRepositoryImpl @Inject constructor(
    @MainApiService private val apiService: ApiService,
    private val serviceStorageSharedPrefs: ServiceStorage
) : ServiceDataRepository {
    /**
     * Send api request [ApiService.getPriceInfo()][ApiService.getPriceInfo]
     * to get price service data [PricesDto][ru.skypaws.data.source.dto.PricesDto]
     * from server.
     *
     * Saves data in SharedPreferences
     * [ServiceStorageSharedPrefs.savePriceInfo()][ru.skypaws.data.storage.ServiceStorageSharedPrefs.savePriceInfo]
     */
    override suspend fun getPriceInfoFromService() {
        withContext(Dispatchers.IO) {
            try {
                val prices = apiService.getPriceInfo()

                serviceStorageSharedPrefs.savePriceInfo(
                    prices.logbook,
                    prices.sync_month,
                    prices.sync_quarter,
                    prices.sync_year
                )
            } catch (_: SocketException) {
            } catch (_: UnresolvedAddressException) {
            } catch (_: ApiErrors) {
            } catch (e: Exception) {
                apiService.sendError(
                    LogErrors.fromException(
                        "ServiceDataRepositoryImpl: getPriceInfoFromServer: Exception",
                        e
                    )
                )
            }
        }
    }

    /**
     * Gets LogbookPrice data from SharedPreferences
     * [ServiceStorageSharedPrefs.getLogbookPrice()][ru.skypaws.data.storage.ServiceStorageSharedPrefs.getLogbookPrice]
     * @return [Int]
     */
    override fun getLogbookPrice(): Int = serviceStorageSharedPrefs.getLogbookPrice()

    /**
     * Gets CalendarMonthPrice data from SharedPreferences
     * [ServiceStorageSharedPrefs.getCalendarMonthPrice()][ru.skypaws.data.storage.ServiceStorageSharedPrefs.getCalendarMonthPrice]
     * @return [Int]
     */
    override fun getCalendarMonthPrice(): Int = serviceStorageSharedPrefs.getCalendarMonthPrice()

    /**
     * Gets CalendarQuarterPrice data from SharedPreferences
     * [ServiceStorageSharedPrefs.getCalendarQuarterPrice()][ru.skypaws.data.storage.ServiceStorageSharedPrefs.getCalendarQuarterPrice]
     * @return [Int]
     */
    override fun getCalendarQuarterPrice(): Int =
        serviceStorageSharedPrefs.getCalendarQuarterPrice()

    /**
     * Gets CalendarYearPrice data from SharedPreferences
     * [ServiceStorageSharedPrefs.getCalendarYearPrice()][ru.skypaws.data.storage.ServiceStorageSharedPrefs.getCalendarYearPrice]
     * @return [Int]
     */
    override fun getCalendarYearPrice(): Int = serviceStorageSharedPrefs.getCalendarYearPrice()
}
