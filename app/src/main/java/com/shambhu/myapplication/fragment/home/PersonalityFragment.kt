package com.shambhu.myapplication.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.R

class PersonalityFragment : Fragment() {

    private lateinit var tvPersonalityNumber: TextView
    private lateinit var tvPersonalityInterpretation: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_personality, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvPersonalityNumber = view.findViewById(R.id.tvPersonalityNumber)
        tvPersonalityInterpretation = view.findViewById(R.id.tvPersonalityInterpretation)

        arguments?.let {
            val personality = it.getInt(ARG_PERSONALITY)

            tvPersonalityNumber.text = personality.toString()

            // Set interpretation
            val interpretations = resources.getStringArray(R.array.personality_interpretations)
            if (personality > 0 && personality <= interpretations.size) {
                tvPersonalityInterpretation.text = interpretations[personality - 1]
            }
        }
    }

    companion object {
        private const val ARG_PERSONALITY = "personality"

        fun newInstance(
            personality: Int
        ): PersonalityFragment {
            val fragment = PersonalityFragment()
            val args = Bundle()
            args.putInt(ARG_PERSONALITY, personality)
            fragment.arguments = args
            return fragment
        }
    }
}
