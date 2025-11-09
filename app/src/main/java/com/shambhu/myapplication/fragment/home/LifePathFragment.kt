package com.shambhu.myapplication.fragment.home

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentBirthNumbersBinding
import com.shambhu.myapplication.databinding.FragmentLifePathBinding
import com.shambhu.myapplication.utils.Constants.Companion.ARG_LIFE_PATH
import com.shambhu.myapplication.utils.Constants.Companion.MY_DATA
import org.json.JSONObject
import java.nio.charset.Charset

class LifePathFragment : Fragment() {

    private var _binding: FragmentLifePathBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLifePathBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val lifePath = it.getInt(ARG_LIFE_PATH)
            binding.tvLifePathNumber.text = lifePath.toString()
            binding.tvLifePathInterpretation.text = getLifePathDescription(lifePath)
        }
    }

    private fun getLifePathDescription(lifePath: Int): String {
        try {
            val inputStream = requireContext().assets.open("life_path_meaning.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, Charsets.UTF_8)
            val jsonObject = org.json.JSONObject(json)
            val lifePathObject = jsonObject.getJSONObject(lifePath.toString())
            return lifePathObject.getString("description")
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ""
        }
    }


    companion object {
        fun newInstance(lifePath: Int): LifePathFragment {
            val fragment = LifePathFragment()
            val args = Bundle()
            args.putInt(ARG_LIFE_PATH, lifePath)
            fragment.arguments = args
            return fragment
        }
    }
}
