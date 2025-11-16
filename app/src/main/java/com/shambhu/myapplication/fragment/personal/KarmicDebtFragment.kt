package com.shambhu.myapplication.fragment.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.shambhu.myapplication.adapter.KarmicDebtAdapter
import com.shambhu.myapplication.databinding.FragmentKarmicDebtBinding
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import org.json.JSONObject

class KarmicDebtFragment : Fragment() {

    private var _binding: FragmentKarmicDebtBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKarmicDebtBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val day = it.getInt(Constants.ARG_DAY)
            val month = it.getInt(Constants.ARG_MONTH)
            val year = it.getInt(Constants.ARG_YEAR)
            val fullName = it.getString(Constants.ARG_FULL_NAME) ?: ""

            val karmicDebtNumbers = NumerologyCalculationUtils.calculateKarmicDebtNumbers(day, month, year, fullName)

            if (karmicDebtNumbers.isEmpty()) {
                binding.tvNoKarmicDebt.visibility = View.VISIBLE
                binding.rvKarmicDebt.visibility = View.GONE
            } else {
                binding.tvNoKarmicDebt.visibility = View.GONE
                binding.rvKarmicDebt.visibility = View.VISIBLE
                setupRecyclerView(karmicDebtNumbers)
            }
        }
    }

    private fun setupRecyclerView(karmicDebtNumbers: List<Pair<String, Int>>) {
        val interpretations = loadInterpretations()
        val adapter = KarmicDebtAdapter(karmicDebtNumbers, interpretations)
        binding.rvKarmicDebt.layoutManager = LinearLayoutManager(context)
        binding.rvKarmicDebt.adapter = adapter
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
        fun newInstance(day: Int, month: Int, year: Int, fullName: String): KarmicDebtFragment {
            val fragment = KarmicDebtFragment()
            val args = Bundle()
            args.putInt(Constants.ARG_DAY, day)
            args.putInt(Constants.ARG_MONTH, month)
            args.putInt(Constants.ARG_YEAR, year)
            args.putString(Constants.ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}
