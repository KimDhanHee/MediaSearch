package com.danytothemoon.feature.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danytothemoon.core.data.model.MediaItem
import com.danytothemoon.core.designsystem.theme.SearchMediaTheme
import com.danytothemoon.feature.component.MediaItemGridList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View = ComposeView(requireContext()).apply {
    setContent {
      SearchMediaTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
          SearchScreen()
        }
      }
    }
  }

  @Composable
  private fun SearchScreen(viewmodel: SearchViewModel = viewModel()) {
    Column {
      SearchTextField(onClickSearch = { keyword ->
        viewmodel.searchMedia(keyword)
      })
      val uiState by viewmodel.uiState.collectAsState()
      SearchResult(
        uiState,
        onClickItem = { mediaItem ->
          when {
            mediaItem.isInterested -> viewmodel.deregisterInterest(mediaItem)
            else -> viewmodel.registerInterest(mediaItem)
          }
        },
        onLoadMore = {
          viewmodel.loadMoreMedia()
        }
      )
    }
  }

  @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
  @Composable
  private fun SearchTextField(onClickSearch: (String) -> Unit) {
    var searchKeyword by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
      value = searchKeyword,
      onValueChange = { searchKeyword = it },
      modifier = Modifier.fillMaxWidth(),
      placeholder = { Text(text = stringResource(id = R.string.search_input_text_field_hint)) },
      trailingIcon = {
        Icon(
          imageVector = Icons.Rounded.Cancel,
          contentDescription = null,
          modifier = Modifier.clickable { searchKeyword = "" }
        )
      },
      keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
      keyboardActions = KeyboardActions(onSearch = {
        onClickSearch(searchKeyword)
        keyboardController?.hide()
      }),
    )
  }

  @Composable
  private fun SearchResult(
    uiState: SearchUiState,
    onClickItem: (MediaItem) -> Unit,
    onLoadMore: () -> Unit,
  ) {
    when (uiState) {
      is SearchUiState.Success -> MediaItemGridList(
        uiState.mediaItems,
        onClickItem,
        onBottomReached = onLoadMore
      )
      SearchUiState.Loading -> Loading()
      else -> Unit
    }
  }

  @Composable
  private fun Loading() {
    Dialog(
      onDismissRequest = { },
      DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
      CircularProgressIndicator()
    }
  }
}