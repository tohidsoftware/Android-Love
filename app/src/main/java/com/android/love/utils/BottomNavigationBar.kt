package com.android.love.utils

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import com.android.love.provider.NavigationItem

@Composable
fun BottomNavigationBar(navHostController: NavHostController) {

    val items = listOf(
        NavigationItem.Map, NavigationItem.Media
    )

    var selectedItem by remember { mutableIntStateOf(0) }
    var currentRoute by remember { mutableStateOf(NavigationItem.Map.route) }

    items.forEachIndexed { index, navigationItem ->
        if (navigationItem.route == currentRoute) {
            selectedItem = index
        }
    }

    NavigationBar {
        items.forEachIndexed { index, navigationItem ->
            NavigationBarItem(alwaysShowLabel = true, icon = {
                Icon(
                    navigationItem.icon, contentDescription = navigationItem.title
                )
            }, label = {
                Text(
                    navigationItem.title, overflow = TextOverflow.Ellipsis, maxLines = 1
                )
            }, selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    currentRoute = navigationItem.route
                    navHostController.navigate(navigationItem.route) {
                        navHostController.graph.startDestinationRoute?.let {
                            popUpTo(route = it) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = androidx.compose.material3.NavigationBarItemDefaults
                    .colors(
                        selectedTextColor = Blue,
                        selectedIconColor = Blue,
                        unselectedTextColor = Gray,
                    )
            )
        }
    }

}