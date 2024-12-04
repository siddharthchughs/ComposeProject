package com.example.pochcompose.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.pochcompose.R

@Composable
fun HomeScreen(
    navigateToSearch: () -> Unit
) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val homeUiState =
        homeViewModel.homeUiState.collectAsStateWithLifecycle(initialValue = HomeUiState.Loading).value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxSize()
    ) {
        HomeScreenToolbar(
            navigateToSearch = navigateToSearch
        )
        HomeScreenStructure(
            homeUiState = homeUiState,
        )
    }
}

@Composable
fun HomeScreenStructure(
    homeUiState: HomeUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (homeUiState) {
            is HomeUiState.Loading -> {
                HomeScreenProgressBar(message = stringResource(R.string.progrssbar_label))
            }

            is HomeUiState.Loaded -> {
                HeroesList(homeUiState.heroesList)
            }

            is HomeUiState.TerminalError -> {
                HomeTerminalError(homeUiState.error)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenToolbar(
    navigateToSearch: () -> Unit
) {
    var isDIsplayed by remember { mutableStateOf(false) }

    TopAppBar(modifier = Modifier.background(MaterialTheme.colors.primary), title = {
        Text(
            text = stringResource(R.string.label_home_screen),
            modifier = Modifier
                .padding(start = 8.dp),
            style = TextStyle(
                fontSize = MaterialTheme.typography.h5.fontSize,
                color = MaterialTheme.colors.onSurface
            )
        )
    }, navigationIcon = {
        Image(
            Icons.Default.Home, contentDescription = "",
            modifier = Modifier
                .padding(8.dp)
        )

    }, actions = {
        IconButton(onClick = {
            isDIsplayed = true
        }) {
            Image(
                Icons.Default.MoreVert, contentDescription = stringResource(R.string.menu)
            )
        }
        DropdownMenu(expanded = isDIsplayed, onDismissRequest = { isDIsplayed = !isDIsplayed }) {
            DropdownMenuItem(onClick = {}) {
                Text(
                    text = "Items 1", style = TextStyle(
                        fontSize = MaterialTheme.typography.h6.fontSize
                    )
                )
            }
            DropdownMenuItem(onClick = navigateToSearch) {
                Text(
                    text = stringResource(R.string.search_label), style = TextStyle(
                        fontSize = MaterialTheme.typography.h6.fontSize
                    )
                )
            }
        }

    })
    Divider(
        modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = MaterialTheme.colors.primary
    )
}

@Composable
fun HeroesList(heroesList: List<HomeUiState.HeroesUIState>) {
    Column {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items(items = heroesList, key = {
                it.id
            }) {
                HeroItem(heroesUIState = it)
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun HeroItem(
    heroesUIState: HomeUiState.HeroesUIState
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {

            HeroImage(image = heroesUIState.image)
            Divider(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 2.dp),
                color = MaterialTheme.colors.primary,
                thickness = 1.dp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SingleTextView(
                    name = heroesUIState.name
                )
                HeroRatingBar(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp)
                    ,
                    rating = heroesUIState.rating,
                    maxRating = 5,
                    color = MaterialTheme.colors.primary
                )
            }
        }
    }
    Spacer(
        modifier = Modifier
            .height(12.dp)
    )

}

@Composable
fun SingleTextView(
    name: String
) {
    Text(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp),
        text = name,
        style = TextStyle(
            fontSize = MaterialTheme.typography.h5.fontSize,
            color = MaterialTheme.colors.primary
        )
    )
}

@Composable
fun HeroImage(image: String) {
        SubcomposeAsyncImage(
            alignment = Alignment.Center,
            model = ImageRequest.Builder(LocalContext.current)
                .data(image)
                .crossfade(true)
                .build(),
            loading = {
                CircularProgressIndicator()
            },
            contentDescription = stringResource(R.string.description)
        )
//    val painter = rememberAsyncImagePainter(
//        model = ImageRequest.Builder(LocalContext.current)
//            .data(image)
//            .crossfade(true)
//            .build(),
//        placeholder = painterResource(R.drawable.ic_launcher_background)
//
//    )
//    Image(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(300.dp),
//        contentScale = ContentScale.Fit,
//        painter = painter,
//        contentDescription = stringResource(R.string.description)
//    )
}

@Composable
fun HeroRatingBar(
    modifier: Modifier,
    rating: Double,
    maxRating:Int,
    color: Color
) {
    repeat(maxRating){it->
        Icon(
            modifier = modifier,
            imageVector = if(it<rating.toInt())Icons.Default.Star else Icons.Outlined.Star,
            contentDescription = stringResource(R.string.hero_rating),
            tint = if(it<rating.toInt()) color else Color.DarkGray
        )

    }
}

@Composable
fun HomeTerminalError(
    errorMessage: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = errorMessage,
            style = TextStyle(
                fontSize = androidx.compose.material3.MaterialTheme.typography.labelLarge.fontSize
            ),
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun HomeScreenProgressBar(message: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = MaterialTheme.colors.primaryVariant
        )
        Spacer(
            modifier = Modifier
                .size(12.dp)
        )
        Text(text = message)

    }
}
