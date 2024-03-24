package com.example.pochcompose.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pochcompose.R

@Composable
fun SearchScreen(){
        val searchViewModel: SearchViewModel = hiltViewModel()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {

        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenToolbar() {
    var isDIsplayed by remember { mutableStateOf(false) }

    TopAppBar(modifier = Modifier.background(MaterialTheme.colors.primary), title = {
        Text(
            text = stringResource(R.string.label_home_screen), style = TextStyle(
                fontSize = MaterialTheme.typography.h5.fontSize,
                color = MaterialTheme.colors.onSurface
            )
        )
    }, navigationIcon = {
        IconButton(onClick = {}) {
            Image(
                Icons.Default.Home, contentDescription = ""
            )
        }

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
            DropdownMenuItem(onClick = {}) {
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