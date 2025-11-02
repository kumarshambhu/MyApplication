package com.shambhu.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shambhu.myapplication.LoshuGridFragment

class OtherPagerAdapter(
    fa: FragmentActivity,
    private val dob: String,
    private val fullName: String
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return 1
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LoshuGridFragment.newInstance(dob, fullName)
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}