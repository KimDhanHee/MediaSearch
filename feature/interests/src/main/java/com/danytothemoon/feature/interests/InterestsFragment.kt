package com.danytothemoon.feature.interests

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.danytothemoon.feature.component.MediaItemGridList

@Composable
fun InterestsScreen(viewmodel: InterestsViewModel = hiltViewModel()) {
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