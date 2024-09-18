package com.android.love.provider

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(val route: String, val icon: ImageVector, val title: String) {
    data object Media :
        NavigationItem(route = "media", icon = Icons.Default.AccountBox, title = "media")

    data object Map : NavigationItem(route = "map", icon = Icons.Default.LocationOn, title = "map")
}