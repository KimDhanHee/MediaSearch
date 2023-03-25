package com.danytothemoon.feature.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.danytothemoon.core.data.model.MediaItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaItemGridList(
  mediaItems: List<MediaItem>,
  onClickItem: (MediaItem) -> Unit,
  onBottomReached: () -> Unit = {},
) {
  val listState = rememberLazyStaggeredGridState()

  listState.OnBottomReached(onBottomReached)

  LazyVerticalStaggeredGrid(
    columns = StaggeredGridCells.Fixed(2),
    state = listState,
    contentPadding = PaddingValues(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    items(mediaItems, key = { it.url }) { mediaItem ->
      MediaListItem(mediaItem, onClick = { onClickItem(mediaItem) })
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyStaggeredGridState.OnBottomReached(callback: () -> Unit) {
  val shouldLoadMore = remember {
    derivedStateOf {
      val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf true

      lastVisibleItem.index == layoutInfo.totalItemsCount - 1
    }
  }

  LaunchedEffect(shouldLoadMore) {
    snapshotFlow { shouldLoadMore.value }
      .collect {
        if (it) callback()
      }
  }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun MediaListItem(media: MediaItem, onClick: () -> Unit) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(8.dp))
      .clickable { onClick() })
  {
    GlideImage(
      model = media.url,
      contentDescription = null,
      contentScale = ContentScale.FillWidth
    )
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color.Black.copy(alpha = 0.5f))
        .padding(8.dp)
        .align(Alignment.BottomCenter),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = Icons.Rounded.Star, contentDescription = null, tint = when {
          media.isInterested -> Color.Red
          else -> Color.White
        }
      )
      Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.End
      ) {
        Text(
          text = media.dateStr,
          textAlign = TextAlign.End,
          color = Color.White
        )
        Text(
          text = media.timeStr,
          textAlign = TextAlign.End,
          color = Color.White
        )
      }
    }
  }
}