package com.shambhu.myapplication.fragment.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentPersonalMonthBinding
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DAY
import com.shambhu.myapplication.utils.Constants.Companion.ARG_MONTH
import com.shambhu.myapplication.utils.Constants.Companion.ARG_YEAR
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

/**
 * A simple [androidx.fragment.app.Fragment] subclass as the default destination in the navigation.
 */
class PersonalMonthFragment : Fragment() {

    private lateinit var tvPersonalMonthInterpretation: TextView
    private lateinit var tvPersonalMonth: TextView
    private var _binding: FragmentPersonalMonthBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPersonalMonthBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvPersonalMonthInterpretation = view.findViewById(R.id.tvPersonalMonthInterpretation)
        tvPersonalMonth = view.findViewById(R.id.tvPersonalMonth)
        arguments?.let {
            val day = it.getInt(ARG_DAY)
            val month = it.getInt(ARG_MONTH)
            val year = it.getInt(ARG_YEAR)
            //tvMonth.text = "$day $month $year"
            val personalMonth = NumerologyCalculationUtils.calculatePersonalMonth(day, month)
            tvPersonalMonth.text = personalMonth.toString()
            print("Data Received: $day $month $year")

            val monthInterpretations = resources.getStringArray(R.array.personal_month_interpretations)
            tvPersonalMonthInterpretation.text = monthInterpretations[personalMonth]
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance(
            day: Int, month: Int, year: Int
        ): PersonalMonthFragment {
            val fragment = PersonalMonthFragment()
            val args = Bundle()
            args.putInt(ARG_DAY, day)
            args.putInt(ARG_MONTH, month)
            args.putInt(ARG_YEAR, year)
            fragment.arguments = args
            return fragment
        }
    }
}