package com.shambhu.myapplication.fragment.core_number


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentChallengeNumberBinding
import com.shambhu.myapplication.model.ChallengeNumber
import com.shambhu.myapplication.repository.ChallengeNumberRepository
import com.shambhu.myapplication.repository.impl.ChallengeNumberRepositoryImpl
import com.shambhu.myapplication.service.impl.ChallengeNumberServiceImpl
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import android.util.Log
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChallengeNumberFragment : Fragment() {

    private var _binding: FragmentChallengeNumberBinding? = null
    private val binding get() = _binding!!
    private val challengeNumberRepository: ChallengeNumberRepository by lazy {
        ChallengeNumberRepositoryImpl(ChallengeNumberServiceImpl(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChallengeNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val date = CommonUtils.parseDate(it.getString(Constants.ARG_DOB).toString())
            val day = date.dayOfMonth
            val month = date.monthValue
            val year = date.year

            val challengeNumbers = NumerologyCalculationUtils.calculateChallengeNumbers(day, month, year)
            val ageRanges = NumerologyCalculationUtils.calculateChallengeNumberAgeRanges(day, month, year)

            lifecycleScope.launch {
                challengeNumberRepository.getChallengeNumberData()
                    .catch { e ->
                        // Handle error, e.g., show a toast or log the error
                        Log.e("ChallengeNumberFragment", "Error fetching challenge numbers", e)
                    }
                    .collect { challengeNumberData ->
                        val explanations = challengeNumberData.challenge_numbers
                        binding.tvFirstChallengeValue.text = challengeNumbers[0].toString()
                        binding.tvFirstChallengeAgeRange.text = ageRanges[0]
                        binding.tvFirstChallengeExplanation.text = getChallengeExplanation(challengeNumbers[0], explanations)

                        binding.tvSecondChallengeValue.text = challengeNumbers[1].toString()
                        binding.tvSecondChallengeAgeRange.text = ageRanges[1]
                        binding.tvSecondChallengeExplanation.text = getChallengeExplanation(challengeNumbers[1], explanations)

                        binding.tvThirdChallengeValue.text = challengeNumbers[2].toString()
                        binding.tvThirdChallengeAgeRange.text = ageRanges[2]
                        binding.tvThirdChallengeExplanation.text = getChallengeExplanation(challengeNumbers[2], explanations)

                        binding.tvFourthChallengeValue.text = challengeNumbers[3].toString()
                        binding.tvFourthChallengeAgeRange.text = ageRanges[3]
                        binding.tvFourthChallengeExplanation.text = getChallengeExplanation(challengeNumbers[3], explanations)
                    }
            }
        }
    }

    private fun getChallengeExplanation(challengeNumber: Int, explanations: List<ChallengeNumber>): String {
        return explanations.find { it.number == challengeNumber }?.interpretation ?: "No interpretation available."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dob: String, fullName: String): ChallengeNumberFragment {
            val fragment = ChallengeNumberFragment()
            val args = Bundle()
            args.putString(Constants.Companion.ARG_DOB, dob)
            args.putString(Constants.Companion.ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}