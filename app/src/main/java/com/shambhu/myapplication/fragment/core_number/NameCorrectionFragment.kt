package com.shambhu.myapplication.fragment.core_number

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.databinding.FragmentNameCorrectionBinding
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DOB
import com.shambhu.myapplication.utils.Constants.Companion.ARG_FULL_NAME

class NameCorrectionFragment : Fragment() {
    private var dob: String? = null
    private var firstName: String? = null
    private var lasttName: String? = null

    private var _binding: FragmentNameCorrectionBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dob = it.getString(ARG_DOB)
            //param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNameCorrectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(dob: String, fullName: String) =
            NameCorrectionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DOB, dob)
                    putString(ARG_FULL_NAME, fullName)
                }
            }
    }
}