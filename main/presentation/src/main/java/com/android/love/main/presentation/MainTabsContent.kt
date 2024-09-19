package com.android.love.main.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.android.love.map.presentation.AndroidLoveMap


@Composable
fun MainTabsContent(tabData: List<Pair<String, ImageVector>>, pagerState: PagerState) {

    HorizontalPager(state = pagerState) { index ->

        when (index) {
            0 -> {
                AndroidLoveMap()
            }

            1 -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text("Index $index")
                }
            }

            2 -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text("Index $index")
                }
            }

            3 -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text("Index $index")
                }
            }
        }

    }
}