package com.example.pochcompose.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pochcompose.R
import com.example.pochcompose.utility.activeIndicators
import com.example.pochcompose.utility.inactiveIndicators
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState

@Composable
fun WelcomeScreen(
    navigateToHome: () -> Unit
) {
    val welcomeViewModel: WelcomeViewModel = hiltViewModel()
    val welcomeUIState =
        welcomeViewModel.welcomeUIState.collectAsStateWithLifecycle(initialValue = WelcomeUIState.Loading).value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onPrimary)
    )
    {
        WelcomeScreenStructure(
            welcomeUIState = welcomeUIState,
            onBoardingOnce = welcomeViewModel::onBoardingOnce,
            navigateToHome = navigateToHome
        )
    }
}

@Composable
fun WelcomeScreenStructure(
    welcomeUIState: WelcomeUIState,
    onBoardingOnce: (Boolean) -> Unit,
    navigateToHome: () -> Unit
) {
    when (welcomeUIState) {
        is WelcomeUIState.Loading -> {
            WelcomeProgressBar()
        }

        is WelcomeUIState.Loaded -> {
            WelcomeOnBoradingLables(
                aboardLabel = onBoardingLabels,
                onBoardingOnce = onBoardingOnce,
                navigateToHome =  navigateToHome
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeOnBoradingLables(
    aboardLabel: List<WelcomeOnBoard>,
    onBoardingOnce: (Boolean) -> Unit,
    navigateToHome: () -> Unit
) {
    val pagerState = rememberPagerState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        HorizontalPager(
            modifier = Modifier
                .background(Color.White)
                .weight(10f),
            count = aboardLabel.size,
            state = pagerState,
            contentPadding = PaddingValues(16.dp, 16.dp),
            verticalAlignment = Alignment.Top
        ) {
            PagerScreen(welcomeUIState = aboardLabel[it])
        }
        HorizontalPagerIndicator(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
                .wrapContentHeight()
                .background(Color.White),
            pagerState = pagerState,
            activeColor = MaterialTheme.colors.activeIndicators,
            inactiveColor = MaterialTheme.colors.inactiveIndicators,
            indicatorWidth = 20.dp,
            spacing = 8.dp
        )

        FinishButton(
            modifier = Modifier
                .weight(1f),
            pagerState = pagerState,
            onClick = {
                navigateToHome()
                onBoardingOnce(true)
            }
        )
    }
}

@Composable
fun PagerScreen(welcomeUIState: WelcomeOnBoard) {
    DisplayonBoardingLayout(welcomeUIState.label, welcomeUIState.image)
}

@Composable
fun DisplayonBoardingLayout(label: String, image: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .padding(8.dp, 8.dp)
            .fillMaxSize()
    )
    {
        Image(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.7f),
            painter = painterResource(image),
            contentDescription = label
        )

        Text(
            text = label,
            style = TextStyle(
                fontSize = MaterialTheme.typography.h4.fontSize
            )
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun FinishButton(
    modifier: Modifier,
    pagerState: PagerState,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            )
    ) {
        AnimatedVisibility(
            visible = pagerState.currentPage == 2
        ) {
            Spacer(modifier.width(16.dp))
            Button(
                modifier = modifier.fillMaxWidth(1f),
                onClick = onClick
            ) {
                Text(
                    text = stringResource(R.string.label_get_started),
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.h5.fontSize
                    )
                )
            }
            Spacer(modifier.width(16.dp))
        }
    }
}

@Composable
fun WelcomeProgressBar() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(Modifier.fillMaxSize(1f))
        CircularProgressIndicator(
            color = MaterialTheme.colors.primary
        )
        Spacer(Modifier.fillMaxSize(1f))
    }
}


