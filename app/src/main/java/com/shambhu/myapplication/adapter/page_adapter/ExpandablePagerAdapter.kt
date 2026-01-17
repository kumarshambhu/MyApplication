package com.shambhu.myapplication.adapter.page_adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shambhu.myapplication.fragment.others.ChallengeNumberFragment
import com.shambhu.myapplication.fragment.others.LifePathCycleFragment
import com.shambhu.myapplication.fragment.others.PinnacleNumberFragment
import com.shambhu.myapplication.fragment.mobile.GridPairsFragment
import com.shambhu.myapplication.fragment.core_number.CoreNameProfileFragment
import com.shambhu.myapplication.fragment.core_number.CoreNumberProfileFragment
import com.shambhu.myapplication.fragment.mobile.FaqFragment
import com.shambhu.myapplication.fragment.others.MaturityFragment
import com.shambhu.myapplication.fragment.secondary_number.NameGridFragment
import com.shambhu.myapplication.fragment.random.CareersFragment
import com.shambhu.myapplication.fragment.others.SuccessNumberFragment
import com.shambhu.myapplication.utils.NumeroCalculator

class ExpandablePagerAdapter(
    fa: FragmentActivity,
    private val dob: String,
    private val fullName: String
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PinnacleNumberFragment.Companion.newInstance(dob, fullName)
            1 -> MaturityFragment.Companion.newInstance(dob, fullName)
            2 -> SuccessNumberFragment.Companion.newInstance(dob, fullName)
            3 -> ChallengeNumberFragment.Companion.newInstance(dob, fullName)
            //4 -> CareersFragment.newInstance(dob, fullName)
            4 -> LifePathCycleFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}