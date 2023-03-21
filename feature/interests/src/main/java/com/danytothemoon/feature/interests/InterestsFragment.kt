package com.danytothemoon.feature.interests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danytothemoon.core.designsystem.theme.SearchMediaTheme
import com.danytothemoon.feature.component.MediaItemList
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
        val viewmodel: InterestsViewModel = viewModel()
        val mediaItems by viewmodel.interestedMediaListFlow.collectAsState()
        MediaItemList(mediaItems, onClickItem = viewmodel::deregisterInterest)
      }
    }
  }
}