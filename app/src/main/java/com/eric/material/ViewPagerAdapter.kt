package com.eric.material

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragmentList = mutableListOf<Fragment>()
    private val titleList = mutableListOf<String>()

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        titleList.add(title)
    }

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence = titleList[position]
}
