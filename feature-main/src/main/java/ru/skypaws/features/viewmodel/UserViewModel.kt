package ru.skypaws.features.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.skypaws.mobileapp.domain.model.UserDomain
import ru.skypaws.mobileapp.domain.usecase.user.CheckAuthorizationUseCase
import ru.skypaws.mobileapp.domain.usecase.user.ClearUserAndLocalDataUseCase
import ru.skypaws.mobileapp.domain.usecase.user.ClearUserUseCase
import ru.skypaws.mobileapp.domain.usecase.user.FetchUserUseCase
import ru.skypaws.mobileapp.domain.usecase.user.GetLocalUserUseCase
import ru.skypaws.mobileapp.domain.usecase.userPayInfo.GetPayInfoUseCase
import ru.skypaws.features.model.UserModel
import ru.skypaws.presentation.viewmodel.UserPayInfoViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val checkAuthorizationUseCase: CheckAuthorizationUseCase,
    private val getLocalUserUseCase: GetLocalUserUseCase,
    private val clearUserUseCase: ClearUserUseCase,
    private val clearUserAndLocalDataUseCase: ClearUserAndLocalDataUseCase,
    private val fetchUserUseCase: FetchUserUseCase,
    getPayInfoUseCase: GetPayInfoUseCase,
) : UserPayInfoViewModel(
    getPayInfoUseCase = getPayInfoUseCase
) {
    private val _userState = MutableStateFlow(UserDomain())
    val userState: StateFlow<UserDomain> = _userState.asStateFlow()

    private val _userDataState = MutableStateFlow(ru.skypaws.features.model.UserModel())
    val userDataState: StateFlow<ru.skypaws.features.model.UserModel> = _userDataState.asStateFlow()

    /**
     * Checks whether user is authorized [UserRepository.isAuthorized()]
     * [ru.skypaws.mobileapp.repository.user.UserRepository.isAuthorized].
     *
     * If authorized: gets user data from local storage.
     *
     * If NOT authorized: clears all user data.
     *
     * @return true [Boolean], if authorized.
     */
    fun isAuthorized() {
        viewModelScope.launch {
            if (checkAuthorizationUseCase()) {
                _userState.update { getLocalUserUseCase() }
            } else {
                clearUser()
            }
        }
    }

    /**
     * Clears user data from local storage by
     * [UserRepository.clearUserData][ru.skypaws.mobileapp.repository.user.UserRepository.clearUserData].
     *
     * [userDataState][_userDataState] is reset to [UserModel()][UserModel]
     */
    private suspend fun clearUser() {
        clearUserUseCase()
        _userState.update { UserDomain() }
        _userDataState.update { ru.skypaws.features.model.UserModel() }
    }

    /**
     * Clears user data and other local data (crewPlan, Logbook) from local storage by
     * [UserRepository.clearUserData][ru.skypaws.mobileapp.repository.user.UserRepository.clearUserData],
     * [CrewPlanRepository.deleteCrewPlan][ru.skypaws.mobileapp.repository.CrewPlanRepository.deleteCrewPlan],
     * [LogbookAndFlightRepository.deleteLogbookAndFlights][ru.skypaws.mobileapp.repository.logbook.LogbookAndFlightRepository.deleteLogbookAndFlights]
     *
     * [userDataState][_userDataState] is reset to [UserModel()][UserModel]
     */
    fun clearUserAndAllLocalData() {
        viewModelScope.launch {
            clearUserAndLocalDataUseCase()
            _userState.update { UserDomain() }
            _userDataState.update { ru.skypaws.features.model.UserModel() }
        }
    }

    /**
     * Sets [userDataState.userInfoLoaded][_userDataState] to **false**
     */
    fun setDefaultValueToUserDataState() {
        _userDataState.update {
            it.copy(userInfoLoaded = false)
        }
    }

    /**
     * Gets user data from server, checks user.isVerified and saves locally if true.
     *
     * Changes [userDataState.loading][_userDataState] to **true**,
     * [userDataState.errorGetUser][_userDataState] to **false**.
     *
     * Sends api request [UserRepositoryImpl.getUser()]
     * [ru.skypaws.mobileapp.repository.user.UserRepositoryImpl.fetchUser]
     * and gets [user][ru.skypaws.mobileapp.model.UserDomain].
     *
     * Saves it locally or clears all user data depending on user.isVerified.
     *
     * If [user.isVerified][ru.skypaws.mobileapp.model.UserDomain]:
     * - [userState][_userState] updated with new data except photo (no new photo from server)
     * - [userDataState.loading][_userDataState] = **false**
     * - [userDataState.userInfoLoaded][_userDataState] = **true**
     *
     * If [!user.isVerified][ru.skypaws.mobileapp.model.UserDomain]:
     * - [userDataState.loading][_userDataState] = **false**
     * - [userDataState.userNotVerified][_userDataState] = **true**
     *
     * In case there's exception: [loading][_userDataState] = false, [errorGetUser][_userDataState] = true
     */
    fun getUserFromService() {
        _userDataState.update {
            it.copy(
                loading = true,
                errorGetUser = false,
            )
        }
        viewModelScope.launch {
            try {
                val user = fetchUserUseCase()

                if (user.isVerified) {
                    _userState.update { user.copy(photo = _userState.value.photo) }

                    _userDataState.update {
                        it.copy(
                            loading = false,
                            userInfoLoaded = true
                        )
                    }
                } else {
                    _userDataState.update {
                        it.copy(
                            loading = false,
                            userNotVerified = true
                        )
                    }
                    _userState.update { UserDomain() }
                }
            } catch (e: Exception) {
                _userDataState.update {
                    it.copy(
                        loading = false,
                        errorGetUser = true
                    )
                }
            }
        }
    }
}
