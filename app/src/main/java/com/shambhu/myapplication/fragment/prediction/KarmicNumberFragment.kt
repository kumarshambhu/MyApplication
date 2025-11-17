package com.shambhu.myapplication.fragment.prediction

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.shambhu.myapplication.adapter.KarmicDebtAdapter
import com.shambhu.myapplication.adapter.KarmicLessonAdapter
import com.shambhu.myapplication.databinding.FragmentKarmicNumberBinding
import com.shambhu.myapplication.model.KarmicAccordionItem
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DOB
import com.shambhu.myapplication.utils.Constants.Companion.ARG_FULL_NAME
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import org.json.JSONObject

class KarmicNumberFragment : Fragment() {
    private var _binding: FragmentKarmicNumberBinding? = null

    private val binding get() = _binding!!
    private lateinit var karmicDebtAdapter: KarmicDebtAdapter
    private lateinit var karmicLessonAdapter: KarmicLessonAdapter

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

    private fun karmicLessonCreation(fullName: String){
        val missing = NumerologyCalculationUtils.calculateKarmicFromName(fullName)
        binding.karmicLessonNumberValue.text = missing.joinToString(", ")

        val karmicLessonsJson = CommonUtils.readAssetFile(requireContext(), "karmic_lesson_debt.json")
        val karmicLessonsObject = JSONObject(karmicLessonsJson).getJSONObject("karmic_lesson")

        val karmicLessons = missing.map { number ->
            val detail = karmicLessonsObject.optString(number.toString(), "No description available.")
            Pair(number.toString(), detail.toString())
        }
        Log.i("karmicLessons","KarmicLessons: $karmicLessons")
        setupKarmicLessonRecyclerView(karmicLessons)
    }

    private fun setupKarmicLessonRecyclerView(karmicLessonNumbers: List<Pair<String, String>>) {
        val accordionItems = karmicLessonNumbers.map { (source, number) ->
            KarmicAccordionItem(source, "Source Empty", number)
        }

        karmicLessonAdapter = KarmicLessonAdapter(accordionItems){ position ->
            // Toggle expansion
            accordionItems[position].isExpanded = !accordionItems[position].isExpanded
            karmicLessonAdapter.notifyItemChanged(position)

        }
        binding.karmicLessonRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.karmicLessonRecyclerView.adapter = karmicLessonAdapter
    }

    private fun karmicDebtCreation(dateOfBirth: String, fullName: String){
        val date = CommonUtils.parseDate(dateOfBirth)
        val day = date.dayOfMonth
        val month = date.monthValue
        val year = date.year

        val karmicDebtNumbers = NumerologyCalculationUtils.calculateKarmicDebtNumbers(day, month, year, fullName)

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
        val interpretations = loadInterpretations()
        val accordionItems = karmicDebtNumbers.map { (source, number) ->
            KarmicAccordionItem("$number", source, interpretations[number.toString()].toString())
        }

        karmicDebtAdapter = KarmicDebtAdapter(accordionItems){ position ->
            // Toggle expansion
            accordionItems[position].isExpanded = !accordionItems[position].isExpanded
            karmicDebtAdapter.notifyItemChanged(position)

        }
        binding.rvKarmicDebt.layoutManager = LinearLayoutManager(context)
        binding.rvKarmicDebt.adapter = karmicDebtAdapter
    }

    private fun loadInterpretations(): Map<String, String> {
        val interpretations = mutableMapOf<String, String>()
        try {
            val inputStream = context?.assets?.open("karmic_lesson_debt.json")
            val json = inputStream?.bufferedReader().use { it?.readText() }
            val jsonObject = JSONObject(json)
            val karmicDebtObject = jsonObject.getJSONObject("karmic_debt")
            val keys = karmicDebtObject.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                interpretations[key] = karmicDebtObject.getString(key)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return interpretations
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance(
            dob: String, fullName: String
        ): KarmicNumberFragment {
            val fragment = KarmicNumberFragment()
            val args = Bundle()
            args.putString(ARG_DOB, dob)
            args.putString(ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}
