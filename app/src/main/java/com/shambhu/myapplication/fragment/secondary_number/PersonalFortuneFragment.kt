package com.shambhu.myapplication.fragment.secondary_number

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.shambhu.myapplication.R
import com.shambhu.myapplication.adapter.CommonAdapterUtil
import com.shambhu.myapplication.databinding.FragmentPersonalFortuneBinding
import com.shambhu.myapplication.model.PersonalFortuneData
import com.shambhu.myapplication.repository.PersonalFortuneRepository
import com.shambhu.myapplication.repository.impl.PersonalFortuneRepositoryImpl
import com.shambhu.myapplication.service.impl.PersonalFortuneServiceImpl
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DOB
import com.shambhu.myapplication.utils.Constants.Companion.ARG_FULL_NAME
import com.shambhu.myapplication.utils.NumeroCalculator
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PersonalFortuneFragment : Fragment() {

    private var _binding: FragmentPersonalFortuneBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalFortuneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val dob = it.getString(ARG_DOB)
            loadPersonalFortuneData(dob.toString())
        }
    }

    private val personalFortuneRepository: PersonalFortuneRepository by lazy {
        PersonalFortuneRepositoryImpl(PersonalFortuneServiceImpl(requireContext()))
    }

    private fun loadPersonalFortuneData(birthDate: String) {
        val (day, month, year) = CommonUtils.parseDateTriple(birthDate.toString())

        val personalYear = NumerologyCalculationUtils.calculatePersonalYear(day, month)
        val personalMonth = NumerologyCalculationUtils.calculatePersonalMonth(day, month)
        val calculator = NumeroCalculator()
        val personalDay = calculator.calculatePersonalDay(birthDate)


        personalFortuneRepository.getPersonalFortune(personalDay, personalMonth, personalYear)
            .onEach { personalFortuneData ->
                updatePersonalFortuneUi(personalFortuneData)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun updatePersonalFortuneUi(personalFortuneData: PersonalFortuneData) {
        personalFortuneData.personalDay?.let {
            Log.d("Personal Day", Gson().toJson(it))
            binding.personalDayValue.text = it.dayNumber.toString()
            binding.personalDayInterpretation.text = it.description
            binding.personalDayColorValue.text = it.luckyColors.joinToString(", ")
            CommonAdapterUtil.setupNumberBulletRecyclerViewAdapter(requireContext(), binding.personalDaysocialHintsRecyclerView, it.socialHints)

        }
        personalFortuneData.personalMonth?.let {
            Log.d("Personal Month", Gson().toJson(it))
            binding.personalMonthValue.text = it.monthNumber.toString()
            binding.personalMonthInterpretation.text = it.enhancementTips.joinToString("\n")
        }
        personalFortuneData.personalYear?.let {
            Log.d("Personal Year", Gson().toJson(it))
            binding.personalYearValue.text = it.yearNumber.toString()
            binding.personalYearInterpretation.text = it.title
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PersonalFortuneFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DOB, param1)
                    putString(ARG_FULL_NAME, param2)
                }
            }
    }
}