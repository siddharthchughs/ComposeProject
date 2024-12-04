package com.example.pochcompose.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.pochcompose.R

@Composable
fun DetailScreen(
    navigateUp:()->Unit
){
  Column(modifier = Modifier
      .fillMaxSize()
  ) {
      DetailScreenToolbar(navigateUp = navigateUp)
  }
}

@Composable
fun DetailScreenStructure(
    navigateUp:()->Unit
){

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenToolbar(
    navigateUp:()->Unit
){
    TopAppBar(modifier = Modifier
        .background(MaterialTheme.colors.primary
        ),
        title = {
             Text(text = stringResource(R.string.detail_label))
        },
        navigationIcon = {
                         IconButton(onClick = {
                             navigateUp()
                         }){
                             Icon(imageVector = Icons.Default.ArrowBack,
                                 contentDescription = stringResource(R.string.back_icon)
                             )
                         }
        },
        actions = {

        }
    )

}
