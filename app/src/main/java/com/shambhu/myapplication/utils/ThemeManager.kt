package com.shambhu.myapplication.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

object ThemeManager {

    private const val THEME_PREFS = "theme_preferences"
    private const val KEY_THEME = "app_theme"
    private const val KEY_DARK_MODE = "dark_mode"

    enum class AppTheme(val value: String) {
        LIGHT("light"),
        DARK("dark"),
        SYSTEM("system"),
        BATTERY("battery")
    }

    fun applyTheme(context: Context) {
        val prefs = getPreferences(context)
        val theme = prefs.getString(KEY_THEME, AppTheme.SYSTEM.value)

        when (theme) {
            AppTheme.LIGHT.value -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            AppTheme.DARK.value -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            AppTheme.SYSTEM.value -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            AppTheme.BATTERY.value -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
        }
    }

    fun setTheme(context: Context, theme: AppTheme) {
        getPreferences(context).edit().putString(KEY_THEME, theme.value).apply()
    }

    fun getCurrentTheme(context: Context): AppTheme {
        val themeValue = getPreferences(context).getString(KEY_THEME, AppTheme.SYSTEM.value)
        return AppTheme.values().find { it.value == themeValue } ?: AppTheme.SYSTEM
    }

    fun setDarkMode(context: Context, isDarkMode: Boolean) {
        val theme = if (isDarkMode) AppTheme.DARK else AppTheme.LIGHT
        setTheme(context, theme)
    }

    fun isDarkMode(context: Context): Boolean {
        return getCurrentTheme(context) == AppTheme.DARK
    }

    fun toggleTheme(context: Context) {
        val currentTheme = getCurrentTheme(context)
        val newTheme = when (currentTheme) {
            AppTheme.LIGHT -> AppTheme.DARK
            AppTheme.DARK -> AppTheme.LIGHT
            else -> if (isSystemInDarkMode(context)) AppTheme.LIGHT else AppTheme.DARK
        }
        setTheme(context, newTheme)
    }

    private fun isSystemInDarkMode(context: Context): Boolean {
        val nightMode = context.resources.configuration.uiMode and
                android.content.res.Configuration.UI_MODE_NIGHT_MASK
        return nightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES
    }

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE)
    }
}
