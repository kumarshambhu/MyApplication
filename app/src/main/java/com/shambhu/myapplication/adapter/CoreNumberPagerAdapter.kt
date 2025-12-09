package com.shambhu.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shambhu.myapplication.fragment.core_number.CoreNumberFragment
import com.shambhu.myapplication.fragment.core_number.KarmicNumberFragment
import com.shambhu.myapplication.fragment.core_number.LoshuGridFragment
import com.shambhu.myapplication.fragment.core_number.ChallengeNumberFragment
import com.shambhu.myapplication.fragment.core_number.PinnacleNumberFragment
import com.shambhu.myapplication.service.NumerologyService

class CoreNumberPagerAdapter(
    fa: FragmentActivity,
    private val dob: String,
    private val fullName: String,
    private val numerologyService: NumerologyService
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CoreNumberFragment.newInstance(dob,fullName, numerologyService)
            1 -> KarmicNumberFragment.newInstance(dob, fullName, numerologyService)
            2 -> PinnacleNumberFragment.newInstance(dob, fullName, numerologyService)
            3 -> ChallengeNumberFragment.newInstance(dob, fullName, numerologyService)
            4 -> LoshuGridFragment.newInstance(dob, fullName, numerologyService)
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
