package com.shambhu.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shambhu.myapplication.fragment.mobile.GridPairsFragment
import com.shambhu.myapplication.fragment.mobile.MobileNumerologyFragment

class MobilePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GridPairsFragment()
            1 -> MobileNumerologyFragment()
            2 -> GridPairsFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
