package com.shambhu.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shambhu.myapplication.fragment.FaqFragment
import com.shambhu.myapplication.fragment.core_number.CoreNumberFragment
import com.shambhu.myapplication.fragment.core_number.KarmicNumberFragment
import com.shambhu.myapplication.fragment.core_number.LoshuGridFragment
import com.shambhu.myapplication.fragment.core_number.ChallengeNumberFragment
import com.shambhu.myapplication.fragment.core_number.PinnacleNumberFragment

class ExpandablePagerAdapter(
    fa: FragmentActivity,
    private val dob: String,
    private val fullName: String
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FaqFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
