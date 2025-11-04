package com.shambhu.myapplication.fragment.other

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.databinding.FragmentMultilineTextBinding
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class MultilineTextFragment : Fragment() {

    private var _binding: FragmentMultilineTextBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMultilineTextBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.multilineTextView.text = loadContentFromJson()
    }

    private fun loadContentFromJson(): String {
        var content = ""
        try {
            val inputStream = requireContext().assets.open("multiline_data.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, Charset.defaultCharset())
            val jsonObject = JSONObject(json)
            content = jsonObject.getString("content")
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return content
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dob: String, fullName: String): MultilineTextFragment {
            val fragment = MultilineTextFragment()
            val args = Bundle()
            // We don't need dob and fullName for this fragment, but keeping the signature consistent
            fragment.arguments = args
            return fragment
        }
    }
}