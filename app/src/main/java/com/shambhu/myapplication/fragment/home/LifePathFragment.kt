package com.shambhu.myapplication.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.R

class LifePathFragment : Fragment() {

    private lateinit var tvLifePathNumber: TextView
    private lateinit var tvLifePathInterpretation: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_life_path, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvLifePathNumber = view.findViewById(R.id.tvLifePathNumber)
        tvLifePathInterpretation = view.findViewById(R.id.tvLifePathInterpretation)

        arguments?.let {
            val lifePath = it.getInt(ARG_LIFE_PATH)

            tvLifePathNumber.text = lifePath.toString()

            // Set interpretation
            tvLifePathInterpretation.text = getLifePathDescription(lifePath)
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
        private const val ARG_LIFE_PATH = "lifePath"

        fun newInstance(lifePath: Int): LifePathFragment {
            val fragment = LifePathFragment()
            val args = Bundle()
            args.putInt(ARG_LIFE_PATH, lifePath)
            fragment.arguments = args
            return fragment
        }
    }
}
