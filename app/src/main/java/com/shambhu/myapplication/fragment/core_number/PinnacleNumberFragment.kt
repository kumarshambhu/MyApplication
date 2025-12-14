package com.shambhu.myapplication.fragment.core_number

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentPinnacleNumberBinding
import com.shambhu.myapplication.model.PinnacleNumber
import com.shambhu.myapplication.repository.PinnacleNumberRepository
import com.shambhu.myapplication.repository.impl.PinnacleNumberRepositoryImpl
import com.shambhu.myapplication.service.impl.PinnacleNumberServiceImpl
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import android.util.Log
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PinnacleNumberFragment : Fragment() {
    private var _binding: FragmentPinnacleNumberBinding? = null
    private val binding get() = _binding!!
    private val pinnacleNumberRepository: PinnacleNumberRepository by lazy {
        PinnacleNumberRepositoryImpl(PinnacleNumberServiceImpl(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPinnacleNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val date = CommonUtils.parseDate(it.getString(Constants.ARG_DOB).toString())
            val day = date.dayOfMonth
            val month = date.monthValue
            val year = date.year

            val pinnacleNumbers = NumerologyCalculationUtils.calculatePinnacleNumbers(day, month, year)
            val ageRanges = NumerologyCalculationUtils.calculatePinnacleNumberAgeRanges(day, month, year)

            lifecycleScope.launch {
                pinnacleNumberRepository.getPinnacleNumberData()
                    .catch { e ->
                        // Handle error, e.g., show a toast or log the error
                        Log.e("PinnacleNumberFragment", "Error fetching pinnacle numbers", e)
                    }
                    .collect { pinnacleNumberData ->
                        val explanations = pinnacleNumberData.pinnacle_numbers
                        binding.tvFirstPinnacleValue.text = pinnacleNumbers[0].toString()
                        binding.tvFirstPinnacleAgeRange.text = ageRanges[0]
                        binding.tvFirstPinnacleExplanation.text = getPinnacleExplanation(pinnacleNumbers[0], explanations)

                        binding.tvSecondPinnacleValue.text = pinnacleNumbers[1].toString()
                        binding.tvSecondPinnacleAgeRange.text = ageRanges[1]
                        binding.tvSecondPinnacleExplanation.text = getPinnacleExplanation(pinnacleNumbers[1], explanations)

                        binding.tvThirdPinnacleValue.text = pinnacleNumbers[2].toString()
                        binding.tvThirdPinnacleAgeRange.text = ageRanges[2]
                        binding.tvThirdPinnacleExplanation.text = getPinnacleExplanation(pinnacleNumbers[2], explanations)

                        binding.tvFourthPinnacleValue.text = pinnacleNumbers[3].toString()
                        binding.tvFourthPinnacleAgeRange.text = ageRanges[3]
                        binding.tvFourthPinnacleExplanation.text = getPinnacleExplanation(pinnacleNumbers[3], explanations)
                    }
            }
        }
    }

    private fun getPinnacleExplanation(pinnacleNumber: Int, explanations: List<PinnacleNumber>): String {
        return explanations.find { it.number == pinnacleNumber }?.interpretation ?: "No interpretation available."
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dob: String, fullName: String): PinnacleNumberFragment {
            val fragment = PinnacleNumberFragment()
            val args = Bundle()
            args.putString(Constants.Companion.ARG_DOB, dob)
            args.putString(Constants.Companion.ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}