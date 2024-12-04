package com.example.pochcompose.search

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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.pochcompose.R

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SearchScreen(
    navigateUp: () -> Unit
) {
    val searchViewModel: SearchViewModel = hiltViewModel()
    val searchUIState =
        searchViewModel.searchUIState.collectAsStateWithLifecycle(initialValue = SearchUIState.Loading).value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxSize()
    ) {

//        SearchScreenStructure(
//            navigateUp = navigateUp,
//            searchUIState = searchUIState,
//            searchText = searchViewModel.searchText.value,
//            onSearchTextChange = searchViewModel::onSearchTextChange,
//        )

    }
}

@Composable
fun SearchScreenStructure(
    navigateUp: () -> Unit,
    searchUIState: SearchUIState,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
) {
    SearchScreenToolbar(
        navigateUp = navigateUp,
        searchText = searchText,
        onSearchTextChange = onSearchTextChange,
    )
    Column {
        when (searchUIState) {
            is SearchUIState.Loading -> {
                SearchScreenProgressBar(message = stringResource(R.string.progrssbar_label))
            }

            is SearchUIState.Loaded -> {
                SearchHeroesList(
                    searchUIState.listBySearch
                )
            }

            is SearchUIState.SearchTerminalError -> {
                SearchScreenTerminalError(errorMessage = searchUIState.searchError)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenToolbar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    navigateUp: () -> Unit,
) {
    val keyboardHide = LocalSoftwareKeyboardController.current
    TopAppBar(modifier = Modifier
        .background(MaterialTheme.colors.surface),
        title = {
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .background(MaterialTheme.colors.surface)
            ) {
                SearchHeroTextInputField(
                    modifier = Modifier.weight(1f)
                        .background(MaterialTheme.colors.surface),
                    value = searchText,
                    onValueChange = {
                        onSearchTextChange(it)
                    },
                    placeHolder = {
                        Text(
                            text = stringResource(R.string.search_label),
                            style = TextStyle(
                                fontSize = 18.sp,
                                color = MaterialTheme.colors.primary
                            )

                        )
                    },
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = {
                                onSearchTextChange("")
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Clear,
                                    contentDescription = stringResource(R.string.clear_text)
                                )
                            }
                        } else {
                            null
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(onSearch = {
                        keyboardHide?.hide()
                    }),
                    maxLine = 1,
                    shape = MaterialTheme.shapes.medium.copy(all = CornerSize(10.dp)),
                    color = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.surface,
                        placeholderColor = MaterialTheme.colors.primary,
                        cursorColor = MaterialTheme.colors.primary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        trailingIconColor = MaterialTheme.colors.primary,

                        )
                )

            }

        }, navigationIcon = {
            IconButton(onClick = navigateUp) {
                Image(
                    Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                    modifier = Modifier
                        .padding(8.dp),
                )
            }
        },
        actions = {}
    )
    Divider(
        modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = MaterialTheme.colors.primary
    )
}

@Composable
fun SearchHeroesList(searchList: List<SearchState>) {
    Column {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items(items = searchList, key = {
                it.searchHeroID
            }) {
                SearchHeroItem(searchState = it)
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun SearchHeroItem(
    searchState: SearchState
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

//            SearchHeroImage(image = searchState.heroImage)
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
                    name = searchState.searchHeroName
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
fun SearchHeroImage(image: String) {
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


@Composable
fun SearchHeroTextInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeHolder: @Composable () -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    onImeAction: @Composable (() -> Unit)? = null,
    modifier: Modifier,
    fontSize: Int = 16,
    textStyle: TextStyle,
    maxLine: Int = 1,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    shape: Shape,
    color: TextFieldColors
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeHolder,
        trailingIcon = trailingIcon,
        maxLines = maxLine,
        textStyle = textStyle,
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        shape = shape,
        colors = color
    )
}

@Composable
fun SearchScreenTerminalError(
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
fun SearchScreenProgressBar(message: String) {
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
