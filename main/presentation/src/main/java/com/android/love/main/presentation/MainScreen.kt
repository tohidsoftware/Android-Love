package com.android.love.main.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainScreen() {

    val tabData = getMainTabList()
    val pagerState: PagerState = rememberPagerState(pageCount = { tabData.count() })

    Scaffold { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            MainTabLayout(tabData = tabData, pagerState = pagerState)
            MainTabsContent(tabData = tabData, pagerState = pagerState)
        }
    }
}