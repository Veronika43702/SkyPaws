package ru.skypaws.mobileapp.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.skypaws.mobileapp.data.datasource.db.dao.CrewPlanDao
import ru.skypaws.mobileapp.data.datasource.remote.CrewPlanRemoteDataSource
import ru.skypaws.mobileapp.data.datasource.remote.api.ApiAviabitService
import ru.skypaws.mobileapp.data.datasource.remote.api.ApiService
import ru.skypaws.mobileapp.data.di.api.MainApiService
import ru.skypaws.mobileapp.data.di.utils.DispatcherIO
import ru.skypaws.mobileapp.data.error.ApiErrors
import ru.skypaws.mobileapp.data.error.LogErrors
import ru.skypaws.mobileapp.data.mapper.crewPlanEventDomainToEntity
import ru.skypaws.mobileapp.data.mapper.crewPlanEventDtoToDomain
import ru.skypaws.mobileapp.data.mapper.toDomain
import ru.skypaws.mobileapp.domain.model.CrewPlanEvent
import ru.skypaws.mobileapp.domain.repository.CrewPlanRepository
import ru.skypaws.mobileapp.domain.usecase.settings.airportCode.GetAirportCodeUseCase
import ru.skypaws.mobileapp.domain.usecase.settings.airportCode.UpdateOldAirportUseCase
import java.io.IOException
import javax.inject.Inject

class CrewPlanRepositoryImpl @Inject constructor(
    private val crewPlanDao: CrewPlanDao,
    private val getNewAirportCodeUseCase: GetAirportCodeUseCase,
    private val updateOldAirportUseCase: UpdateOldAirportUseCase,
    @MainApiService private val apiService: ApiService,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    private val crewPlanRemoteDataSource: CrewPlanRemoteDataSource,
) : CrewPlanRepository {
    /**
     * Gets Flow of [CrewPlanEvent] data from local storage.
     */
    override val data: Flow<List<ru.skypaws.mobileapp.domain.model.CrewPlanEvent>> = crewPlanDao.getCrewPlan()
        .map { it.map { entity -> entity.toDomain() } }
        .flowOn(Dispatchers.IO)

    /**
     * Fetches [CrewPlanEvent] data
     * from server by api request [ApiAviabitService.getCrewPlan()][ApiAviabitService.fetchCrewPlan]
     * for period between -7 days from now and +2 months (31 days * 2)
     * using airport code form settings
     * [SettingGetLocalDataSource.get][ru.skypaws.mobileapp.datasource.local.settings.SettingGetLocalDataSource.get].
     *
     * Deletes old data saved locally and inserts new list from server
     * into local database [CrewPlanDao.insert()][CrewPlanDao.insert].
     *
     * Saves new airport code locally [AirportCodeSettingLocalDataSource.updateAirportCodeWithNewValue(code)][ru.skypaws.mobileapp.datasource.local.settings.AirportCodeSettingLocalDataSource.updateAirportCodeWithNewValue]
     *
     * @throws ApiErrors
     * @throws Exception
     */
    override suspend fun fetchCrewPlanFromServer() {
        withContext(dispatcherIO) {
            val dateBegin = System.currentTimeMillis() - 604800 * 1000 // now - 7 days
            val dateEnd =
                System.currentTimeMillis() + 2678400L * 2 * 1000 // + 2 month (31 days * 2)
            val code = getNewAirportCodeUseCase()
            try {
                val crewPlanEvents = crewPlanRemoteDataSource.getCrewPlan(dateBegin, dateEnd, code)
                    .crewPlanEventDtoToDomain()

                // удаление устаревших событий
                val crewPlanEventSet = crewPlanEvents.map { it.dateTakeoff }.toSet()

                getCrewPlan().filter { event ->
                    event.dateTakeoff !in crewPlanEventSet
                }.forEach {
                    deleteEvent(it.dateTakeoff)
                }

                // сохранение списка событий
                saveCrewPlan(crewPlanEvents)

                // обновление старого кода аэропорта
                updateOldAirportUseCase(code)
            } catch (e: ApiErrors) {
                if (e.code != -1) {
                    throw e
                } else {
                    throw Exception()
                }
            } catch (e: IOException) {
                apiService.sendError(
                    LogErrors.fromException("CrewPlanRepositoryImpl: getCrewPlan: IO Exception", e)
                )
                throw e
            } catch (e: Exception) {
                apiService.sendError(
                    LogErrors.fromException("CrewPlanRepositoryImpl: getCrewPlan: Exception", e)
                )
                throw e
            }
        }
    }

    /**
     * Saves crew plan data locally
     */
    override suspend fun saveCrewPlan(crewPlanEvents: List<ru.skypaws.mobileapp.domain.model.CrewPlanEvent>) {
        crewPlanDao.insert(crewPlanEvents.crewPlanEventDomainToEntity())
    }

    /**
     * Gets crew plan data from local storage
     */
    override suspend fun getCrewPlan(): List<ru.skypaws.mobileapp.domain.model.CrewPlanEvent> =
        crewPlanDao.getCurrentCrewPlan().map { it.toDomain() }

    /**
     * Deletes CrewPlan events by dateTakeOff
     */
    override suspend fun deleteEvent(dateTakeoff: String) = withContext(dispatcherIO) {
        crewPlanDao.deleteOldEvents(dateTakeoff)
    }

    /**
     * clears CrewPlan Data
     */
    override suspend fun deleteCrewPlan() = withContext(dispatcherIO) {
        crewPlanDao.deleteALl()
    }
}