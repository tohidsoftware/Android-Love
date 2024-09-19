package com.android.love.screen

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.android.love.provider.Navigation
import com.android.love.utils.BottomNavigationBar


@Composable
fun MainScreen(navHostController: NavHostController) {
    /*Scaffold(
        bottomBar = {
            BottomAppBar(modifier = Modifier) {
                BottomNavigationBar(navHostController = navHostController)
            }
        }
    ) { innerPadding ->
        val padding = innerPadding
        Navigation(navHostController = navHostController)
    }*/

    com.android.love.main.presentation.MainScreen()
}