package com.shambhu.myapplication.fragment.prediction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.databinding.FragmentKarmicNumberBinding
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DOB
import com.shambhu.myapplication.utils.Constants.Companion.ARG_FULL_NAME

class KarmicNumberFragment : Fragment() {
    private var _binding: FragmentKarmicNumberBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKarmicNumberBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance(
            dob: String, fullName: String
        ): KarmicNumberFragment {
            val fragment = KarmicNumberFragment()
            val args = Bundle()
            args.putString(ARG_DOB, dob)
            args.putString(ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}
