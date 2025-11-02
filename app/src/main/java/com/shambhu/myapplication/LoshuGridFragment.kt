package com.shambhu.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.databinding.FragmentLoshuGridBinding
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

class LoshuGridFragment : Fragment() {

    private var _binding: FragmentLoshuGridBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoshuGridBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString(ARG_DOB)?.let { dob ->
            val digits = dob.filter { it.isDigit() }
            val numberCounts = IntArray(10)
            for (digitChar in digits) {
                val digit = digitChar.toString().toInt()
                numberCounts[digit]++
            }

            updateCell(binding.cell1, 1, numberCounts[1])
            updateCell(binding.cell2, 2, numberCounts[2])
            updateCell(binding.cell3, 3, numberCounts[3])
            updateCell(binding.cell4, 4, numberCounts[4])
            updateCell(binding.cell5, 5, numberCounts[5])
            updateCell(binding.cell6, 6, numberCounts[6])
            updateCell(binding.cell7, 7, numberCounts[7])
            updateCell(binding.cell8, 8, numberCounts[8])
            updateCell(binding.cell9, 9, numberCounts[9])
        }

        arguments?.getString(ARG_FULL_NAME)?.let { fullName ->
            val personalityNumber = NumerologyCalculationUtils.calculatePersonality(fullName)
            val destinyNumber = NumerologyCalculationUtils.calculateExpression(fullName)

            binding.tvPersonalityNumber.text = personalityNumber.toString()
            binding.tvDestinyNumber.text = destinyNumber.toString()
        }
    }

    private fun updateCell(textView: TextView, number: Int, count: Int) {
        if (count > 0) {
            textView.text = number.toString().repeat(count)
        } else {
            textView.text = ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_DOB = "dob"
        private const val ARG_FULL_NAME = "fullName"

        fun newInstance(dob: String, fullName: String): LoshuGridFragment {
            val fragment = LoshuGridFragment()
            val args = Bundle()
            args.putString(ARG_DOB, dob)
            args.putString(ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}