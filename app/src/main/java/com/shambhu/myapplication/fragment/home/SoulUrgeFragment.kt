package com.shambhu.myapplication.fragment.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.R

class SoulUrgeFragment : Fragment() {

    private lateinit var tvFullName: TextView
    private lateinit var tvDateOfBirth: TextView
    private lateinit var tvTimeOfBirth: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvSoulUrgeNumber: TextView
    private lateinit var tvSoulUrgeInterpretation: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_soul_urge, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvFullName = view.findViewById(R.id.tvFullName)
        tvDateOfBirth = view.findViewById(R.id.tvDateOfBirth)
        tvTimeOfBirth = view.findViewById(R.id.tvTimeOfBirth)
        tvLocation = view.findViewById(R.id.tvLocation)
        tvSoulUrgeNumber = view.findViewById(R.id.tvSoulUrgeNumber)
        tvSoulUrgeInterpretation = view.findViewById(R.id.tvSoulUrgeInterpretation)
    }

    fun updateData(fullName: String, dob: String, time: String, location: String, soulUrge: Int) {
        tvFullName.text = fullName
        tvDateOfBirth.text = dob
        tvTimeOfBirth.text = time
        tvLocation.text = location
        tvSoulUrgeNumber.text = soulUrge.toString()

        // Set interpretation
        val interpretations = resources.getStringArray(R.array.soul_urge_interpretations)
        tvSoulUrgeInterpretation.text = interpretations[soulUrge - 1]
    }
}
