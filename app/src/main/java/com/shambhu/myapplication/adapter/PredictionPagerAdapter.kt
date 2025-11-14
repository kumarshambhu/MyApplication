package com.shambhu.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shambhu.myapplication.fragment.prediction.HoroscopeFragment
import com.shambhu.myapplication.fragment.prediction.TarotFragment
import com.shambhu.myapplication.fragment.prediction.PalmistryFragment
import com.shambhu.myapplication.fragment.prediction.CrystalBallFragment
import com.shambhu.myapplication.fragment.prediction.IChingFragment
import com.shambhu.myapplication.fragment.prediction.RunesFragment
import com.shambhu.myapplication.fragment.prediction.TeaLeafFragment
import com.shambhu.myapplication.fragment.prediction.AstrologyFragment

class PredictionPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 8

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HoroscopeFragment()
            1 -> TarotFragment()
            2 -> PalmistryFragment()
            3 -> CrystalBallFragment()
            4 -> IChingFragment()
            5 -> RunesFragment()
            6 -> TeaLeafFragment()
            7 -> AstrologyFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
