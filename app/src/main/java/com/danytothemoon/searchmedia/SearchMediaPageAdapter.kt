package com.danytothemoon.searchmedia

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.danytothemoon.feature.interests.InterestsFragment
import com.danytothemoon.feature.search.SearchFragment

class SearchMediaPageAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
  override fun getItemCount(): Int = SearchMediaPage.values().size

  override fun createFragment(position: Int): Fragment = when (position) {
    SearchMediaPage.SEARCH.ordinal -> SearchFragment()
    SearchMediaPage.INTERESTS.ordinal -> InterestsFragment()
    else -> throw IllegalArgumentException()
  }
}

enum class SearchMediaPage {
  SEARCH,
  INTERESTS,
  ;

  val tabName: String
    get() = when (this) {
      SEARCH -> "Search"
      INTERESTS -> "Interests"
    }
}