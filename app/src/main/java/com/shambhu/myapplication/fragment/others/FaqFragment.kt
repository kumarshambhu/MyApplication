package com.shambhu.myapplication.fragment.others

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shambhu.myapplication.adapter.FaqAdapter
import com.shambhu.myapplication.databinding.FragmentFaqBinding
import com.shambhu.myapplication.model.FaqItem
import com.shambhu.myapplication.utils.CommonUtils
import org.json.JSONObject

class FaqFragment : Fragment() {

    private var _binding: FragmentFaqBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaqBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val faqJson = CommonUtils.readAssetFile(requireContext(), "faq.json")
        val faqList = parseFaqs(faqJson)
        val adapter = FaqAdapter(faqList)
        binding.faqRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.faqRecyclerView.adapter = adapter
    }

    private fun parseFaqs(jsonString: String): List<FaqItem> {
        val jsonObject = JSONObject(jsonString)
        val faqsArray = jsonObject.getJSONArray("faqs")
        val gson = Gson()
        val faqListType = object : TypeToken<List<FaqItem>>() {}.type
        return gson.fromJson(faqsArray.toString(), faqListType)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FaqFragment()
    }
}