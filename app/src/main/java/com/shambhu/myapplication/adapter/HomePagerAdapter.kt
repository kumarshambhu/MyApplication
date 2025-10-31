package com.shambhu.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shambhu.myapplication.fragment.home.BirthNumbersFragment
import com.shambhu.myapplication.fragment.home.ExpressionFragment
import com.shambhu.myapplication.fragment.home.LifePathFragment
import com.shambhu.myapplication.fragment.home.PersonalityFragment
import com.shambhu.myapplication.fragment.home.SoulUrgeFragment

class HomePagerAdapter(
    fragmentActivity: FragmentActivity,
    private val fullName: String,
    private val dob: String,
    private val time: String,
    private val location: String,
    private val lifePath: Int,
    private val expression: Int,
    private val personality: Int,
    private val soulUrge: Int,
    private val birthDay: Int,
    private val birthMonth: Int,
    private val birthYear: Int
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PersonalityFragment.newInstance(fullName, dob, time, location, personality)
            1 -> LifePathFragment.newInstance(lifePath)
            2 -> ExpressionFragment.newInstance(fullName, dob, time, location, expression)
            3 -> BirthNumbersFragment.newInstance(fullName, dob, time, location, birthDay, birthMonth, birthYear)
            4 -> SoulUrgeFragment.newInstance(fullName, dob, time, location, soulUrge)
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
