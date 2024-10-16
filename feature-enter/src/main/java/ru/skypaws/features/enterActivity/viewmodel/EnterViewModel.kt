package ru.skypaws.features.enterActivity.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.skypaws.mobileapp.data.error.ApiErrors
import ru.skypaws.mobileapp.domain.usecase.enter.GetResponseForCodeUseCase
import ru.skypaws.mobileapp.domain.usecase.enter.RegisterUseCase
import ru.skypaws.mobileapp.domain.usecase.enter.SignInUseCase
import ru.skypaws.mobileapp.domain.usecase.userPayInfo.GetPayInfoUseCase
import ru.skypaws.features.enterActivity.model.EnterModel
import ru.skypaws.presentation.viewmodel.UserPayInfoViewModel
import javax.inject.Inject

@HiltViewModel
class EnterViewModel @Inject constructor(
    private val getResponseForCodeUseCase: GetResponseForCodeUseCase,
    private val registerUseCase: RegisterUseCase,
    private val signInUseCase: SignInUseCase,
    getPayInfoUseCase: GetPayInfoUseCase,
) : UserPayInfoViewModel(
    getPayInfoUseCase = getPayInfoUseCase
) {
    private val _enterDataState = MutableStateFlow(EnterModel())
    val enterDataState: StateFlow<EnterModel> = _enterDataState.asStateFlow()


    /**
     * Sets [enterDataState][enterDataState] in [EnterViewModel] to initial state
     * [EnterModel()][EnterModel]
     */
    fun setEnterDataStateToInitial() {
        _enterDataState.value = EnterModel()
    }

    /**
     * Changes [enterDataState.loading][enterDataState] to **true**.
     *
     * Sends auth data for signIn [EnterRepositoryImpl.signIn(username, password)][ru.skypaws.mobileapp.repository.EnterRepositoryImpl.signIn]
     * and gets [user][ru.skypaws.mobileapp.model.UserDomain] from server.
     *
     * Function changes properties of [enterDataState][enterDataState] in [EnterViewModel]
     * - In case there's successful response: [loading][enterDataState] and [signedIn][enterDataState] = true
     * - In case there's exception: [loading][enterDataState] = false, [signInError][enterDataState] = true
     *      - ApiErrors:
     *          - 401 -> [wrongData][enterDataState] = true
     *          - 404 -> [userNotFound][enterDataState] = true
     *          - 408 -> [aviabitServerTimeOut][enterDataState] = true
     *          - else -> [wrongData][enterDataState] = true
     *      - Exception: [networkError][enterDataState] = true
     */
    fun signIn(username: String, password: String) {
        _enterDataState.value = EnterModel(loading = true)
        viewModelScope.launch {
            try {
                signInUseCase(username, password)

                _enterDataState.update {
                    it.copy(
                        loading = false,
                        signedIn = true
                    )
                }
            } catch (e: ApiErrors) {
                when (e.code) {
                    // неверные данные
                    401 -> _enterDataState.update {
                        it.copy(
                            signInError = true,
                            loading = false,
                            wrongData = true
                        )
                    }

                    404 -> _enterDataState.update {
                        it.copy(
                            signInError = true,
                            loading = false,
                            userNotFound = true
                        )
                    }

                    408 -> _enterDataState.update {
                        it.copy(
                            signInError = true,
                            loading = false,
                            aviabitServerTimeOut = true
                        )
                    }

                    else -> _enterDataState.update {
                        it.copy(
                            signInError = true,
                            loading = false,
                            networkError = true
                        )
                    }
                }
            } catch (e: Exception) {
                _enterDataState.update {
                    it.copy(
                        signedIn = false,
                        loading = false,
                        networkError = true
                    )
                }
            }
        }
    }


    /**
     * Changes [enterDataState.loading][enterDataState] to **true**.
     *
     * Sends auth data to send code [EnterRepositoryImpl.getResponseForCode(username, password, airline)]
     * [ru.skypaws.mobileapp.repository.EnterRepositoryImpl.getResponseForCode]
     * and gets [id][Int], saving it to [enterDataState.id][enterDataState]
     *
     * Function changes properties of [enterDataState][enterDataState] in [EnterViewModel]
     * - In case there's successful response: [loading][enterDataState] and [codeIsSent][enterDataState] = true
     * - In case there's exception: [loading][enterDataState] = false,
     * [codeSendingError][enterDataState] = true
     *      - ApiErrors:
     *          - 400 -> [wrongData][enterDataState] = true
     *          - 404 -> [userNotFound][enterDataState] = true
     *          - 408 -> [aviabitServerTimeOut][enterDataState] = true
     *          - else -> [networkError][enterDataState] = true
     *      - Exception: [networkError][enterDataState] = true
     */
    fun getResponseForCode(username: String, password: String, airline: Int) {
        _enterDataState.value = EnterModel(loading = true)
        viewModelScope.launch {
            try {
                _enterDataState.value.id = getResponseForCodeUseCase(username, password, airline)

                _enterDataState.update {
                    it.copy(
                        loading = false,
                        codeIsSent = true
                    )
                }
            } catch (e: ApiErrors) {
                when (e.code) {
                    // пользователь отсуствует в Aviabit
                    404 -> _enterDataState.value = _enterDataState.value.copy(
                        loading = false,
                        codeSendingError = true,
                        userNotFound = true
                    )
                    // пользователь зарегестрирован, но пароль от Aviabit неверен (случай, когда пароль был изменен)
                    400 -> _enterDataState.value =
                        _enterDataState.value.copy(
                            loading = false,
                            codeSendingError = true,
                            wrongData = true
                        )
                    // неудалось отправить данные для кода
                    408 -> _enterDataState.value =
                        _enterDataState.value.copy(
                            loading = false,
                            codeSendingError = true,
                            aviabitServerTimeOut = true
                        )

                    else -> _enterDataState.value =
                        _enterDataState.value.copy(
                            loading = false,
                            codeSendingError = true,
                            networkError = true
                        )
                }
            } catch (e: Exception) {
                _enterDataState.value =
                    _enterDataState.value.copy(
                        loading = false,
                        codeSendingError = true,
                        networkError = true
                    )
            }
        }
    }

    /**
     * Changes [enterDataState.loading][enterDataState] to **true**.
     *
     * Sends auth data to register (verify code) [EnterRepositoryImpl.register(username, password, code, id)]
     * [ru.skypaws.mobileapp.repository.EnterRepositoryImpl.register] using enterDataState.id
     * and gets [user][ru.skypaws.mobileapp.model.UserDomain] from server.
     *
     * Function changes properties of [enterDataState][enterDataState] in [EnterViewModel]
     * - In case there's successful response: [loading][enterDataState] and [registered][enterDataState] = true
     * - In case there's exception: [loading][enterDataState] = false,
     * [codeIsSent][enterDataState] = true,  [registerError][enterDataState] = true
     *      - ApiErrors:
     *          - 400 -> [wrongData][enterDataState] = true
     *          - 408 -> [aviabitServerTimeOut][enterDataState] = true
     *          - else -> [networkError][enterDataState] = true
     *      - Exception: [networkError][enterDataState] = true
     */
    fun register(username: String, password: String, code: String) {
        _enterDataState.value = EnterModel(loading = true, codeIsSent = true, id = _enterDataState.value.id)
        viewModelScope.launch {
            try {
                registerUseCase(username, password, code, _enterDataState.value.id ?: "")

                _enterDataState.update {
                    it.copy(
                        loading = false,
                        registered = true
                    )
                }
            } catch (e: ApiErrors) {
                when (e.code) {
                    // неверный код
                    400 -> _enterDataState.update {
                        it.copy(
                            loading = false,
                            codeIsSent = true,
                            registerError = true,
                            wrongData = true
                        )
                    }
                    // неудалось отправить данные с кодом
                    408 -> _enterDataState.update {
                        it.copy(
                            loading = false,
                            codeIsSent = true,
                            registerError = true,
                            aviabitServerTimeOut = true
                        )
                    }

                    else -> _enterDataState.update {
                        it.copy(
                            loading = false,
                            codeIsSent = true,
                            registerError = true,
                            networkError = true
                        )
                    }
                }
            } catch (e: Exception) {
                _enterDataState.update {
                    it.copy(
                        loading = false,
                        codeIsSent = true,
                        registerError = true,
                        networkError = true
                    )
                }
            }
        }
    }
}
