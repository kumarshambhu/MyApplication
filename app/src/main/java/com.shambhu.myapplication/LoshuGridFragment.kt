package com.shambhu.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.databinding.FragmentLoshuGridBinding

class LoshuGridFragment : Fragment() {

    private var _binding: FragmentLoshuGridBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoshuGridBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // You can add any logic here that needs to be executed when the view is created.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_DOB = "dob"

        fun newInstance(dob: String): LoshuGridFragment {
            val fragment = LoshuGridFragment()
            val args = Bundle()
            args.putString(ARG_DOB, dob)
            fragment.arguments = args
            return fragment
        }
    }
}