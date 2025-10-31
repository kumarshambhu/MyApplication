package com.shambhu.myapplication.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.R

class ExpressionFragment : Fragment() {

    private lateinit var tvFullName: TextView
    private lateinit var tvDateOfBirth: TextView
    private lateinit var tvTimeOfBirth: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvExpressionNumber: TextView
    private lateinit var tvExpressionInterpretation: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_expression, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvFullName = view.findViewById(R.id.tvFullName)
        tvDateOfBirth = view.findViewById(R.id.tvDateOfBirth)
        tvTimeOfBirth = view.findViewById(R.id.tvTimeOfBirth)
        tvLocation = view.findViewById(R.id.tvLocation)
        tvExpressionNumber = view.findViewById(R.id.tvExpressionNumber)
        tvExpressionInterpretation = view.findViewById(R.id.tvExpressionInterpretation)
    }

    fun updateData(fullName: String, dob: String, time: String, location: String, expression: Int) {
        tvFullName.text = fullName
        tvDateOfBirth.text = dob
        tvTimeOfBirth.text = time
        tvLocation.text = location
        tvExpressionNumber.text = expression.toString()

        // Set interpretation
        val interpretations = resources.getStringArray(R.array.expression_interpretations)
        tvExpressionInterpretation.text = interpretations[expression - 1]
    }
}
