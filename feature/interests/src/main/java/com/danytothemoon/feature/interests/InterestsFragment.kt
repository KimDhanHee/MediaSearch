package com.danytothemoon.feature.interests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danytothemoon.core.designsystem.theme.SearchMediaTheme
import com.danytothemoon.feature.component.MediaItemGridList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InterestsFragment : Fragment() {
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View = ComposeView(requireContext()).apply {
    setContent {
      SearchMediaTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
          InterestsScreen()
        }
      }
    }
  }

  @Composable
  private fun InterestsScreen(viewmodel: InterestsViewModel = viewModel()) {
    val mediaItems by viewmodel.interestedMediaListFlow.collectAsState(emptyList())

    when {
      mediaItems.isEmpty() -> EmptyInterest()
      else -> MediaItemGridList(mediaItems, onClickItem = viewmodel::deregisterInterest)
    }
  }

  @Composable
  private fun EmptyInterest() {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(
        imageVector = Icons.Default.BookmarkAdd,
        contentDescription = null,
        modifier = Modifier.size(96.dp),
        tint = Color.Gray
      )
      Spacer(modifier = Modifier.size(24.dp))
      Text(text = stringResource(id = R.string.empty_interest), textAlign = TextAlign.Center)
    }
  }
}