package com.example.pochcompose.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pochcompose.R

@Composable
fun LoginScreen() {

    val loginRequestModel: LoginViewModel = hiltViewModel()
    val loginUIState =
        loginRequestModel.uiLoginState.collectAsStateWithLifecycle(initialValue = LoginUiState.Loading).value

    Column {
        LoginStructure(
            loginUiState = loginUIState,
            username = loginRequestModel.usernameText.value,
            onUsernameChange = loginRequestModel::onUsernameChange,
            password = loginRequestModel.passwordText.value,
            onPasswordChange = loginRequestModel::onPasswordChange,
            errorMessage = loginRequestModel::loginErrorMessage,
            login = loginRequestModel::login
        )
    }
}

@Composable
fun LoginStructure(
    loginUiState: LoginUiState,
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    errorMessage: (String) -> Unit,
    login: () -> Unit
) {
    when (loginUiState) {
        LoginUiState.Initial, is LoginUiState.Loading -> {
            LoginProgressBar()
        }

        LoginUiState.Loaded -> {

            LoginForm(
                username =username,
                onUsernameChange = onUsernameChange,
                password = password,
                onPasswordChange = onPasswordChange,
                errorMessage = errorMessage,
                login = login
            )

        }

        is LoginUiState.TerminalError -> {
           LoginTerminalError(
               errorMessage = errorMessage.toString()
           )
        }
    }

}


@Composable
fun LoginForm(
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    errorMessage:(String)->Unit,
    login: () -> Unit
) {
    LoginUsernameInputField(
        username = username,
        onUsernameChange = {
            onUsernameChange(it)
        },
        label = stringResource(R.string.login_label_username),
        singleLine = true,
        maxLine = 1,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        textStyle = TextStyle(
            fontSize = MaterialTheme.typography.labelMedium.fontSize,
            color = MaterialTheme.colorScheme.primary
        )
    )
    LoginPasswordInput(
        password = password,
        onPasswordChange = {
            onPasswordChange(it)
        },
        passwordLabel = stringResource(R.string.login_label_password),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        textStyle = TextStyle(
            fontSize = MaterialTheme.typography.labelMedium.fontSize,
            color = MaterialTheme.colorScheme.primary
        )
    )

    LoginButton(
        onUsernameChange = onUsernameChange,
        onPasswordChange = onPasswordChange,
        login = login
    )
    Login(
        username = username,
        password = password,
        login = login
    )

    LoginTerminalError(
        errorMessage = errorMessage.toString()
    )


}


@Composable
fun LoginUsernameInputField(
    username: String,
    label: String,
    onUsernameChange: (String) -> Unit,
    onImeAction: () -> Unit = {},
    singleLine: Boolean,
    maxLine: Int = 1,
    modifier: Modifier = Modifier,
    textStyle: TextStyle
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = username,
        onValueChange = onUsernameChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = {
            onImeAction()
            keyboardController?.hide()
        }),
        singleLine = singleLine,
        maxLines = maxLine,
        modifier = modifier,
        textStyle = textStyle
    )
}


@Composable
fun LoginPasswordInput(
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordLabel: String,
    onImeAction: () -> Unit = {},
    trailingIcon: (() -> Unit)? = null,
    singleLine: Boolean,
    modifier: Modifier = Modifier,
    textStyle: TextStyle
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = password,
        onValueChange = onPasswordChange,
        label = {
            Text(text = passwordLabel)
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            onImeAction()
            keyboardController?.hide()
        }),
        modifier = modifier,
        singleLine = singleLine,
        textStyle = textStyle
    )
}

@Composable
fun Login(
    username: String,
    password: String,
    login: () -> Unit
) {


}

@Composable
fun LoginButton(
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    login: () -> Unit,
) {
    Button(onClick = login) {
        Text(
            text = stringResource(R.string.login_label)
        )
    }

}

@Composable
fun LoginTerminalError(
    errorMessage: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = errorMessage,
            style = TextStyle(
                fontSize = MaterialTheme.typography.labelLarge.fontSize
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoginProgressBar() {

}


