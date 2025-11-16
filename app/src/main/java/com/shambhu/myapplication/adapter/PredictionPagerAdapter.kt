package com.shambhu.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shambhu.myapplication.fragment.prediction.CoreNumberFragment
import com.shambhu.myapplication.fragment.prediction.KarmicNumberFragment
import com.shambhu.myapplication.fragment.prediction.IChingFragment

class PredictionPagerAdapter(
    fa: FragmentActivity,
    private val dob: String,
    private val fullName: String
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CoreNumberFragment.newInstance(dob,fullName)
            1 -> KarmicNumberFragment.newInstance(dob, fullName)
            2 -> IChingFragment.newInstance(dob,fullName)
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
