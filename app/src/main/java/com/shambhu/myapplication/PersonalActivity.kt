package com.shambhu.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.shambhu.myapplication.databinding.ActivityPersonalBinding

class PersonalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val adapter = PersonalPagerAdapter(this)
        binding.content.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabs, binding.content.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Month"
                1 -> "Year"
                else -> null
            }
        }.attach()
    }
}
