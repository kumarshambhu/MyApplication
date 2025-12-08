package com.shambhu.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shambhu.myapplication.fragment.others.FaqFragment
import com.shambhu.myapplication.fragment.core_number.CoreNumberFragment
import com.shambhu.myapplication.fragment.core_number.KarmicNumberFragment
import com.shambhu.myapplication.fragment.core_number.LoshuGridFragment
import com.shambhu.myapplication.fragment.core_number.ChallengeNumberFragment
import com.shambhu.myapplication.fragment.core_number.PinnacleNumberFragment
import com.shambhu.myapplication.fragment.others.MaturityFragment
import com.shambhu.myapplication.fragment.others.MobileNumerologyFragment
import com.shambhu.myapplication.fragment.others.NameGridFragment
import com.shambhu.myapplication.fragment.others.NumeroProfileFragment
import com.shambhu.myapplication.fragment.others.SuccessNumberFragment

class ExpandablePagerAdapter(
    fa: FragmentActivity,
    private val dob: String,
    private val fullName: String
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 6

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FaqFragment()
            1 -> NameGridFragment.newInstance(dob, fullName)
            2 -> NumeroProfileFragment.newInstance(dob)
            3 -> MaturityFragment.newInstance(dob, fullName)
            4 -> SuccessNumberFragment.newInstance(dob, fullName)
            5 -> MobileNumerologyFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
