package com.shambhu.myapplication.fragment.other

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentLoshuGridBinding
import com.shambhu.myapplication.databinding.FragmentTestingBinding
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DOB
import com.shambhu.myapplication.utils.Constants.Companion.ARG_FULL_NAME
import com.shambhu.myapplication.utils.NumerologyCalculationUtils


class TestingFragment : Fragment() {
    private var _binding: FragmentTestingBinding? = null
    private val binding get() = _binding!!
    private var fullName: String? = null
    private var dob: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fullName = it.getString(ARG_FULL_NAME)
            dob = it.getString(ARG_DOB)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTestingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("Testing Fragment", "Name: $fullName DOB: $dob")

        val birthDate = CommonUtils.parseDate(dob.toString())
        val birthDay = birthDate.dayOfMonth
        val birthMonth = birthDate.monthValue
        val birthYear = birthDate.year

        val birthdayValue = NumerologyCalculationUtils.getBirthdayNumber(birthDay)
        binding.birthdayNumberKey.text =  getString(R.string.birthday_number) + birthdayValue.toString()
        val dayInterpretations = resources.getStringArray(R.array.birth_day_interpretations)
        binding.birthdayNumberDescription.text = dayInterpretations[ birthdayValue- 1]


        val lifepathValue = NumerologyCalculationUtils.calculateLifePath(birthDay, birthMonth, birthYear)
        binding.lifePathNumberKey.text =  getString(R.string.lifepath_number) + lifepathValue.toString()
        val lifepathInterpretations = resources.getStringArray(R.array.life_path_interpretations)
        binding.lifePathNumberDescription.text = lifepathInterpretations[ lifepathValue- 1]


        val soulUrgeValue = NumerologyCalculationUtils.calculateSoulUrge(fullName.toString())
        binding.soulUrgeKey.text =  getString(R.string.soulurge_number) + soulUrgeValue.toString()
        val soulUrgeInterpretations = resources.getStringArray(R.array.soul_urge_interpretations)
        binding.soulUrgeDescription.text = soulUrgeInterpretations[ soulUrgeValue- 1]


        val personalityNumberValue = NumerologyCalculationUtils.calculateSoulUrge(fullName.toString())
        binding.personalityNumberKey.text =  getString(R.string.personality_number) +  ": " + personalityNumberValue.toString()
        val personalityNumberInterpretations = resources.getStringArray(R.array.soul_urge_interpretations)
        binding.personalityNumberDescription.text = personalityNumberInterpretations[ personalityNumberValue- 1]


        val destinyNumberValue = NumerologyCalculationUtils.calculateSoulUrge(fullName.toString())
        binding.destinyNumberKey.text =  getString(R.string.destiny_number) + ":" + destinyNumberValue.toString()
        val destinyNumberInterpretations = resources.getStringArray(R.array.soul_urge_interpretations)
        binding.destinyNumberDescription.text = destinyNumberInterpretations[ soulUrgeValue- 1]





        Log.i("Testing Fragment", "Birthday Value: $birthdayValue")

    }

    companion object {
        fun newInstance(dob: String, fullName: String) =
            TestingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DOB, dob)
                    putString(ARG_FULL_NAME, fullName)
                }
            }
    }
}