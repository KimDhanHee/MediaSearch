package com.danytothemoon.searchmedia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.danytothemoon.core.designsystem.theme.SearchMediaTheme
import com.danytothemoon.feature.interests.InterestsScreen
import com.danytothemoon.feature.search.SearchScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  @OptIn(ExperimentalFoundationApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      SearchMediaTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
          HorizontalPager(pageCount = TabPage.values().size) { page ->
            when (page) {
              TabPage.SEARCH.ordinal -> SearchScreen()
              TabPage.INTEREST.ordinal -> InterestsScreen()
            }
          }
        }
      }
    }
  }

  private enum class TabPage {
    SEARCH,
    INTEREST,
    ;
  }
}