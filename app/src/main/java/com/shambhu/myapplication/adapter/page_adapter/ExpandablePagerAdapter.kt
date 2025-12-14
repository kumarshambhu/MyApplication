package com.shambhu.myapplication.adapter.page_adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shambhu.myapplication.fragment.core_number.ChallengeNumberFragment
import com.shambhu.myapplication.fragment.core_number.PinnacleNumberFragment
import com.shambhu.myapplication.fragment.mobile.GridPairsFragment
import com.shambhu.myapplication.fragment.others.CoreNameProfileFragment
import com.shambhu.myapplication.fragment.others.CoreNumberProfileFragment
import com.shambhu.myapplication.fragment.others.FaqFragment
import com.shambhu.myapplication.fragment.others.MaturityFragment
import com.shambhu.myapplication.fragment.others.NameGridFragment
import com.shambhu.myapplication.fragment.others.SuccessNumberFragment

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
            4 -> GridPairsFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}