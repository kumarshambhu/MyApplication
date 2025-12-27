package com.shambhu.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.shambhu.myapplication.utils.Constants

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    abstract val bindingInflater: (LayoutInflater) -> VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean(Constants.PREFERENCE_THEME, false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        enableEdgeToEdge()
        _binding = bindingInflater.invoke(layoutInflater)
        setContentView(binding.root)
    }

    protected fun setupToolbar(toolbar: Toolbar, title: String, showUpButton: Boolean = false) {
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(showUpButton)
        supportActionBar?.setDisplayShowHomeEnabled(showUpButton)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
