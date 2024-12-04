package com.example.pochcompose.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pochcompose.setting.ApplicationSetting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LoginResult {
    data object Available : LoginResult
    data object LoggedIn : LoginResult
    data object NotLoggedIn : LoginResult
    data class Failure(
        val error: String
    ) : LoginResult
}

sealed interface LoginUiState {
    data object Initial : LoginUiState
    data object Loading : LoginUiState
    data object Loaded : LoginUiState
    data class TerminalError(
        val error: String
    ) : LoginUiState
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val applicationSetting: ApplicationSetting,
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val loginResult = MutableStateFlow<LoginResult>(LoginResult.NotLoggedIn)
    val usernameText = mutableStateOf("")
    val passwordText = mutableStateOf("")

    val uiLoginState: Flow<LoginUiState> = loginResult.map {
        when (it) {
            LoginResult.Available -> LoginUiState.Initial
            LoginResult.LoggedIn -> LoginUiState.Loaded
            is LoginResult.Failure -> LoginUiState.TerminalError(error = it.error)
            LoginResult.NotLoggedIn -> TODO()
        }
    }

    fun onUsernameChange(username: String) {
        usernameText.value = username
    }

    fun onPasswordChange(password: String) {
        passwordText.value = password
    }

    fun login() {
        viewModelScope.launch {
            loginResult.value = LoginResult.Available
            val response = loginRepository.loginResponse(
                username = usernameText.value,
                password = passwordText.value
            )
            if (response != null)
                loginResult.value = LoginResult.LoggedIn
            else
                loginResult.value = LoginResult.Failure(error = "Invalid Login")
        }
    }

    fun loginErrorMessage(error: String) {
        loginResult.value = LoginResult.Failure(error = error)
    }
}