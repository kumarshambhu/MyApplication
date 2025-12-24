package com.shambhu.myapplication.adapter.page_adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shambhu.myapplication.fragment.core_number.ChallengeNumberFragment
import com.shambhu.myapplication.fragment.core_number.LifePathCycleFragment
import com.shambhu.myapplication.fragment.core_number.PinnacleNumberFragment
import com.shambhu.myapplication.fragment.mobile.GridPairsFragment
import com.shambhu.myapplication.fragment.others.CoreNameProfileFragment
import com.shambhu.myapplication.fragment.others.CoreNumberProfileFragment
import com.shambhu.myapplication.fragment.others.FaqFragment
import com.shambhu.myapplication.fragment.others.MaturityFragment
import com.shambhu.myapplication.fragment.others.NameGridFragment
import com.shambhu.myapplication.fragment.others.CareersFragment
import com.shambhu.myapplication.fragment.others.SuccessNumberFragment
import com.shambhu.myapplication.utils.NumeroCalculator

class ExpandablePagerAdapter(
    fa: FragmentActivity,
    private val dob: String,
    private val fullName: String
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 6

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PinnacleNumberFragment.Companion.newInstance(dob, fullName)
            1 -> MaturityFragment.Companion.newInstance(dob, fullName)
            2 -> SuccessNumberFragment.Companion.newInstance(dob, fullName)
            3 -> ChallengeNumberFragment.Companion.newInstance(dob, fullName)
            4 -> CareersFragment.newInstance(dob, fullName)
            5 -> LifePathCycleFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}