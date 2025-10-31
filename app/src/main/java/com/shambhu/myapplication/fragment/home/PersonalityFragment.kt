package com.shambhu.myapplication.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.R

class PersonalityFragment : Fragment() {

    private lateinit var tvFullName: TextView
    private lateinit var tvDateOfBirth: TextView
    private lateinit var tvTimeOfBirth: TextView
    private lateinit var tvLocation: TextView
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

        tvFullName = view.findViewById(R.id.tvFullName)
        tvDateOfBirth = view.findViewById(R.id.tvDateOfBirth)
        tvTimeOfBirth = view.findViewById(R.id.tvTimeOfBirth)
        tvLocation = view.findViewById(R.id.tvLocation)
        tvPersonalityNumber = view.findViewById(R.id.tvPersonalityNumber)
        tvPersonalityInterpretation = view.findViewById(R.id.tvPersonalityInterpretation)

        arguments?.let {
            val fullName = it.getString(ARG_FULL_NAME) ?: ""
            val dob = it.getString(ARG_DOB) ?: ""
            val time = it.getString(ARG_TIME) ?: ""
            val location = it.getString(ARG_LOCATION) ?: ""
            val personality = it.getInt(ARG_PERSONALITY)

            tvFullName.text = fullName
            tvDateOfBirth.text = dob
            tvTimeOfBirth.text = time
            tvLocation.text = location
            tvPersonalityNumber.text = personality.toString()

            // Set interpretation
            val interpretations = resources.getStringArray(R.array.personality_interpretations)
            if (personality > 0 && personality <= interpretations.size) {
                tvPersonalityInterpretation.text = interpretations[personality - 1]
            }
        }
    }

    companion object {
        private const val ARG_FULL_NAME = "fullName"
        private const val ARG_DOB = "dob"
        private const val ARG_TIME = "time"
        private const val ARG_LOCATION = "location"
        private const val ARG_PERSONALITY = "personality"

        fun newInstance(
            fullName: String,
            dob: String,
            time: String,
            location: String,
            personality: Int
        ): PersonalityFragment {
            val fragment = PersonalityFragment()
            val args = Bundle()
            args.putString(ARG_FULL_NAME, fullName)
            args.putString(ARG_DOB, dob)
            args.putString(ARG_TIME, time)
            args.putString(ARG_LOCATION, location)
            args.putInt(ARG_PERSONALITY, personality)
            fragment.arguments = args
            return fragment
        }
    }
}
