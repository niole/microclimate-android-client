package com.example.microclimates

import androidx.fragment.app.Fragment
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

class MainActivity : FragmentActivity() {
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var pagerLayout: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pagerLayout = findViewById<ViewPager>(R.id.container)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        pagerLayout?.adapter = mSectionsPagerAdapter
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val setupPage = "setupPage"
        private val deploymentOverviewPage = "deploymentPage"
        private val pages = listOf<String>(deploymentOverviewPage, setupPage)

        override fun getItem(position: Int): Fragment {
            val pageName = pages[position]
            return when(pageName) {
                setupPage -> SetupPage.newInstance()
                deploymentOverviewPage -> DeploymentOverview.newInstance()
                else -> SetupPage()
            }
        }

        override fun getCount(): Int {
            return pages.size
        }
    }
}
