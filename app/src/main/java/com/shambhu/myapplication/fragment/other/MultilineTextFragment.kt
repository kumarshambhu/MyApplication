package com.shambhu.myapplication.fragment.other

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.databinding.FragmentMultilineTextBinding
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
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
            var result = CommonUtils.nameToIntArray(fullName.toString())
            var numberList = result.toList();
            //println(result.joinToString(", "))
            var elementArray =  arrayOfNulls<String> (numberList.size)
            val json = CommonUtils.readAssetFile(requireContext(), "elements.json")
            val jsonObject = JSONObject(json)
            val jsonArray = jsonObject.getJSONObject("element")
            for (i in 0 until numberList.size) {
                val element = jsonArray.getString(result[i].toString())
                Log.i("ELEMENT", element.toString())
                elementArray[i] = element
            }
            val sorted = elementArray.toList().groupingBy { it }.eachCount()

            sorted.forEach { (data, count) ->
                println("$data -> $count times")
            }

        }

        binding.multilineTextView.text = loadContentFromJson()
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