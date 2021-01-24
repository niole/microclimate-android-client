package com.example.microclimates

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class PeripheralListViewAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val items: List<DeviceViewModel>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return SensorCard.newInstance(items[position])
    }

    override fun getItemId(position: Int): Long {
        return items[position].id.toLong()
    }

}

