package com.shambhu.myapplication.fragment.other

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentJsonBulletBinding
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset
import android.widget.TextView

data class BulletItem(val title: String, val description: String)

class JsonBulletFragment : Fragment() {

    private var _binding: FragmentJsonBulletBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJsonBulletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = loadItemsFromJson()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = BulletListAdapter(items)
    }

    private fun loadItemsFromJson(): List<BulletItem> {
        val items = mutableListOf<BulletItem>()
        try {
            val inputStream = requireContext().assets.open("data.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, Charset.defaultCharset())
            val jsonObject = JSONObject(json)
            val jsonArray = jsonObject.getJSONArray("items")
            for (i in 0 until jsonArray.length()) {
                val itemObject = jsonArray.getJSONObject(i)
                val title = itemObject.getString("title")
                val description = itemObject.getString("description")
                items.add(BulletItem(title, description))
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return items
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dob: String, fullName: String): JsonBulletFragment {
            val fragment = JsonBulletFragment()
            val args = Bundle()
            // We don't need dob and fullName for this fragment, but keeping the signature consistent
            fragment.arguments = args
            return fragment
        }
    }

    private class BulletListAdapter(private val items: List<BulletItem>) :
        RecyclerView.Adapter<BulletListAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_bullet, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.titleTextView.text = item.title
            holder.descriptionTextView.text = item.description
        }

        override fun getItemCount(): Int {
            return items.size
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
            val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        }
    }
}