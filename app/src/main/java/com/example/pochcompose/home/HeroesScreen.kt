package com.example.pochcompose.home

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.pochcompose.R
import com.example.pochcompose.customcomponent.HeroRating
import com.example.pochcompose.database.Hero

@SuppressLint("SuspiciousIndentation")
@Composable
fun HeroesScreen(
    navigateToSettings: () -> Unit,
    navigateToSearch: () -> Unit,
    navigateToDetail: () -> Unit
) {
    val heroesViewModel: HeroesViewModel = hiltViewModel()
    val allHeroes = heroesViewModel.heroesFlow.collectAsLazyPagingItems()


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxSize()
    ) {
        HeroesScreenToolbar(
            navigateToSettings = navigateToSettings,
            navigateToSearch = navigateToSearch
        )

        HeroesScreenStructure(
            heroUIState = allHeroes,
            navigateToSearch = {},
            navigateToDetail = { navigateToDetail() }
        )

    }
}

@Composable
fun HeroesScreenStructure(
    heroUIState: LazyPagingItems<Hero>,
    navigateToSearch: () -> Unit,
    navigateToDetail: () -> Unit
) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 4.dp)
        ) {
            items(
                count = heroUIState.itemCount,
                key = heroUIState.itemKey {
                    heroUIState-> heroUIState.id
                },
                contentType = heroUIState.itemContentType { "Heroes" }
            ){ index: Int ->
                val items  = heroUIState[index]
                if(items != null){
                    HeroItemUI(
                        heroState = items,
                        navigateToDetail = navigateToDetail

                    )
                }
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroesScreenToolbar(
    navigateToSettings: () -> Unit,
    navigateToSearch: () -> Unit
) {
    var isDIsplayed by remember { mutableStateOf(false) }

    TopAppBar(modifier = Modifier.background(MaterialTheme.colors.primary), title = {
        Text(
            text = "Heroes",
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
            DropdownMenuItem(onClick = {
                navigateToSettings()
            }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.label_settings)
                )
                Text(
                    text = stringResource(R.string.label_settings),
                    style = TextStyle(
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

//@Composable
//fun HeroList(heroesList: List<HeroUIState.HeroState>) {
//    Column {
//        LazyColumn(
//            modifier = Modifier.fillMaxWidth()
//                .padding(horizontal = 8.dp, vertical = 8.dp)
//        ) {
//            items(items = heroesList, key = {
//                it.id
//            }) {
//                HeroItemUI(
//                    heroState = it,
//                    navigateToDetail = {}
//                )
//            }
//        }
//    }
//}

@Composable
fun HeroItemUI(
    heroState: Hero,
    navigateToDetail: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clickable(
                    onClick = {
                        navigateToDetail()
                    }
                )
        ) {
            HeroImage(heroState.image)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .alpha(0.6f)
                    .background(MaterialTheme.colors.primaryVariant)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                ) {
                    SingleHeroTextView(
                        heroState.name,
                        modifier = Modifier
                            .weight(1f)
                    )
                    HeroRating(
                        modifier = Modifier
                            .weight(0.5f),
                        rating = heroState.rating
                    )
                }

                AboutHeroTextView(heroState.about)
            }
        }
    }
    Spacer(
        modifier = Modifier
            .height(12.dp)
    )

}

@Composable
fun HeroImageUI(heroState: Hero) {
    val painter = rememberImagePainter(
            data="http://192.168.1.3:8080$heroState.image")
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        painter = painter,
        contentScale = ContentScale.FillHeight,
        contentDescription = stringResource(R.string.hero_image)
    )
}

//@SuppressLint("SuspiciousIndentation")
//@Composable
//fun HroItem(
//    heroesUIState: HeroState
//) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        elevation = 2.dp,
//        shape = MaterialTheme.shapes.medium
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .wrapContentHeight()
//        ) {
//
//            HroItemImage(image = heroesUIState.image)
//            Divider(
//                modifier = Modifier.fillMaxWidth()
//                    .padding(horizontal = 2.dp),
//                color = MaterialTheme.colors.primary,
//                thickness = 1.dp
//            )
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
////                SingleHeroTextView(
////                    name = heroesUIState.name
////                )
//
//                HeroRating(
//                    modifier = Modifier.padding(all = 8.dp),
//                    rating = heroesUIState.rating
//                )
//            }
//        }
//    }
//    Spacer(
//        modifier = Modifier
//            .height(12.dp)
//    )
//
//}

@Composable
fun SingleHeroTextView(
    name: String,
    modifier: Modifier
) {
    Text(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 8.dp),
        text = name,
        style = TextStyle(
            fontSize = MaterialTheme.typography.h6.fontSize,
            color = MaterialTheme.colors.surface
        )
    )
}

@Composable
fun AboutHeroTextView(
    aboutHero: String
) {
    val maxLine by remember { mutableStateOf(4) }
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 4.dp)
    ) {
        Text(
            modifier = Modifier
                .wrapContentHeight()
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .animateContentSize(),
            text = aboutHero,
            style = TextStyle(
                fontSize = MaterialTheme.typography.h6.fontSize,
                color = MaterialTheme.colors.surface
            ),
            overflow = TextOverflow.Ellipsis,
            maxLines = if (isExpanded) Int.MAX_VALUE else maxLine
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 1.dp,
            color = Color.Yellow
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (maxLine > 0) {
                if (!isExpanded)
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                            .wrapContentHeight()
                            .clickable {
                                isExpanded = !isExpanded
                            },
                        text = "Read More",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.h6.fontSize,
                            color = MaterialTheme.colors.surface
                        )
                    )
                else
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                            .wrapContentHeight()
                            .clickable {
                                isExpanded = !isExpanded
                            },
                        text = "Read Less",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.h6.fontSize,
                            color = MaterialTheme.colors.surface
                        ),
                        textAlign = TextAlign.End
                    )
            }
        }
    }
}

@Composable
fun HroItemImage(image: String) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(image)
            .build(),
    )
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentScale = ContentScale.Fit,
        painter = painter,
        contentDescription = stringResource(R.string.description)
    )
}

//@Composable
//fun SingleHeroRatingBar(
//    modifier: Modifier,
//    rating: Double,
//    maxRating: Int,
//    color: Color
//) {
//    repeat(maxRating) { it ->
//        Icon(
//            modifier = modifier,
//            imageVector = if (it < rating.toInt()) Icons.Default.Star else Icons.Outlined.Star,
//            contentDescription = stringResource(R.string.hero_rating),
//            tint = if (it < rating.toInt()) color else Color.DarkGray
//        )
//
//    }
//}

@Composable
fun HeroTerminalError(
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
fun HeroScreenProgressBar(message: String) {
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
