package com.example.boardgamebuddy.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.boardgamebuddy.ui.CollectionFragment
import com.example.boardgamebuddy.ui.FavoritesFragment

class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm){
    override fun getCount(): Int  {
        return 2
    }
    override fun getItem(i: Int): Fragment {
        return when (i) {
            0 -> CollectionFragment.newInstance()
            else -> FavoritesFragment.newInstance()
        }
    }
    override fun getPageTitle(i: Int): CharSequence? {
        return when (i) {
            0 -> "Your collection"
            else -> "Your favorites"
        }
    }
}