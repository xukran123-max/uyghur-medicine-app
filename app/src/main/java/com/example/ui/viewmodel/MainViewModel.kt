package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Language
import com.example.data.model.MedicinalPlant
import com.example.data.model.MizajType
import com.example.data.model.PlantCategory
import com.example.data.model.PlantRepository
import com.example.data.model.TraditionalProperty
import com.example.data.repository.UserPreferencesRepository
import com.example.data.update.AppUpdateChecker
import com.example.data.update.AppUpdateInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenTab {
    HOME, CATALOG, ASSISTANT, CALENDAR, MIZAJ_QUIZ, FAVORITES, SETTINGS
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val userPrefsRepo = UserPreferencesRepository(application)
    private val plantsStorageRepo = com.example.data.repository.PlantsStorageRepository(application)

    val currentLanguage: StateFlow<Language> = userPrefsRepo.currentLanguage
    val isDarkMode: StateFlow<Boolean> = userPrefsRepo.isDarkMode
    val fontSizeMultiplier: StateFlow<Float> = userPrefsRepo.fontSizeMultiplier
    val favoriteIds: StateFlow<Set<Int>> = userPrefsRepo.favoriteIds
    val userMizaj: StateFlow<MizajType?> = userPrefsRepo.userMizaj

    val isAdminLoggedIn: StateFlow<Boolean> = userPrefsRepo.isAdminLoggedIn

    fun loginAdmin(password: String): Boolean = userPrefsRepo.loginAdmin(password)
    fun logoutAdmin() = userPrefsRepo.logoutAdmin()
    fun changeAdminPassword(oldPass: String, newPass: String): Boolean =
        userPrefsRepo.changeAdminPassword(oldPass, newPass)

    private val _allPlants = MutableStateFlow<List<MedicinalPlant>>(plantsStorageRepo.getPlants())
    val allPlants: StateFlow<List<MedicinalPlant>> = _allPlants.asStateFlow()

    fun savePlant(plant: MedicinalPlant) {
        val updated = plantsStorageRepo.savePlant(plant)
        _allPlants.value = updated
        if (_selectedPlant.value?.id == plant.id) {
            _selectedPlant.value = updated.find { it.id == plant.id } ?: plant
        }
        if (isAdminLoggedIn.value) {
            viewModelScope.launch {
                plantsStorageRepo.pushToServer(updated, userPrefsRepo.getAdminPassword())
            }
        }
    }

    fun deletePlant(plantId: Int) {
        val updated = plantsStorageRepo.deletePlant(plantId)
        _allPlants.value = updated
        if (_selectedPlant.value?.id == plantId) {
            _selectedPlant.value = null
        }
        if (isAdminLoggedIn.value) {
            viewModelScope.launch {
                plantsStorageRepo.pushToServer(updated, userPrefsRepo.getAdminPassword())
            }
        }
    }

    fun resetPlantsToDefault() {
        val reset = plantsStorageRepo.resetToDefaults()
        _allPlants.value = reset
        _selectedPlant.value = null
        if (isAdminLoggedIn.value) {
            viewModelScope.launch {
                plantsStorageRepo.pushToServer(reset, userPrefsRepo.getAdminPassword())
            }
        }
    }

    private val _appUpdateInfo = MutableStateFlow<AppUpdateInfo?>(null)
    val appUpdateInfo: StateFlow<AppUpdateInfo?> = _appUpdateInfo.asStateFlow()

    private val _showUpdateDialog = MutableStateFlow(false)
    val showUpdateDialog: StateFlow<Boolean> = _showUpdateDialog.asStateFlow()

    init {
        checkForUpdates()
        syncPlantsFromServer()
        checkNotifications()
    }

    fun checkNotifications() {
        viewModelScope.launch {
            com.example.data.notification.NotificationSyncManager.checkAndNotify(getApplication())
        }
    }

    fun syncPlantsFromServer() {
        viewModelScope.launch {
            val serverPlants = plantsStorageRepo.syncWithServer()
            if (serverPlants != null && serverPlants.isNotEmpty()) {
                _allPlants.value = serverPlants
            }
        }
    }

    fun checkForUpdates() {
        viewModelScope.launch {
            val info = AppUpdateChecker.checkUpdate(currentLanguage.value)
            if (info != null && info.shouldUpdate) {
                _appUpdateInfo.value = info
                _showUpdateDialog.value = true
            }
        }
    }

    fun dismissUpdateDialog() {
        _showUpdateDialog.value = false
    }

    private val _currentTab = MutableStateFlow(ScreenTab.HOME)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    private val _selectedPlant = MutableStateFlow<MedicinalPlant?>(null)
    val selectedPlant: StateFlow<MedicinalPlant?> = _selectedPlant.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<PlantCategory?>(null)
    val selectedCategory: StateFlow<PlantCategory?> = _selectedCategory.asStateFlow()

    private val _selectedMizaj = MutableStateFlow<MizajType?>(null)
    val selectedMizaj: StateFlow<MizajType?> = _selectedMizaj.asStateFlow()

    private val _selectedProperty = MutableStateFlow<TraditionalProperty?>(null)
    val selectedProperty: StateFlow<TraditionalProperty?> = _selectedProperty.asStateFlow()

    val filteredPlants: StateFlow<List<MedicinalPlant>> = combine(
        _allPlants,
        _searchQuery,
        _selectedCategory,
        _selectedMizaj,
        _selectedProperty
    ) { all, query, cat, mizaj, prop ->
        PlantRepository.searchPlants(all, query, cat, mizaj, prop)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val favoritePlants: StateFlow<List<MedicinalPlant>> = combine(
        favoriteIds,
        _allPlants
    ) { favSet, allList ->
        allList.filter { favSet.contains(it.id) }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun selectTab(tab: ScreenTab) {
        _selectedPlant.value = null
        _currentTab.value = tab
    }

    fun selectPlant(plant: MedicinalPlant?) {
        _selectedPlant.value = plant
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: PlantCategory?) {
        _selectedCategory.value = if (_selectedCategory.value == category) null else category
    }

    fun selectMizaj(mizaj: MizajType?) {
        _selectedMizaj.value = if (_selectedMizaj.value == mizaj) null else mizaj
    }

    fun selectProperty(property: TraditionalProperty?) {
        _selectedProperty.value = if (_selectedProperty.value == property) null else property
    }

    fun toggleFavorite(plantId: Int) {
        userPrefsRepo.toggleFavorite(plantId)
    }

    fun setLanguage(language: Language) {
        userPrefsRepo.setLanguage(language)
    }

    fun setDarkMode(enabled: Boolean) {
        userPrefsRepo.setDarkMode(enabled)
    }

    fun setFontScale(scale: Float) {
        userPrefsRepo.setFontScale(scale)
    }

    fun saveUserMizaj(mizaj: MizajType) {
        userPrefsRepo.setUserMizaj(mizaj)
    }

    fun clearUserMizaj() {
        userPrefsRepo.clearUserMizaj()
    }
}
