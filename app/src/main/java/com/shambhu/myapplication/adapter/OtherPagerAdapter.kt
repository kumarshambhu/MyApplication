package com.shambhu.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shambhu.myapplication.fragment.other.NumerologyPlainFragment
import com.shambhu.myapplication.fragment.other.LoshuGridFragment
import com.shambhu.myapplication.fragment.other.TestingFragment

class OtherPagerAdapter(
    fa: FragmentActivity,
    private val dob: String,
    private val fullName: String
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LoshuGridFragment.newInstance(dob, fullName)
            1 -> NumerologyPlainFragment.newInstance(dob, fullName)
            2 -> TestingFragment.newInstance(dob, fullName)
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
