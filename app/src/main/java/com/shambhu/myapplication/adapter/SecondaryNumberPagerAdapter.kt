package com.shambhu.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shambhu.myapplication.fragment.secondary_number.FontTestFragment
import com.shambhu.myapplication.fragment.secondary_number.NameElementFragment
import com.shambhu.myapplication.fragment.secondary_number.LuckyNumberFragment
import com.shambhu.myapplication.fragment.secondary_number.NameColorFragment
import com.shambhu.myapplication.fragment.secondary_number.PersonalFortuneFragment

class SecondaryNumberPagerAdapter(
    fa: FragmentActivity,
    private val dob: String,
    private val fullName: String
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NameElementFragment.newInstance(dob, fullName)
            1 -> NameColorFragment.newInstance(dob, fullName)
            2 -> PersonalFortuneFragment.newInstance(dob, fullName)
            3 -> LuckyNumberFragment.newInstance(dob, fullName)
            4 -> FontTestFragment.newInstance(dob, fullName)
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
