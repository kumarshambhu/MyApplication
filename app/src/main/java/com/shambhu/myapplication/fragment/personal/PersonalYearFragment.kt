package com.shambhu.myapplication.fragment.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentPersonalYearBinding
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DAY
import com.shambhu.myapplication.utils.Constants.Companion.ARG_MONTH
import com.shambhu.myapplication.utils.Constants.Companion.ARG_YEAR
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

/**
 * A simple [androidx.fragment.app.Fragment] subclass as the second destination in the navigation.
 */
class PersonalYearFragment : Fragment() {

    private var _binding: FragmentPersonalYearBinding? = null
    private lateinit var tvPersonalYearInterpretation: TextView
    private lateinit var tvPersonalYear: TextView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPersonalYearBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvPersonalYearInterpretation = view.findViewById(R.id.tvPersonalYearInterpretation)
        tvPersonalYear = view.findViewById(R.id.tvPersonalYear)
        arguments?.let {
            val day = it.getInt(ARG_DAY)
            val month = it.getInt(ARG_MONTH)
            val year = it.getInt(ARG_YEAR)
            //tvMonth.text = "$day $month $year"
            val personalYear = NumerologyCalculationUtils.calculatePersonalYear(day, month)
            tvPersonalYear.text = "$personalYear"
            val yearInterpretations = resources.getStringArray(R.array.personal_year_interpretations)
            tvPersonalYearInterpretation.text = yearInterpretations[personalYear]

            print("Data Received: $day $month $year")
        }
        /*binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_Second2Fragment_to_First2Fragment)
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(
            day: Int, month: Int, year: Int
        ): PersonalYearFragment {
            val fragment = PersonalYearFragment()
            val args = Bundle()
            args.putInt(ARG_DAY, day)
            args.putInt(ARG_MONTH, month)
            args.putInt(ARG_YEAR, year)
            fragment.arguments = args
            return fragment
        }
    }
}