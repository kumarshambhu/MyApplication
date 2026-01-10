package com.shambhu.myapplication.adapter.page_adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shambhu.myapplication.fragment.prediction.CreateCyclesFragment
import com.shambhu.myapplication.fragment.prediction.MonthlyPredictionFragment
import com.shambhu.myapplication.fragment.prediction.YearlyPredictionFragment
import com.shambhu.myapplication.fragment.prediction.LifePredictionFragment
import com.shambhu.myapplication.fragment.prediction.PsychicNumbersFragment

class PredictionPagerAdapter(
    fa: FragmentActivity,
    private val dob: String,
    private val fullName: String
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CreateCyclesFragment.Companion.newInstance(dob, fullName)
            1 -> MonthlyPredictionFragment.Companion.newInstance(dob, fullName)
            2 -> YearlyPredictionFragment()
            3 -> LifePredictionFragment()
            4 -> PsychicNumbersFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
