package ru.skypaws.mobileapp.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.skypaws.mobileapp.data.datasource.local.service.ServiceLocalDataSource
import ru.skypaws.mobileapp.data.datasource.remote.ServiceRemoteDataSource
import ru.skypaws.mobileapp.data.di.utils.DispatcherIO
import ru.skypaws.mobileapp.domain.repository.ServiceDataRepository
import javax.inject.Inject

class ServiceDataRepositoryImpl @Inject constructor(
    private val serviceLocalDataSource: ServiceLocalDataSource,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    private val serviceRemoteDataSource: ServiceRemoteDataSource
) : ServiceDataRepository {
    /**
     * Fetches price service data [PricesDto][ru.skypaws.mobileapp.model.dto.PricesDto]
     * from server and saves it in local storage.
     */
    override suspend fun fetchPriceInfoFromService() {
        withContext(dispatcherIO) {
            try {
                val prices = serviceRemoteDataSource.getPriceInfoFromService()

                serviceLocalDataSource.savePriceInfo(
                    prices.logbook,
                    prices.sync_month,
                    prices.sync_quarter,
                    prices.sync_year
                )
            } catch (_: Exception) {
            }
        }
    }

    /**
     * Gets LogbookPrice data from local storage
     * [ServiceLocalDataSource.getLogbookPrice()][ru.skypaws.mobileapp.datasource.local.ServiceLocalDataSource.getLogbookPrice]
     * @return [Int]
     */
    override suspend fun getLogbookPrice(): Int =
        withContext(dispatcherIO) { serviceLocalDataSource.getLogbookPrice() }

    /**
     * Gets CalendarMonthPrice data from local storage
     * [ServiceLocalDataSource.getCalendarMonthPrice()][ru.skypaws.mobileapp.datasource.local.ServiceLocalDataSource.getCalendarMonthPrice]
     * @return [Int]
     */
    override suspend fun getCalendarMonthPrice(): Int =
        withContext(dispatcherIO) { serviceLocalDataSource.getCalendarMonthPrice() }

    /**
     * Gets CalendarQuarterPrice data from local storage
     * [ServiceLocalDataSource.getCalendarQuarterPrice()][ru.skypaws.mobileapp.datasource.local.ServiceLocalDataSource.getCalendarQuarterPrice]
     * @return [Int]
     */
    override suspend fun getCalendarQuarterPrice(): Int =
        withContext(dispatcherIO) { serviceLocalDataSource.getCalendarQuarterPrice() }

    /**
     * Gets CalendarYearPrice data from local storage
     * [ServiceLocalDataSource.getCalendarYearPrice()][ru.skypaws.mobileapp.datasource.local.ServiceLocalDataSource.getCalendarYearPrice]
     * @return [Int]
     */
    override suspend fun getCalendarYearPrice(): Int =
        withContext(dispatcherIO) { serviceLocalDataSource.getCalendarYearPrice() }
}
