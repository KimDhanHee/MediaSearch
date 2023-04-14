package com.danytothemoon.searchmedia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.danytothemoon.core.designsystem.theme.SearchMediaTheme
import com.danytothemoon.feature.interests.InterestsScreen
import com.danytothemoon.feature.search.SearchScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      SearchMediaTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
          SearchMediaApp()
        }
      }
    }
  }

  @OptIn(ExperimentalFoundationApi::class)
  @Composable
  private fun SearchMediaApp() {
    Column {
      val pagerState = rememberPagerState()
      HorizontalPager(
        pageCount = TabPage.values().size,
        modifier = Modifier.weight(1f),
        state = pagerState
      ) { page ->
        when (page) {
          TabPage.SEARCH.ordinal -> SearchScreen()
          TabPage.INTEREST.ordinal -> InterestsScreen()
        }
      }
      val coroutineScope = rememberCoroutineScope()
      SearchMediaTab(
        currentPage = pagerState.currentPage,
        onClickSearchTab = {
          coroutineScope.launch { pagerState.animateScrollToPage(TabPage.SEARCH.ordinal) }
        },
        onClickInterestTab = {
          coroutineScope.launch { pagerState.animateScrollToPage(TabPage.INTEREST.ordinal) }
        }
      )
    }
  }

  @Composable
  private fun SearchMediaTab(
    currentPage: Int,
    onClickSearchTab: () -> Unit,
    onClickInterestTab: () -> Unit
  ) {
    Row {
      TabItem(
        modifier = Modifier.weight(1f),
        icon = TabPage.SEARCH.icon,
        text = stringResource(id = TabPage.SEARCH.textRes),
        isActivate = currentPage == TabPage.SEARCH.ordinal,
        onClick = onClickSearchTab
      )
      TabItem(
        modifier = Modifier.weight(1f),
        icon = TabPage.INTEREST.icon,
        text = stringResource(id = TabPage.INTEREST.textRes),
        isActivate = currentPage == TabPage.INTEREST.ordinal,
        onClick = onClickInterestTab
      )
    }
  }

  @Composable
  private fun TabItem(
    modifier: Modifier,
    icon: ImageVector,
    text: String,
    isActivate: Boolean = false,
    onClick: () -> Unit
  ) {
    val color = when {
      isActivate -> Color.Black
      else -> Color.Gray.copy(alpha = 0.5f)
    }
    Row(
      modifier
        .clickable { onClick() }
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Icon(imageVector = icon, contentDescription = null, tint = color)
      Spacer(modifier = Modifier.size(8.dp))
      Text(text = text, color = color)
    }
  }

  private enum class TabPage(val icon: ImageVector, @StringRes val textRes: Int) {
    SEARCH(Icons.Filled.Search, R.string.tab_title_search),
    INTEREST(Icons.Filled.Star, R.string.tab_title_interests),
    ;
  }
}