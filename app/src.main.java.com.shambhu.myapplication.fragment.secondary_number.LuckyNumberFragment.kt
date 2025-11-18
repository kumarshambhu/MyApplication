package com.shambhu.myapplication.fragment.secondary_number

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shambhu.myapplication.databinding.FragmentLuckyNumberBinding
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import java.time.LocalDate

class LuckyNumberFragment : Fragment() {

    private var _binding: FragmentLuckyNumberBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLuckyNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val dob = it.getString(Constants.ARG_DOB)
            val fullName = it.getString(Constants.ARG_FULL_NAME)
            if (dob != null && fullName != null) {
                bindLuckyUnluckyNumbers(dob, fullName)
            }
        }
    }

    private fun bindLuckyUnluckyNumbers(dob: String, fullName: String) {
        val date = CommonUtils.parseDate(dob)
        val day = date.dayOfMonth
        val month = date.monthValue
        val year = date.year

        // Calculate and display lucky number
        val luckyNumber = NumerologyCalculationUtils.calculateLuckyNumber(day)
        binding.luckyNumberValue.text = luckyNumber.toString()

        // Calculate and display unlucky numbers (Karmic Debt)
        val karmicDebtNumbers = NumerologyCalculationUtils.calculateKarmicDebtNumbers(day, month, year, fullName)
        val unluckyNumbersText = if (karmicDebtNumbers.isNotEmpty()) {
            karmicDebtNumbers.joinToString(", ") { it.second.toString() }
        } else {
            "None"
        }
        binding.unluckyNumbersValueTextView.text = unluckyNumbersText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dob: String, fullName: String): LuckyNumberFragment {
            val fragment = LuckyNumberFragment()
            val args = Bundle()
            args.putString(Constants.ARG_DOB, dob)
            args.putString(Constants.ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}
