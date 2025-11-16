package com.shambhu.myapplication.fragment.prediction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.shambhu.myapplication.adapter.KarmicLessonAdapter
import com.shambhu.myapplication.databinding.FragmentKarmicNumberBinding
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DOB
import com.shambhu.myapplication.utils.Constants.Companion.ARG_FULL_NAME
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import org.json.JSONObject

class KarmicNumberFragment : Fragment() {
    private var _binding: FragmentKarmicNumberBinding? = null

    private val binding get() = _binding!!

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
            updateKarmicNumber(fullName.toString())
        }
    }


    private fun updateKarmicNumber(fullName: String) {
        val missing = NumerologyCalculationUtils.calculateKarmicFromName(fullName)
        binding.karmicLessonNumberValue.text = missing.joinToString(", ")

        val karmicLessonsJson = CommonUtils.readAssetFile(requireContext(), "karmic_lesson_debt.json")
        val karmicLessonsObject = JSONObject(karmicLessonsJson).getJSONObject("karmic_lesson")

        val karmicLessons = missing.map { number ->
            val detail = karmicLessonsObject.optString(number.toString(), "No description available.")
            Pair(number.toString(), detail)
        }

        binding.karmicLessonRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.karmicLessonRecyclerView.adapter = KarmicLessonAdapter(karmicLessons)
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
