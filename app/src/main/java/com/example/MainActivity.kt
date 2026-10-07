package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.data.model.Language
import com.example.data.model.MedicinalPlant
import com.example.ui.components.AdminConfirmDeleteDialog
import com.example.ui.components.AppUpdateDialog
import com.example.ui.components.BottomNavBar
import com.example.ui.components.EditPlantDialog
import com.example.ui.components.MenuDrawerSheet
import com.example.ui.components.TopBar
import com.example.ui.screens.AssistantScreen
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MizajQuizScreen
import com.example.ui.screens.PlantDetailScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.UyghurTibabitiTheme
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenTab

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val currentLanguage by viewModel.currentLanguage.collectAsState()
            val isDarkMode by viewModel.isDarkMode.collectAsState()
            val currentTab by viewModel.currentTab.collectAsState()
            val selectedPlant by viewModel.selectedPlant.collectAsState()
            val searchQuery by viewModel.searchQuery.collectAsState()
            val selectedCategory by viewModel.selectedCategory.collectAsState()
            val selectedMizaj by viewModel.selectedMizaj.collectAsState()
            val selectedProperty by viewModel.selectedProperty.collectAsState()
            val filteredPlants by viewModel.filteredPlants.collectAsState()
            val favoritePlants by viewModel.favoritePlants.collectAsState()
            val favoriteIds by viewModel.favoriteIds.collectAsState()
            val userMizaj by viewModel.userMizaj.collectAsState()
            val fontSizeScale by viewModel.fontSizeMultiplier.collectAsState()
            val appUpdateInfo by viewModel.appUpdateInfo.collectAsState()
            val showUpdateDialog by viewModel.showUpdateDialog.collectAsState()
            val isAdminLoggedIn by viewModel.isAdminLoggedIn.collectAsState()

            var showMenuSheet by remember { mutableStateOf(false) }
            var editingPlant by remember { mutableStateOf<MedicinalPlant?>(null) }
            var showEditPlantDialog by remember { mutableStateOf(false) }
            var deletingPlant by remember { mutableStateOf<MedicinalPlant?>(null) }
            var showDeleteConfirmDialog by remember { mutableStateOf(false) }

            // RTL for Uyghur Language, LTR for English/Turkish/Chinese
            val layoutDirection = if (currentLanguage == Language.UYGHUR) LayoutDirection.Rtl else LayoutDirection.Ltr

            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                UyghurTibabitiTheme(darkTheme = isDarkMode) {
                    if (showMenuSheet) {
                        MenuDrawerSheet(
                            currentLanguage = currentLanguage,
                            onDismiss = { showMenuSheet = false },
                            onNavigateTab = { tab -> viewModel.selectTab(tab) }
                        )
                    }

                    if (showUpdateDialog && appUpdateInfo != null) {
                        AppUpdateDialog(
                            updateInfo = appUpdateInfo!!,
                            currentLanguage = currentLanguage,
                            onDismiss = { viewModel.dismissUpdateDialog() }
                        )
                    }

                    if (showEditPlantDialog) {
                        EditPlantDialog(
                            initialPlant = editingPlant,
                            currentLanguage = currentLanguage,
                            onDismiss = { showEditPlantDialog = false },
                            onSave = { plant ->
                                viewModel.savePlant(plant)
                                showEditPlantDialog = false
                            }
                        )
                    }

                    if (showDeleteConfirmDialog && deletingPlant != null) {
                        AdminConfirmDeleteDialog(
                            plantName = deletingPlant!!.getName(currentLanguage),
                            onDismiss = { showDeleteConfirmDialog = false },
                            onConfirmDelete = {
                                viewModel.deletePlant(deletingPlant!!.id)
                                showDeleteConfirmDialog = false
                            }
                        )
                    }

                    if (selectedPlant != null) {
                        // Fullscreen Detail View
                        PlantDetailScreen(
                            plant = selectedPlant!!,
                            currentLanguage = currentLanguage,
                            isFavorite = favoriteIds.contains(selectedPlant!!.id),
                            isAdmin = isAdminLoggedIn,
                            onEditClick = {
                                editingPlant = selectedPlant
                                showEditPlantDialog = true
                            },
                            onDeleteClick = {
                                deletingPlant = selectedPlant
                                showDeleteConfirmDialog = true
                            },
                            onFavoriteToggle = { viewModel.toggleFavorite(selectedPlant!!.id) },
                            onBackClick = { viewModel.selectPlant(null) }
                        )
                    } else {
                        Scaffold(
                            topBar = {
                                TopBar(
                                    currentLanguage = currentLanguage,
                                    isDarkMode = isDarkMode,
                                    onMenuClick = { showMenuSheet = true },
                                    onProfileClick = { viewModel.selectTab(ScreenTab.MIZAJ_QUIZ) },
                                    onDarkModeToggle = { viewModel.setDarkMode(!isDarkMode) },
                                    onLanguageClick = { viewModel.selectTab(ScreenTab.SETTINGS) }
                                )
                            },
                            bottomBar = {
                                BottomNavBar(
                                    currentTab = currentTab,
                                    currentLanguage = currentLanguage,
                                    onTabSelected = { viewModel.selectTab(it) }
                                )
                            },
                            modifier = Modifier.fillMaxSize()
                        ) { innerPadding ->
                            Box(modifier = Modifier.padding(innerPadding)) {
                                when (currentTab) {
                                    ScreenTab.HOME -> HomeScreen(
                                        currentLanguage = currentLanguage,
                                        searchQuery = searchQuery,
                                        onSearchQueryChange = { viewModel.setSearchQuery(it) },
                                        selectedCategory = selectedCategory,
                                        onCategorySelect = { viewModel.selectCategory(it) },
                                        selectedProperty = selectedProperty,
                                        onPropertySelect = { viewModel.selectProperty(it) },
                                        plantsList = filteredPlants,
                                        favoriteIds = favoriteIds,
                                        onFavoriteToggle = { viewModel.toggleFavorite(it) },
                                        onPlantClick = { viewModel.selectPlant(it) },
                                        onSeeAllClick = { viewModel.selectTab(ScreenTab.CATALOG) },
                                        onStartQuizClick = { viewModel.selectTab(ScreenTab.MIZAJ_QUIZ) },
                                        onAssistantClick = { viewModel.selectTab(ScreenTab.ASSISTANT) }
                                    )

                                    ScreenTab.CATALOG -> CatalogScreen(
                                        currentLanguage = currentLanguage,
                                        searchQuery = searchQuery,
                                        onSearchQueryChange = { viewModel.setSearchQuery(it) },
                                        selectedCategory = selectedCategory,
                                        onCategorySelect = { viewModel.selectCategory(it) },
                                        selectedMizaj = selectedMizaj,
                                        onMizajSelect = { viewModel.selectMizaj(it) },
                                        selectedProperty = selectedProperty,
                                        onPropertySelect = { viewModel.selectProperty(it) },
                                        plantsList = filteredPlants,
                                        favoriteIds = favoriteIds,
                                        onFavoriteToggle = { viewModel.toggleFavorite(it) },
                                        onPlantClick = { viewModel.selectPlant(it) }
                                    )

                                    ScreenTab.ASSISTANT -> AssistantScreen(
                                        currentLanguage = currentLanguage
                                    )

                                    ScreenTab.CALENDAR -> CalendarScreen(
                                        currentLanguage = currentLanguage
                                    )

                                    ScreenTab.MIZAJ_QUIZ -> MizajQuizScreen(
                                        currentLanguage = currentLanguage,
                                        savedMizaj = userMizaj,
                                        onSaveMizaj = { viewModel.saveUserMizaj(it) },
                                        onClearMizaj = { viewModel.clearUserMizaj() },
                                        favoriteIds = favoriteIds,
                                        onFavoriteToggle = { viewModel.toggleFavorite(it) },
                                        onPlantClick = { viewModel.selectPlant(it) }
                                    )

                                    ScreenTab.FAVORITES -> CatalogScreen(
                                        currentLanguage = currentLanguage,
                                        searchQuery = searchQuery,
                                        onSearchQueryChange = { viewModel.setSearchQuery(it) },
                                        selectedCategory = selectedCategory,
                                        onCategorySelect = { viewModel.selectCategory(it) },
                                        selectedMizaj = selectedMizaj,
                                        onMizajSelect = { viewModel.selectMizaj(it) },
                                        selectedProperty = selectedProperty,
                                        onPropertySelect = { viewModel.selectProperty(it) },
                                        plantsList = filteredPlants,
                                        favoriteIds = favoriteIds,
                                        onFavoriteToggle = { viewModel.toggleFavorite(it) },
                                        onPlantClick = { viewModel.selectPlant(it) }
                                    )

                                    ScreenTab.SETTINGS -> SettingsScreen(
                                        currentLanguage = currentLanguage,
                                        onLanguageChange = { viewModel.setLanguage(it) },
                                        isDarkMode = isDarkMode,
                                        onDarkModeToggle = { viewModel.setDarkMode(it) },
                                        fontSizeScale = fontSizeScale,
                                        onFontScaleChange = { viewModel.setFontScale(it) },
                                        isAdminLoggedIn = isAdminLoggedIn,
                                        onLoginAdmin = { pass -> viewModel.loginAdmin(pass) },
                                        onLogoutAdmin = { viewModel.logoutAdmin() },
                                        onChangeAdminPassword = { oldPass, newPass -> viewModel.changeAdminPassword(oldPass, newPass) },
                                        onAddNewPlant = {
                                            editingPlant = null
                                            showEditPlantDialog = true
                                        },
                                        onResetToDefault = { viewModel.resetPlantsToDefault() }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
