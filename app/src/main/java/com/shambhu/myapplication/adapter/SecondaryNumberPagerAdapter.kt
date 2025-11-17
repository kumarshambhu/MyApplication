package com.shambhu.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shambhu.myapplication.fragment.secondary_number.ElementColorFragment
import com.shambhu.myapplication.fragment.secondary_number.LuckyNumberFragment
import com.shambhu.myapplication.fragment.secondary_number.PersonalMonthYearFragment

class SecondaryNumberPagerAdapter(
    fa: FragmentActivity,
    private val dob: String,
    private val fullName: String
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ElementColorFragment.newInstance(dob, fullName)
            1 -> PersonalMonthYearFragment.newInstance(dob, fullName)
            2 -> LuckyNumberFragment.newInstance(dob, fullName)
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
