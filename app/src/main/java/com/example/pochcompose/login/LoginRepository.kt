package com.example.pochcompose.login

import com.example.pochcompose.setting.ApplicationSetting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

enum class LoginState {
    LOGGED_IN,
    NOT_LOGGED_IN
}


interface LoginRepository {

    var loginState: Flow<LoginState>

    suspend fun loginResponse(
        username: String,
        password: String
    ): LoginNetworkResult
}

class LoginRepositoryImpl @Inject constructor(
    private val loginNetworkLocalResponse: LoginNetworkLocalDataSource,
    private val applicationSetting: ApplicationSetting,
    private val dispatcher: CoroutineDispatcher
) : LoginRepository {
    override var loginState: Flow<LoginState> = flow {
        if (applicationSetting.token().first() != null) {
            emit(LoginState.LOGGED_IN)
        } else {
            emit(LoginState.NOT_LOGGED_IN)
        }
    }


    override suspend fun loginResponse(username: String, password: String): LoginNetworkResult =
        withContext(dispatcher) {
            try {
                val response = loginNetworkLocalResponse.login(
                    userAgent = "user",
                    username = username,
                    password = password
                )
                return@withContext when (response) {

                    is LoginNetworkResult.LoggedIn -> {
                        applicationSetting.saveToken(response.token)
                    }

                    is LoginNetworkResult.UnAuthorizedResponse -> {
                        response.message
                    }

                    is LoginNetworkResult.UnAuthorizedError -> {
                        response.code
                    }

                    else -> {}
                }
            } catch (e: Throwable) {
                LoginNetworkResult.UnAuthorizedResponse(
                    e.message ?: "Unnknow", detail = e.printStackTrace().toString()
                )
            }

        } as LoginNetworkResult
}


