package com.satdev.weatherapp.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.satdev.weatherapp.R

@Composable
fun AppErrorScreen(errorMessage: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.Info, // Error icon
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error, // Use Material Design error color
            modifier = Modifier.size(64.dp) // Customize icon size
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.oops_something_went_wrong),
            style = MaterialTheme.typography.titleMedium // Heading style
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = errorMessage,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(32.dp))
        // Optional: Add a "Retry" button or other actions
    }
}