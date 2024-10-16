package ru.skypaws.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.skypaws.data.api.ApiAviabitService
import ru.skypaws.data.api.ApiService
import ru.skypaws.data.db.CrewPlanDao
import ru.skypaws.data.di.api.AviabitApiService
import ru.skypaws.data.di.api.MainApiService
import ru.skypaws.data.error.ApiErrors
import ru.skypaws.data.error.LogErrors
import ru.skypaws.data.mapper.toDomain
import ru.skypaws.data.source.dto.CrewPlanEventDto
import ru.skypaws.data.source.entity.crewPlanEventDtoToEntity
import ru.skypaws.data.source.entity.crewPlanEventEntityToDto
import ru.skypaws.data.storage.SettingsStorage
import ru.skypaws.domain.model.CrewPlanEvent
import ru.skypaws.domain.repository.CrewPlanRepository
import java.io.IOException
import java.net.SocketException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

class CrewPlanRepositoryImpl @Inject constructor(
    private val crewPlanDao: CrewPlanDao,
    private val settingsStorageSharedPrefs: SettingsStorage,
    @MainApiService private val apiService: ApiService,
    @AviabitApiService private val apiAviabitService: ApiAviabitService,
) : CrewPlanRepository {
    override val data: Flow<List<CrewPlanEvent>> = crewPlanDao.getCrewPlan()
        .map { it.map { entity -> entity.toDomain() } }
        .flowOn(Dispatchers.IO)


    /**
     * Check whether new airport code is set in settings (saved in SharedPreferences)
     * @return true [Boolean], if newAirportCode != oldAirportCode
     */
    override fun isNewCodeSet() = settingsStorageSharedPrefs.isNewCodeSet()


    /**
     * Send api request [ApiAviabitService.getCrewPlan()][ApiAviabitService.getCrewPlan]
     * to get crew plan data [List(CrewPlanEventDto)][ru.skypaws.data.source.dto.CrewPlanEventDto]
     * from server for period between -7 days from now and +2 months (31 days * 2)
     * using airport code form settings [SettingsStorageSharedPrefs.getNewAirportCode()][SettingsStorage.getNewAirportCode].
     *
     * Takes current crew plan from local database [CrewPlanDao.getCurrentCrewPlan()][CrewPlanDao.getCurrentCrewPlan],
     * filter events not contained in new list from server and delete them.
     *
     *          val currentEvents: List<CrewPlanEventDto> = crewPlanDao.getCurrentCrewPlan().toDto()
     *          val eventsToDelete = currentEvents.filterNot { currentFlight ->
     *              crewPlanEvents.any { it.dateTakeoff == currentFlight.dateTakeoff }
     *          }
     *          eventsToDelete.forEach { event ->
     *              crewPlanDao.deleteOldEvents(event.dateTakeoff)
     *          }
     *
     * Insert new list from server into local database [CrewPlanDao.insert()][CrewPlanDao.insert].
     *
     *
     * Saves new airport code in sharedPreferences [SettingsStorage.saveOldAirportCode(code)][SettingsStorage.saveOldAirportCode]
     *
     * @throws ApiErrors
     * @throws Exception
     */
    override suspend fun getCrewPlan() {
        withContext(Dispatchers.IO) {
            val dateBegin = System.currentTimeMillis() - 604800 * 1000 // now - 7 days
            val dateEnd =
                System.currentTimeMillis() + 2678400L * 2 * 1000 // + 2 month (31 days * 2)
            val code = settingsStorageSharedPrefs.getNewAirportCode()
            try {
                val crewPlanEvents =
                    apiAviabitService.getCrewPlan(dateBegin, dateEnd, false, code)

                // поиск и удаление событий, которых больше нет в авиабите
                val currentEvents: List<CrewPlanEventDto> =
                    crewPlanDao.getCurrentCrewPlan().crewPlanEventEntityToDto()

                val eventsToDelete = currentEvents.filterNot { currentFlight ->
                    crewPlanEvents.any { it.dateTakeoff == currentFlight.dateTakeoff }
                }
                eventsToDelete.forEach { event ->
                    crewPlanDao.deleteOldEvents(event.dateTakeoff)
                }

                // обновление списка событий (добавление новых и изменение старых)
                crewPlanDao.insert(crewPlanEvents.crewPlanEventDtoToEntity())

                settingsStorageSharedPrefs.saveOldAirportCode(code)
            } catch (e: SocketException) {
                throw Exception()
            } catch (e: UnresolvedAddressException) {
                throw Exception()
            } catch (e: ApiErrors) {
                throw ApiErrors(e.code, e.message)
            } catch (e: IOException) {
                apiService.sendError(
                    LogErrors.fromException("CrewPlanRepositoryImpl: getCrewPlan: IO Exception", e)
                )
                throw Exception()
            } catch (e: Exception) {
                apiService.sendError(
                    LogErrors.fromException("CrewPlanRepositoryImpl: getCrewPlan: Exception", e)
                )
                throw Exception()
            }
        }
    }

    /**
     * deletes CrewPlanDao
     */
    override suspend fun delete() = withContext(Dispatchers.IO) {
        crewPlanDao.deleteALl()
    }

}