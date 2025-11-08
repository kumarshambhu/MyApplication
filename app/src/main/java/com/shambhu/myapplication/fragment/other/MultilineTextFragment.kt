package com.shambhu.myapplication.fragment.other

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.shambhu.myapplication.adapter.ColorAdapter
import com.shambhu.myapplication.databinding.FragmentMultilineTextBinding
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import org.json.JSONObject

class MultilineTextFragment : Fragment() {

    private var _binding: FragmentMultilineTextBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMultilineTextBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val fullName = it.getString(Constants.ARG_FULL_NAME) ?: return
            val colorNumbers = NumerologyCalculationUtils.nameToColorNumbers(fullName)
            val colorsJson = CommonUtils.loadJSONFromAsset(requireContext(), "colors.json") ?: return
            val colorsObject = JSONObject(colorsJson).getJSONObject("color_by_number")

            val colorCounts = colorNumbers.groupingBy { it }.eachCount()
            val colorsToShow = colorCounts.mapNotNull { (number, count) ->
                colorsObject.optJSONObject(number.toString())?.let { it to count }
            }

            binding.colorsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.colorsRecyclerView.adapter = ColorAdapter(colorsToShow)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dob: String, fullName: String): MultilineTextFragment {
            val fragment = MultilineTextFragment()
            val args = Bundle()
            args.putString(Constants.ARG_DOB, dob)
            args.putString(Constants.ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}