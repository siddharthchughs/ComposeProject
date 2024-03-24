package com.example.pochcompose.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pochcompose.R
import com.example.pochcompose.ui.theme.Purple40
import com.example.pochcompose.ui.theme.Purple80

@Composable
fun SplashScreen(
    navigateToWelcome: () -> Unit,
    navigateToHome: () -> Unit,
) {

    val splashViewModel: SplashViewModel = hiltViewModel()
    val splashUIState =
        splashViewModel.splashUIState.collectAsStateWithLifecycle(initialValue = SplashUIState.Loading).value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        when (splashUIState) {
            SplashUIState.Loaded -> {
                val rotate = remember { Animatable(0f) }
                LaunchedEffect(key1 = true) {
                    rotate.animateTo(
                        targetValue = 360f,
                        animationSpec = tween(
                            durationMillis = 2000,
                            delayMillis = 200
                        )
                    )
                    if (splashViewModel.onBoardingCompleted.value)
                        navigateToHome()
                    else
                        navigateToWelcome()
                }
                SplashScreenStruture(degree = rotate.value)
            }

            SplashUIState.Loading -> {}
        }
    }
}

@Composable
fun SplashScreenStruture(degree: Float) {
    if (isSystemInDarkTheme()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                modifier = Modifier.rotate(degree),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(R.string.heroes_logo)
            )
        }
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Brush.verticalGradient(listOf(Purple40, Purple80)))
                .fillMaxSize()
        ) {
            Image(
                modifier = Modifier.rotate(degree),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(R.string.heroes_logo)
            )
        }
    }

}
