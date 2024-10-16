package ru.skypaws.features.mainActivity.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.skypaws.domain.model.User
import ru.skypaws.domain.usecase.crewPlan.DeleteCrewPlanUseCase
import ru.skypaws.domain.usecase.logbook.DeleteLogbookUseCase
import ru.skypaws.domain.usecase.settings.GetThemeUseCase
import ru.skypaws.domain.usecase.user.CheckAuthorizationUseCase
import ru.skypaws.domain.usecase.user.ClearUserUseCase
import ru.skypaws.domain.usecase.user.GetLocalUserUseCase
import ru.skypaws.domain.usecase.userPayInfo.GetPayInfoUseCase
import ru.skypaws.features.mainActivity.model.UserModel
import ru.skypaws.features.viewmodel.UserPayInfoViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val checkAuthorizationUseCase: CheckAuthorizationUseCase,
    private val getLocalUserUseCase: GetLocalUserUseCase,
    private val clearUserUseCase: ClearUserUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val getUserUseCase: GetLocalUserUseCase,
    private val getPayInfoUseCase: GetPayInfoUseCase,
    private val deleteCrewPlanUseCase: DeleteCrewPlanUseCase,
    private val deleteLogbookUseCase: DeleteLogbookUseCase
) : UserPayInfoViewModel(
    getPayInfoUseCase = getPayInfoUseCase
) {
    private val _userState = MutableStateFlow(User())
    val userState: StateFlow<User> = _userState.asStateFlow()

    private val _userDataState = MutableStateFlow(UserModel())
    val userDataState: StateFlow<UserModel> = _userDataState.asStateFlow()

    init {
        isAuthorized()
    }

    /**
     * Checks whether user is authorized [UserRepositoryImpl.isAuthorized()]
     * [ru.skypaws.data.repository.UserRepositoryImpl.isAuthorized].
     *
     * If authorized: gets user data from SharedPreferences.
     *
     * If NOT authorized: clears all user data.
     *
     * @return true [Boolean], if authorized.
     */
    fun isAuthorized(): Boolean {
        return if (checkAuthorizationUseCase()) {
            _userState.value = getLocalUserUseCase()
            true
        } else {
            clearUser()
            false
        }
    }

    /**
     * Calls [UserRepositoryImpl.clearUserData()]
     * [ru.skypaws.data.repository.UserRepositoryImpl.clearUserData] to
     * totally clear all user data from prefAuth.
     *
     * [userDataState][_userDataState] is reset to [UserModel()][UserModel]
     */
    fun clearUser() {
        clearUserUseCase()
        _userDataState.value = UserModel()
    }

    /**
     * Sets [userDataState.userInfoLoaded][_userDataState] to **false**
     */
    fun setDefaultValueToUserDataState() {
        _userDataState.value = _userDataState.value.copy(userInfoLoaded = false)
    }

    /**
     * Changes [userDataState.loading][_userDataState] to **true**,
     * [userDataState.errorGetUser][_userDataState] to **false**.
     *
     * Sends api request [UserRepositoryImpl.getUser()]
     * [ru.skypaws.data.repository.UserRepositoryImpl.getUser]
     * and gets [user][ru.skypaws.domain.model.User].
     *
     * Saves it to SharedPreferences or clears all user data depending on user.is_verified.
     *
     * If [user.is_verified][ru.skypaws.domain.model.User]:
     * - [userState][_userState] updated with new data except photo (no new photo from server)
     * - [userDataState.loading][_userDataState] = **false**
     * - [userDataState.userInfoLoaded][_userDataState] = **true**
     *
     * If [!user.is_verified][ru.skypaws.domain.model.User]:
     * - [userDataState.loading][_userDataState] = **false**
     * - [userDataState.userNotVerified][_userDataState] = **true**
     *
     * In case there's exception: [loading][_userDataState] = false, [errorGetUser][_userDataState] = true
     */
    fun getUserFromService() {
        _userDataState.value = _userDataState.value.copy(
            loading = true,
            errorGetUser = false,
        )
        viewModelScope.launch {
            try {
                val user = getUserUseCase()

                if (user.isVerified) {
                    _userState.value = user.copy(photo = _userState.value.photo)

                    _userDataState.value = _userDataState.value.copy(
                        loading = false,
                        userInfoLoaded = true
                    )
                } else {
                    _userDataState.value =
                        _userDataState.value.copy(
                            loading = false,
                            userNotVerified = true
                        )
                }
            } catch (e: Exception) {
                _userDataState.value = _userDataState.value.copy(
                    loading = false,
                    errorGetUser = true
                )
            }
        }
    }

    /**
     * Gets theme from SharedPreferences [SettingsRepositoryImpl.getTheme()]
     * [ru.skypaws.data.repository.SettingsRepositoryImpl.getTheme]
     * @return theme [Int] (0 - system, 1 - light, 2 - dark)
     */
    fun getTheme(): Int = getThemeUseCase()

    /**
     * Clears CrewPlanDao and LogbookDao.
     */
    fun deleteDB() {
        viewModelScope.launch {
            deleteCrewPlanUseCase()
            deleteLogbookUseCase()
        }
    }
}
