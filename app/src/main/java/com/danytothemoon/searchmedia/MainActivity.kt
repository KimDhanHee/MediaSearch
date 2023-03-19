package com.danytothemoon.searchmedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.danytothemoon.searchmedia.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
      viewPager.adapter = SearchMediaPageAdapter(this@MainActivity)

      TabLayoutMediator(viewTab, viewPager) { tab, position ->
        tab.text = getString(position.toPage().tabTitleRes)
      }.attach()
    }
  }

  private fun Int.toPage(): SearchMediaPage = when (this) {
    SearchMediaPage.SEARCH.ordinal -> SearchMediaPage.SEARCH
    SearchMediaPage.INTERESTS.ordinal -> SearchMediaPage.INTERESTS
    else -> throw IllegalArgumentException()
  }
}