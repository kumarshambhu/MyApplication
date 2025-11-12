package com.shambhu.myapplication.fragment.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.databinding.FragmentKarmicNumberBinding
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DAY
import com.shambhu.myapplication.utils.Constants.Companion.ARG_FULL_NAME
import com.shambhu.myapplication.utils.Constants.Companion.ARG_MONTH
import com.shambhu.myapplication.utils.Constants.Companion.ARG_YEAR
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

/**
 * A simple [androidx.fragment.app.Fragment] subclass as the default destination in the navigation.
 */
class KarmicNumberFragment : Fragment() {

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
        arguments?.let {
            val day = it.getInt(ARG_DAY)
            val month = it.getInt(ARG_MONTH)
            val year = it.getInt(ARG_YEAR)
            val fullName = it.getString(ARG_FULL_NAME)

            binding.tvKarmicByDateValue.text = NumerologyCalculationUtils.calculateKarmicNumber(day, month, year)
            if (fullName != null) {
                binding.tvKarmicByNameValue.text = NumerologyCalculationUtils.calculateKarmicFromName(fullName).joinToString(", ")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

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