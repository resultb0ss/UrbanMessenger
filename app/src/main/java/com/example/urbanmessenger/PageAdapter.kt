package com.example.urbanmessenger

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PageAdapter(val fragment: Fragment, val pageList: MutableList<Page>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount() = pageList.size

    override fun createFragment(position: Int): Fragment {
        val fragment = ViewPagerFragment()
        fragment.arguments = bundleOf("page" to pageList[position])
        return fragment
    }
}