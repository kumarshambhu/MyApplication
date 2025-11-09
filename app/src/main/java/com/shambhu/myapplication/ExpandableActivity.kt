package com.shambhu.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shambhu.myapplication.adapter.SampleAdapter
import com.shambhu.myapplication.databinding.ActivityExpandableBinding
import com.shambhu.myapplication.databinding.ActivityMainBinding
import com.shambhu.myapplication.utils.ExpandableTextView

class ExpandableActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpandableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpandableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        initViews()
        setupClickListeners()
        setupRecyclerViewExample()
    }

    private fun initViews() {
       /* binding.expandableText3 = findViewById(R.id.expandableText3)
        binding.expandableText5 = findViewById(R.id.expandableText5)
        binding.btnToggleAll = findViewById(R.id.btnToggleAll)*/

        // Customize programmatically
        binding.expandableText5.setToggleButtonTextColor(getColor(R.color.purple_700))
        binding.expandableText5.setShowMoreText("Show more content")
        binding.expandableText5.setShowLessText("Hide content")

        // Set up expand listener
        binding.expandableText3.setOnExpandListener { isExpanded ->
            val message = if (isExpanded) "Text expanded!" else "Text collapsed!"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupClickListeners() {
        binding.btnToggleAll.setOnClickListener {
            // Toggle all expandable text views
            binding.expandableText3.toggle()
            binding.expandableText5.toggle()
        }
    }

    private fun setupRecyclerViewExample() {
        // This demonstrates how to use ExpandableTextView in RecyclerView
        val sampleData = listOf(
            "First item with short text",
            "Second item with medium text that might need expansion in some cases",
            """Third item with very long text. Lorem ipsum dolor sit amet, consectetur adipiscing elit. 
                Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, 
                quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. 
                Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.""",
            "Fourth item with another long description that needs to be truncated and expanded when user wants to read more content",
            "Fifth short item"
        )

        // If you want to see RecyclerView in action, uncomment below:
         showRecyclerView(sampleData)
    }

    private fun showRecyclerView(data: List<String>) {
        val recyclerView = RecyclerView(this).apply {
            layoutManager = LinearLayoutManager(this@ExpandableActivity)
            adapter = SampleAdapter(data)
        }

        //findViewById<LinearLayout>(R.id.mainLayout).addView(recyclerView)
    }
}