package com.shambhu.myapplication.adapter.page_adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shambhu.myapplication.fragment.prediction.DailyPredictionFragment
import com.shambhu.myapplication.fragment.prediction.MonthlyPredictionFragment
import com.shambhu.myapplication.fragment.prediction.YearlyPredictionFragment
import com.shambhu.myapplication.fragment.prediction.LifePredictionFragment

class PredictionPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DailyPredictionFragment()
            1 -> MonthlyPredictionFragment()
            2 -> YearlyPredictionFragment()
            3 -> LifePredictionFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
