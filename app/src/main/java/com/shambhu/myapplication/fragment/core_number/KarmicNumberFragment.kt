package com.shambhu.myapplication.fragment.core_number

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.shambhu.myapplication.adapter.KarmicDebtRecyclerViewAdapter
import com.shambhu.myapplication.adapter.KarmicLessonRecyclerViewAdapter
import com.shambhu.myapplication.databinding.FragmentKarmicNumberBinding
import com.shambhu.myapplication.model.KarmicAccordionItem
import com.shambhu.myapplication.model.KarmicDebt
import com.shambhu.myapplication.model.KarmicDebtResponse
import com.shambhu.myapplication.service.NumerologyService
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DOB
import com.shambhu.myapplication.utils.Constants.Companion.ARG_FULL_NAME
import org.json.JSONObject
import kotlin.String
import kotlin.collections.List

class KarmicNumberFragment : Fragment() {
    private var _binding: FragmentKarmicNumberBinding? = null

    private val binding get() = _binding!!
    private lateinit var karmicDebtRecyclerViewAdapter: KarmicDebtRecyclerViewAdapter
    private lateinit var karmicLessonAdapter: KarmicLessonRecyclerViewAdapter
    private lateinit var numerologyService: NumerologyService

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
            val dob = it.getString(Constants.ARG_DOB)
            val fullName = it.getString(Constants.ARG_FULL_NAME)
            updateKarmicNumber(fullName.toString(), dob.toString())
        }
    }


    private fun updateKarmicNumber(fullName: String, dateOfBirth: String) {

        karmicLessonCreation(fullName)
        karmicDebtCreation(dateOfBirth, fullName)

    }

    private fun karmicLessonCreation(fullName: String) {
        val missing = numerologyService.calculateKarmicFromName(fullName)
        binding.karmicLessonNumberValue.text = missing.joinToString(", ")

        val karmicLessonsJson = numerologyService.getKarmicLessonDebtJson()
        val karmicLessonsObject = JSONObject(karmicLessonsJson).getJSONObject("karmic_lesson")

        val karmicLessons = missing.map { number ->
            val detail =
                karmicLessonsObject.optString(number.toString(), "No description available.")
            Pair(number.toString(), detail.toString())
        }
        Log.i("karmicLessons", "KarmicLessons: $karmicLessons")
        setupKarmicLessonRecyclerView(karmicLessons)
    }

    private fun setupKarmicLessonRecyclerView(karmicLessonNumbers: List<Pair<String, String>>) {
        val accordionItems = karmicLessonNumbers.map { (source, number) ->
            KarmicAccordionItem(source, "Source Empty", number)
        }

        karmicLessonAdapter = KarmicLessonRecyclerViewAdapter(accordionItems) { position ->
            // Toggle expansion
            accordionItems[position].isExpanded = !accordionItems[position].isExpanded
            karmicLessonAdapter.notifyItemChanged(position)

        }
        binding.karmicLessonRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.karmicLessonRecyclerView.adapter = karmicLessonAdapter
    }

    private fun karmicDebtCreation(dateOfBirth: String, fullName: String) {
        val date = CommonUtils.parseDate(dateOfBirth)
        val day = date.dayOfMonth
        val month = date.monthValue
        val year = date.year

        val karmicDebtNumbers =
            numerologyService.calculateKarmicDebtNumbers(day, month, year, fullName)

        if (karmicDebtNumbers.isEmpty()) {
            binding.tvNoKarmicDebt.visibility = View.VISIBLE
            binding.rvKarmicDebt.visibility = View.GONE
        } else {
            binding.tvNoKarmicDebt.visibility = View.GONE
            binding.rvKarmicDebt.visibility = View.VISIBLE
            setupKarmicDebtRecyclerView(karmicDebtNumbers)
        }
    }

    private fun setupKarmicDebtRecyclerView(karmicDebtNumbers: List<Pair<String, Int>>) {
        val data = Gson().fromJson(
            numerologyService.getKarmicLessonDebtJson(),
            KarmicDebtResponse::class.java
        )
        val data1 = data.karmic_debt

        val accordionItems = karmicDebtNumbers.map { (source, number) ->
            val data2 = data1.get(number.toString())
            KarmicDebt(
                number = number.toString(), source = source,
                challengesAndProblems = data2?.challengesAndProblems,
                qualities = data2?.qualities,
                keysToOvercome = data2?.keysToOvercome,
                summary = data2?.summary,
                potentialOutcome = data2?.potentialOutcome, isExpanded = false
            )
        }

        karmicDebtRecyclerViewAdapter = KarmicDebtRecyclerViewAdapter(requireContext(),accordionItems) { position ->
            // Toggle expansion
            accordionItems[position].isExpanded = !accordionItems[position].isExpanded
            karmicDebtRecyclerViewAdapter.notifyItemChanged(position)

        }
        binding.rvKarmicDebt.layoutManager = LinearLayoutManager(context)
        binding.rvKarmicDebt.adapter = karmicDebtRecyclerViewAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance(
            dob: String, fullName: String, numerologyService: NumerologyService
        ): KarmicNumberFragment {
            val fragment = KarmicNumberFragment()
            fragment.numerologyService = numerologyService
            val args = Bundle()
            args.putString(ARG_DOB, dob)
            args.putString(ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}
