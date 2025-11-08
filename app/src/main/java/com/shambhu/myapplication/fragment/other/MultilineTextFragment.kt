package com.shambhu.myapplication.fragment.other

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.databinding.FragmentMultilineTextBinding
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import org.json.JSONObject
import java.io.IOException

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
            val fullName = it.getString(Constants.ARG_FULL_NAME)

            val elementsJson = CommonUtils.readAssetFile(requireContext(), "elements.json")
            val elementPrediction = elementsJson.let {
                val score = NumerologyCalculationUtils.calculateElements(fullName.toString(), it)
                binding.airElementValue.text = score.get(Constants.Companion.ELEMENT_KEY_AIR).toString()
                binding.earthElementValue.text = score.get(Constants.Companion.ELEMENT_KEY_EARTH).toString()
                binding.fireElementValue.text = score.get(Constants.Companion.ELEMENT_KEY_FIRE).toString()
                binding.waterElementValue.text = score.get(Constants.Companion.ELEMENT_KEY_WATER).toString()
                Log.i("score", score.toString())
                var elementDescription = NumerologyCalculationUtils.calculateElementDescription(score, it)
                binding.elementDescription.text = Html.fromHtml(elementDescription, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }

        }
    }

    private fun loadContentFromJson(): String {
        var content = ""
        try {
            val json = CommonUtils.readAssetFile(requireContext(), "multiline_data.json")
            val jsonObject = JSONObject(json)
            content = jsonObject.getString("content")
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return content
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