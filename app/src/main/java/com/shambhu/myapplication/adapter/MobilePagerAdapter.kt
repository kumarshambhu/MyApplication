package com.shambhu.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shambhu.myapplication.fragment.core_number.CoreNumberFragment
import com.shambhu.myapplication.fragment.core_number.KarmicNumberFragment
import com.shambhu.myapplication.fragment.core_number.LoshuGridFragment

class MobilePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CoreNumberFragment.newInstance("","")
            1 -> KarmicNumberFragment.newInstance("","")
            2 -> LoshuGridFragment.newInstance("","")
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
