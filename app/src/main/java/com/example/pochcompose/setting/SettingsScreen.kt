package com.example.pochcompose.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pochcompose.R

@Composable
fun SettingsScreen(
    navigationUp: () -> Unit
) {

    val settingViewmodel: SettingsViewModel = hiltViewModel()
    val settingUiState =
        settingViewmodel.settingUIState.collectAsStateWithLifecycle(initialValue = SettingUIState.NotRequired)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top, modifier = Modifier
            .fillMaxSize()
    ) {
        SettingsToolbar(
            navigationUp = navigationUp
        )
        SettingsScreenStructure(
            settingUIState = settingUiState.value,
            navigationUp = navigationUp
        )
    }
}

@Composable
fun SettingsScreenStructure(
    settingUIState: SettingUIState,
    navigationUp: () -> Unit
) {
    when (settingUIState) {
        is SettingUIState.Loaded -> {
            SettingItems(settingUIState.list)
        }

        SettingUIState.NotRequired -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsToolbar(
    navigationUp: () -> Unit
) {
    TopAppBar(modifier = Modifier.fillMaxWidth(), title = {
        androidx.compose.material3.Text(
            text = stringResource(R.string.label_settings)
        )
    },
        navigationIcon = {
            IconButton(onClick = {
                navigationUp()
            }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )

            }
        },
        actions = {
        }
    )
}

@Composable
fun SettingItems(settingUIState: List<SettingUIState.SettingItems>) {
    LazyColumn {
        items(settingUIState) { it ->
            when (it) {
                SettingUIState.CrashItem -> {
                    SingleItemLayout(
                        label = stringResource(R.string.crash_label),
                        icon = Icons.Default.ExitToApp
                    )
                }

                is SettingUIState.LogoutItem -> {
                    SingleItemLayout(
                        label = stringResource(R.string.login_label),
                        icon = Icons.Sharp.AccountCircle
                    )
                }
            }
        }
    }
}

@Composable
fun SingleItemLayout(label: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        SingleItemIcon(
            modifier = Modifier
                .weight(1f),
            image = icon
        )
        SingleItemText(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),

            label = label
        )
    }
}

@Composable
inline fun SingleItemText(
    modifier: Modifier,
    label: String
) {
    Text(
        modifier = modifier,
        text = label,
        style = TextStyle(
            color = MaterialTheme.colors.onBackground,
            fontSize = MaterialTheme.typography.h5.fontSize
        )
    )
}

@Composable
inline fun SingleItemIcon(
    modifier: Modifier,
    image: ImageVector
) {
    Image(
        modifier = Modifier
            .size(48.dp)
            .padding(horizontal = 8.dp),
        imageVector = image,
        contentDescription = stringResource(R.string.crash_label)
    )
}


