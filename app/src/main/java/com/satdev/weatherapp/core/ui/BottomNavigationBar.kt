package com.satdev.weatherapp.core.ui

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.satdev.weatherapp.navigation.ScreenDestinations

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        ScreenDestinations.Home,
        ScreenDestinations.Forecast,
    )
    var selectedItem : ScreenDestinations by remember { mutableStateOf(ScreenDestinations.Home) }

    BottomNavigation {
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = stringResource(id = screen.resourceId)) },
                label = { Text(stringResource(id = screen.resourceId)) },
                selected = selectedItem == screen,
                onClick = {
                    selectedItem = screen
                    navController.navigate(screen.route)
                },
            )
        }
    }
}