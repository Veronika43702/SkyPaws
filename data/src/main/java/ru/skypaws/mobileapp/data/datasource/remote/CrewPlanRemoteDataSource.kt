package ru.skypaws.mobileapp.data.datasource.remote

import ru.skypaws.mobileapp.data.datasource.remote.api.ApiAviabitService
import ru.skypaws.mobileapp.data.datasource.remote.api.ApiService
import ru.skypaws.mobileapp.data.di.api.AviabitApiService
import ru.skypaws.mobileapp.data.di.api.MainApiService
import ru.skypaws.mobileapp.data.error.ApiErrors
import ru.skypaws.mobileapp.data.error.LogErrors
import ru.skypaws.mobileapp.data.model.dto.CrewPlanEventDto
import java.io.IOException
import java.net.SocketException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

class CrewPlanRemoteDataSource @Inject constructor(
    @MainApiService private val apiService: ApiService,
    @AviabitApiService private val apiAviabitService: ApiAviabitService,
) {
    suspend fun getCrewPlan(dateBegin: Long, dateEnd: Long, code: Int): List<CrewPlanEventDto> {
        return try {
            apiAviabitService.fetchCrewPlan(dateBegin, dateEnd, false, code)
        } catch (e: SocketException) {
            throw Exception()
        } catch (e: UnresolvedAddressException) {
            throw Exception()
        } catch (e: ApiErrors) {
            throw e
        } catch (e: IOException) {
            apiService.sendError(
                LogErrors.fromException("CrewPlanRemoteDataSource: getCrewPlan: IO Exception", e)
            )
            throw ApiErrors(-1, null)
        } catch (e: Exception) {
            apiService.sendError(
                LogErrors.fromException("CrewPlanRemoteDataSource: getCrewPlan: Exception", e)
            )
            throw ApiErrors(-1, null)
        }
    }
}