package com.shambhu.myapplication.fragment.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.R

class SoulUrgeFragment : Fragment() {

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

        tvSoulUrgeNumber = view.findViewById(R.id.tvSoulUrgeNumber)
        tvSoulUrgeInterpretation = view.findViewById(R.id.tvSoulUrgeInterpretation)

        arguments?.let {
            val soulUrge = it.getInt(ARG_SOUL_URGE)

            tvSoulUrgeNumber.text = soulUrge.toString()

            // Set interpretation
            val interpretations = resources.getStringArray(R.array.soul_urge_interpretations)
            if (soulUrge > 0 && soulUrge <= interpretations.size) {
                tvSoulUrgeInterpretation.text = interpretations[soulUrge - 1]
            }
        }
    }

    companion object {
        private const val ARG_SOUL_URGE = "soulUrge"

        fun newInstance(
            soulUrge: Int
        ): SoulUrgeFragment {
            val fragment = SoulUrgeFragment()
            val args = Bundle()
            args.putInt(ARG_SOUL_URGE, soulUrge)
            fragment.arguments = args
            return fragment
        }
    }
}
