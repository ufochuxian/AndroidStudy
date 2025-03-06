package com.eric.material

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eric.androidstudy.R
import com.eric.androidstudy.databinding.ActivityCoordinatorLayoutBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout

class CoordinatorLayoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoordinatorLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoordinatorLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Collapsing Toolbar"

        setupViewPager()

        binding.fab.setOnClickListener {
            Toast.makeText(this, "FAB Clicked!", Toast.LENGTH_SHORT).show()
        }
        setupCustomViewTabLayout()
    }

    private fun setupCustomViewTabLayout() {
        val tabTitles = listOf("Tab 1", "Tab 2", "Tab 3")

        for (i in tabTitles.indices) {
            val tab = binding.tabs.getTabAt(i)
            tab?.customView = LayoutInflater.from(this).inflate(R.layout.tab_item, null)
            val textView = tab?.customView?.findViewById<TextView>(R.id.tab_text)
            textView?.text = tabTitles[i]
        }

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val textView = tab.customView?.findViewById<TextView>(R.id.tab_text)
                textView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                textView?.setTextColor(Color.BLACK)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val textView = tab.customView?.findViewById<TextView>(R.id.tab_text)
                textView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                textView?.setTextColor(Color.GRAY)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }


    private fun setupTabLayout() {
        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val textView = getTabTextView(it)
                    textView?.animate()
                        ?.scaleX(1.5f)
                        ?.scaleY(1.5f)
                        ?.duration = 200
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {
                    val textView = getTabTextView(it)
                    textView?.animate()
                        ?.scaleX(1.5f)
                        ?.scaleY(1.5f)
                        ?.duration = 200

                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun getTabTextView(tab: TabLayout.Tab): TextView? {
        val tabView = tab.view

        var tabTextView: TextView? = null
        val childCount = tabView.childCount
        for (i in 0 until childCount) {
            val child = tabView.getChildAt(i)
            if (child is TextView) {
                tabTextView = child
                break
            }
        }
        return tabTextView
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(SampleFragment.newInstance("Tab 1"), "Tab 1")
        adapter.addFragment(SampleFragment.newInstance("Tab 2"), "Tab 2")
        adapter.addFragment(SampleFragment.newInstance("Tab 3"), "Tab 3")

        binding.viewpager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewpager)
    }
}
