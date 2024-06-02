package com.satdev.weatherapp.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.satdev.weatherapp.navigation.ScreenDestinations

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        ScreenDestinations.Home,
        ScreenDestinations.Forecast,
    )
    var selectedItem: ScreenDestinations by remember { mutableStateOf(ScreenDestinations.Home) }
    Column {
        Divider(thickness = 1.dp, color = Color.Black)
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            items.forEach { screen ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = stringResource(id = screen.resourceId),
                            modifier = Modifier.size(45.dp)
                        )
                    },
                    label = { Text(stringResource(id = screen.resourceId),style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), modifier = Modifier.offset(y = 10.dp)) },
                    selected = selectedItem == screen,
                    onClick = {
                        selectedItem = screen
                        navController.navigate(screen.route)
                    },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.primary)

                )
            }
        }
    }

}