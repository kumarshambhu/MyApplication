package com.shambhu.myapplication.fragment.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentKarmicNumberBinding
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

/**
 * A simple [androidx.fragment.app.Fragment] subclass as the default destination in the navigation.
 */
class KarmicNumberFragment : Fragment() {

    private lateinit var karmic_by_date: TextView
    private lateinit var karmic_by_name: TextView
    private var _binding: FragmentKarmicNumberBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentKarmicNumberBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        karmic_by_name = view.findViewById(R.id.karmic_by_name)
        karmic_by_date = view.findViewById(R.id.karmic_by_date)
        arguments?.let {
            val day = it.getInt(ARG_DAY)
            val month = it.getInt(ARG_MONTH)
            val year = it.getInt(ARG_YEAR)
            //tvMonth.text = "$day $month $year"

            karmic_by_date.text = NumerologyCalculationUtils.calculateKarmicNumber(day, month, year)

           karmic_by_name.text = NumerologyCalculationUtils.calculateKarmicFromName("Shambhu Kumar")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_DAY = "day"
        private const val ARG_MONTH = "month"
        private const val ARG_YEAR = "year"
        private const val ARG_FULL_NAME = "full_name"

        fun newInstance(
            day: Int, month: Int, year: Int, fullName: String
        ): KarmicNumberFragment {
            val fragment = KarmicNumberFragment()
            val args = Bundle()
            args.putInt(ARG_DAY, day)
            args.putInt(ARG_MONTH, month)
            args.putInt(ARG_YEAR, year)
            args.putString(ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}