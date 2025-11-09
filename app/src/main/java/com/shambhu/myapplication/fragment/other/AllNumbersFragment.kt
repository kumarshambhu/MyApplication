package com.shambhu.myapplication.fragment.other

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentAllNumbersBinding
import com.shambhu.myapplication.databinding.FragmentNumerologyPlainBinding


class AllNumbersFragment : Fragment() {
    private var _binding: FragmentAllNumbersBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllNumbersBinding.inflate(inflater, container, false)



        return binding.root
    }


}