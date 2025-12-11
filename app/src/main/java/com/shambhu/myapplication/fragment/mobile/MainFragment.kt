package com.shambhu.myapplication.fragment.mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.shambhu.myapplication.databinding.FragmentMainBinding
import java.util.*

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCalculate.setOnClickListener {
            val dob = binding.etDob.text.toString().trim()
            val mobile = binding.etMobile.text.toString().trim()

            if (dob.isEmpty()) {
                binding.etDob.error = "Please enter Date of Birth"
                return@setOnClickListener
            }

            if (mobile.isEmpty()) {
                binding.etMobile.error = "Please enter Mobile Number"
                return@setOnClickListener
            }

            // Navigate to results with data
            val action = MainFragmentDirections.actionMainFragmentToResultsFragment(
                dob = dob,
                mobile = mobile
            )
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}