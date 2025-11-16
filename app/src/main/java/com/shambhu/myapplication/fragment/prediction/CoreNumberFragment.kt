package com.shambhu.myapplication.fragment.prediction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentCoreNumberBinding
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DOB
import com.shambhu.myapplication.utils.Constants.Companion.ARG_FULL_NAME
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

class CoreNumberFragment : Fragment() {
    private var _binding: FragmentCoreNumberBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoreNumberBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val dob = it.getString(ARG_DOB)
            val fullName = it.getString(ARG_FULL_NAME)

            if (dob != null && fullName != null) {
                val date = CommonUtils.parseDate(dob)
                val day = date.dayOfMonth
                val month = date.monthValue
                val year = date.year

                val birthNumber = NumerologyCalculationUtils.calculateBirthdayNumber(day)
                binding.contentBirthNumber.numberImage.setImageResource(R.drawable.ic_earth)
                binding.contentBirthNumber.numberTitle.text = getString(R.string.birthday_number)
                binding.contentBirthNumber.numberValue.text = birthNumber.toString()
                binding.contentBirthNumber.numberInterpretation.setText(
                    CommonUtils.getDescriptionFromAssetFile
                        (this.requireContext(), "birthday.json", birthNumber.toString())
                )

                binding.contentLifepathNumber.numberWhatSays.text =
                    getString(R.string.birth_day_title)

                val lifePath = NumerologyCalculationUtils.calculateLifePath(day, month, year)
                binding.contentLifepathNumber.numberImage.setImageResource(R.drawable.ic_road)
                binding.contentLifepathNumber.numberTitle.text = getString(R.string.lifepath_number)
                binding.contentLifepathNumber.numberValue.text = lifePath.toString()
                binding.contentLifepathNumber.numberInterpretation.setText(
                    NumerologyCalculationUtils.getLifePathDescription(requireContext(),lifePath))
                binding.contentLifepathNumber.numberWhatSays.text =
                    getString(R.string.lifepath_title)

                val soulNumberValue = NumerologyCalculationUtils.calculateSoulUrge(fullName)
                binding.contentSoulNumber.numberImage.setImageResource(R.drawable.ic_heart)
                binding.contentSoulNumber.numberTitle.text = getString(R.string.soul_urge_number)
                binding.contentSoulNumber.numberValue.text = soulNumberValue.toString()
                binding.contentSoulNumber.numberWhatSays.text = getString(R.string.soul_title)
                binding.contentSoulNumber.numberInterpretation.setText(
                    CommonUtils.getDescriptionFromAssetFile
                        (this.requireContext(), "soul_urge.json", soulNumberValue.toString())
                )


                val personalityNumberValue =
                    NumerologyCalculationUtils.calculatePersonality(fullName)
                binding.contentPersonalityNumber.numberImage.setImageResource(R.drawable.ic_mirrors)
                binding.contentPersonalityNumber.numberTitle.text =
                    getString(R.string.personality_number)
                binding.contentPersonalityNumber.numberValue.text =
                    personalityNumberValue.toString()
                binding.contentPersonalityNumber.numberWhatSays.text =
                    getString(R.string.personality_title)
                binding.contentPersonalityNumber.numberInterpretation.setText(
                    CommonUtils.getDescriptionFromAssetFile
                        (
                        this.requireContext(),
                        "personality.json",
                        personalityNumberValue.toString()
                    )
                )

                val destinyNumberValue = NumerologyCalculationUtils.calculateExpression(fullName)
                binding.contentDestinyNumber.numberImage.setImageResource(R.drawable.ic_mic)
                binding.contentDestinyNumber.numberTitle.text = getString(R.string.destiny_number)
                binding.contentDestinyNumber.numberValue.text = destinyNumberValue.toString()
                binding.contentDestinyNumber.numberWhatSays.text = getString(R.string.destiny_title)
                binding.contentDestinyNumber.numberInterpretation.setText(
                    CommonUtils.getDescriptionFromAssetFile
                        (this.requireContext(), "destiny.json", destinyNumberValue.toString())
                )
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dob: String, fullName: String): CoreNumberFragment {
            val fragment = CoreNumberFragment()
            val args = Bundle()
            args.putString(ARG_DOB, dob)
            args.putString(ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}
