package com.shambhu.myapplication.adapter.page_adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shambhu.myapplication.fragment.core_number.KarmicNumberFragment
import com.shambhu.myapplication.fragment.core_number.LoshuGridFragment
import com.shambhu.myapplication.fragment.core_number.CoreNameProfileFragment
import com.shambhu.myapplication.fragment.core_number.CoreNumberProfileFragment
import com.shambhu.myapplication.fragment.core_number.LuckyNumberFragment
import com.shambhu.myapplication.fragment.core_number.NameCorrectionFragment

class CoreNumberPagerAdapter(
    fa: FragmentActivity,
    private val dob: String,
    private val fullName: String
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 6

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CoreNumberProfileFragment.newInstance(dob)
            1 -> CoreNameProfileFragment.newInstance(dob,fullName)
            2 -> KarmicNumberFragment.newInstance(dob, fullName)
            3 -> LuckyNumberFragment.newInstance(dob, fullName)
            4 -> LoshuGridFragment.newInstance(dob, fullName)
            5 -> NameCorrectionFragment.newInstance(dob, fullName)
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}