package com.shambhu.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shambhu.myapplication.fragment.personal.KarmicNumberFragment
import com.shambhu.myapplication.fragment.personal.PersonalMonthFragment
import com.shambhu.myapplication.fragment.personal.PersonalYearFragment

class PersonalPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val birthDay: Int,
    private val birthMonth: Int,
    private val birthYear: Int,
    private val fullName: String
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PersonalMonthFragment.newInstance(birthDay, birthMonth, birthYear)
            1 -> PersonalYearFragment.newInstance(birthDay, birthMonth, birthYear)
            2 -> KarmicNumberFragment.newInstance(birthDay, birthMonth, birthYear, fullName)
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}