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
            val interpretations = resources.getStringArray(R.array.life_path_interpretations)
            if (lifePath > 0 && lifePath <= interpretations.size) {
                tvLifePathInterpretation.text = interpretations[lifePath - 1]
            }
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
