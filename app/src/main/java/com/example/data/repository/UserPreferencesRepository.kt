package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.Language
import com.example.data.model.MizajType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPreferencesRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("uyghur_tibabiti_prefs", Context.MODE_PRIVATE)

    private val _currentLanguage = MutableStateFlow(
        Language.entries.find { it.code == prefs.getString("KEY_LANG", Language.UYGHUR.code) } ?: Language.UYGHUR
    )
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    private val _isDarkMode = MutableStateFlow(prefs.getBoolean("KEY_DARK_MODE", false))
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _fontSizeMultiplier = MutableStateFlow(prefs.getFloat("KEY_FONT_SCALE", 1.0f))
    val fontSizeMultiplier: StateFlow<Float> = _fontSizeMultiplier.asStateFlow()

    private val _favoriteIds = MutableStateFlow(
        prefs.getStringSet("KEY_FAVORITES", emptySet())?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
    )
    val favoriteIds: StateFlow<Set<Int>> = _favoriteIds.asStateFlow()

    private val _userMizaj = MutableStateFlow<MizajType?>(
        prefs.getString("KEY_USER_MIZAJ", null)?.let { name -> MizajType.entries.find { it.name == name } }
    )
    val userMizaj: StateFlow<MizajType?> = _userMizaj.asStateFlow()

    fun setLanguage(language: Language) {
        prefs.edit().putString("KEY_LANG", language.code).apply()
        _currentLanguage.value = language
    }

    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean("KEY_DARK_MODE", enabled).apply()
        _isDarkMode.value = enabled
    }

    fun setFontScale(scale: Float) {
        prefs.edit().putFloat("KEY_FONT_SCALE", scale).apply()
        _fontSizeMultiplier.value = scale
    }

    fun toggleFavorite(plantId: Int) {
        val current = _favoriteIds.value.toMutableSet()
        if (current.contains(plantId)) {
            current.remove(plantId)
        } else {
            current.add(plantId)
        }
        prefs.edit().putStringSet("KEY_FAVORITES", current.map { it.toString() }.toSet()).apply()
        _favoriteIds.value = current
    }

    fun setUserMizaj(mizajType: MizajType) {
        prefs.edit().putString("KEY_USER_MIZAJ", mizajType.name).apply()
        _userMizaj.value = mizajType
    }

    fun clearUserMizaj() {
        prefs.edit().remove("KEY_USER_MIZAJ").apply()
        _userMizaj.value = null
    }

    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    fun getAdminPassword(): String {
        return prefs.getString("KEY_ADMIN_PASSWORD", "123456") ?: "123456"
    }

    fun loginAdmin(password: String): Boolean {
        if (password == getAdminPassword()) {
            _isAdminLoggedIn.value = true
            return true
        }
        return false
    }

    fun logoutAdmin() {
        _isAdminLoggedIn.value = false
    }

    fun changeAdminPassword(oldPass: String, newPass: String): Boolean {
        if (oldPass == getAdminPassword() && newPass.isNotBlank()) {
            prefs.edit().putString("KEY_ADMIN_PASSWORD", newPass).apply()
            return true
        }
        return false
    }

    fun setUpdateDismissed(versionCode: Int) {
        prefs.edit()
            .putInt("KEY_UPDATE_DISMISSED_CODE", versionCode)
            .putLong("KEY_UPDATE_DISMISSED_TIME", System.currentTimeMillis())
            .apply()
    }

    fun isUpdateDismissedRecently(versionCode: Int): Boolean {
        val dismissedCode = prefs.getInt("KEY_UPDATE_DISMISSED_CODE", -1)
        val dismissedTime = prefs.getLong("KEY_UPDATE_DISMISSED_TIME", 0L)
        val oneDayMillis = 24 * 60 * 60 * 1000L
        return dismissedCode == versionCode && (System.currentTimeMillis() - dismissedTime) < oneDayMillis
    }
}
