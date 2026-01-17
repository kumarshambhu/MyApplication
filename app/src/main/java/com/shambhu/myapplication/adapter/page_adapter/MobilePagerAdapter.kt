package com.shambhu.myapplication.adapter.page_adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shambhu.myapplication.fragment.mobile.GridPairsFragment
import com.shambhu.myapplication.fragment.mobile.MainFragment
import com.shambhu.myapplication.fragment.mobile.MobileNumerologyFragment
import com.shambhu.myapplication.fragment.mobile.FaqFragment

class MobilePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GridPairsFragment()
            1 -> MobileNumerologyFragment()
            2 -> MainFragment()
            3 -> FaqFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}