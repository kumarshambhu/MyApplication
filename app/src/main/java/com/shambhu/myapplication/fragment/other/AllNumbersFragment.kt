package com.shambhu.myapplication.fragment.other

import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentAllNumbersBinding
import com.shambhu.myapplication.databinding.FragmentNumerologyPlainBinding
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DOB
import com.shambhu.myapplication.utils.Constants.Companion.ARG_FULL_NAME
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import kotlin.toString


class AllNumbersFragment : Fragment() {
    private var _binding: FragmentAllNumbersBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllNumbersBinding.inflate(inflater, container, false)
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

                /*
                Life Path Number(DDMMYYYY)
                Destiny Number(ALL LETTER IN NAME)
                Soul Number(ALL VOWEL IN NAME)
                Personality Number(ALL CONSTANTS IN NAME)
                Maturity Number(LIFE PATH + DESTINY)
                Birth Day Number(DD)
                Current Name Number(CALLING NAME)
                Karmic Lesson Numbers(MISSING NUMBER IN NAME)
                Karmic Debt Numbers
                 */


                val lifePath = NumerologyCalculationUtils.calculateLifePath(day, month, year)
                binding.allNumberLifePathValue.text = lifePath.toString()
                val lifepathInterpretations =
                    resources.getStringArray(R.array.life_path_interpretations)
                binding.allNumberLifePathDescription.setText(lifepathInterpretations[lifePath - 1])

                val destinyNumberValue = NumerologyCalculationUtils.calculateSoulUrge(fullName)
                binding.allNumberDestinyValue.text = destinyNumberValue.toString()
                binding.allNumberDestinyDescription.setText(
                    CommonUtils.getDescriptionFromAssetFile
                        (this.requireContext(), "destiny.json", destinyNumberValue.toString())
                )

                val soulUrgeValue = NumerologyCalculationUtils.calculateSoulUrge(fullName.toString())
                binding.allNumberSoulValue.text = soulUrgeValue.toString()
                val soulUrgeInterpretations = resources.getStringArray(R.array.soul_urge_interpretations)
                binding.allNumberSoulDescription.setText(CommonUtils.getDescriptionFromAssetFile
                    (this.requireContext(), "soul_urge.json", destinyNumberValue.toString()))


                val personalityNumberValue = NumerologyCalculationUtils.calculateSoulUrge(fullName.toString())
                binding.allNumberPersonalityValue.text =  personalityNumberValue.toString()
                val personalityNumberInterpretations = resources.getStringArray(R.array.soul_urge_interpretations)
                binding.allNumberPersonalityDescription.setText(CommonUtils.getDescriptionFromAssetFile
                    (this.requireContext(), "personality.json", personalityNumberValue.toString()))


                val maturity = lifePath + destinyNumberValue
                val birthday = NumerologyCalculationUtils.getBirthdayNumber(day)





                binding.allNumberMaturityValue.text = maturity.toString()
                binding.allNumberBirthDayValue.text = birthday.toString()


            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dob: String, fullName: String): AllNumbersFragment {
            val fragment = AllNumbersFragment()
            val args = Bundle()
            args.putString(ARG_DOB, dob)
            args.putString(ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }


}