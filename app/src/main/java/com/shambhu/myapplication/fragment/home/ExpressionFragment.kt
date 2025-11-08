package com.shambhu.myapplication.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.R
import com.shambhu.myapplication.utils.Constants.Companion.ARG_EXPRESSION

class ExpressionFragment : Fragment() {

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

        tvExpressionNumber = view.findViewById(R.id.tvExpressionNumber)
        tvExpressionInterpretation = view.findViewById(R.id.tvExpressionInterpretation)

        arguments?.let {
            val expression = it.getInt(ARG_EXPRESSION)

            tvExpressionNumber.text = expression.toString()

            // Set interpretation
            val interpretations = resources.getStringArray(R.array.expression_interpretations)
            if (expression > 0 && expression <= interpretations.size) {
                tvExpressionInterpretation.text = interpretations[expression - 1]
            }
        }
    }

    companion object {

        fun newInstance(expression: Int): ExpressionFragment {
            val fragment = ExpressionFragment()
            val args = Bundle()
            args.putInt(ARG_EXPRESSION, expression)
            fragment.arguments = args
            return fragment
        }
    }
}
