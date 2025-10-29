package com.shambhu.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shambhu.myapplication.fragment.home.BirthNumbersFragment
import com.shambhu.myapplication.fragment.home.ExpressionFragment
import com.shambhu.myapplication.fragment.home.LifePathFragment
import com.shambhu.myapplication.fragment.home.PersonalityFragment
import com.shambhu.myapplication.fragment.home.SoulUrgeFragment

class HomePagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PersonalityFragment()
            1 -> LifePathFragment()
            2 -> ExpressionFragment()
            3 -> BirthNumbersFragment()
            4 -> SoulUrgeFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}