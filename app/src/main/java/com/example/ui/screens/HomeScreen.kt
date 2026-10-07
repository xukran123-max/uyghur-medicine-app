package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import com.example.data.model.Language
import com.example.data.model.MedicinalPlant
import com.example.data.model.PlantCategory
import com.example.data.model.TraditionalProperty
import com.example.ui.components.BannerCarousel
import com.example.ui.components.CategoryCircleRow
import com.example.ui.components.PlantCard

@Composable
fun HomeScreen(
    currentLanguage: Language,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: PlantCategory?,
    onCategorySelect: (PlantCategory?) -> Unit,
    selectedProperty: TraditionalProperty?,
    onPropertySelect: (TraditionalProperty?) -> Unit,
    plantsList: List<MedicinalPlant>,
    favoriteIds: Set<Int>,
    onFavoriteToggle: (Int) -> Unit,
    onPlantClick: (MedicinalPlant) -> Unit,
    onSeeAllClick: () -> Unit,
    onStartQuizClick: () -> Unit,
    onAssistantClick: () -> Unit = {}
) {
    val searchPlaceholder = when (currentLanguage) {
        Language.UYGHUR -> "دورىلىق ئۆسۈملۈكلەردىن ئىزدەڭ..."
        Language.ENGLISH -> "Search Uyghur medicinal herbs..."
        Language.TURKISH -> "Şifalı bitkiler arasında arayın..."
        Language.CHINESE -> "在维药草本典籍中搜索..."
    }

    val sectionTitle = when (currentLanguage) {
        Language.UYGHUR -> "دورىلىق ئۆسۈملۈكلەر"
        Language.ENGLISH -> "Medicinal Herbs & Plants"
        Language.TURKISH -> "Şifalı Bitkiler ve Otlar"
        Language.CHINESE -> "药用草本植物"
    }

    val seeAllText = when (currentLanguage) {
        Language.UYGHUR -> "ھەممىسى"
        Language.ENGLISH -> "All"
        Language.TURKISH -> "Tümü"
        Language.CHINESE -> "全部"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Search Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_search_input"),
                placeholder = {
                    Text(
                        text = searchPlaceholder,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        )
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(24.dp)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Hero Banner Slider
            item(span = { GridItemSpan(2) }) {
                BannerCarousel(
                    currentLanguage = currentLanguage,
                    onStartQuizClick = onStartQuizClick,
                    onExploreClick = onSeeAllClick
                )
            }


            // Category Circles
            item(span = { GridItemSpan(2) }) {
                CategoryCircleRow(
                    currentLanguage = currentLanguage,
                    selectedCategory = selectedCategory,
                    onCategorySelect = onCategorySelect
                )
            }

            // Traditional Property Filter Chips Group (Hot, Cold, Dry, Moist)
            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = selectedProperty == null,
                        onClick = { onPropertySelect(null) },
                        label = {
                            Text(
                                when (currentLanguage) {
                                    Language.UYGHUR -> "ھەممىسى"
                                    Language.ENGLISH -> "All"
                                    Language.TURKISH -> "Tümü"
                                    Language.CHINESE -> "全部"
                                }
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        )
                    )

                    TraditionalProperty.entries.forEach { prop ->
                        val isSelected = selectedProperty == prop
                        FilterChip(
                            selected = isSelected,
                            onClick = { onPropertySelect(prop) },
                            label = { Text("${prop.emoji} ${prop.getDisplayName(currentLanguage)}") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(prop.colorHex),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Section Header
            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp, 20.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.secondary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = sectionTitle,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { onSeeAllClick() }
                            .padding(4.dp)
                            .testTag("home_see_all_button")
                    ) {
                        Text(
                            text = seeAllText,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "All",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Plant Items Grid
            items(
                items = plantsList,
                key = { it.id }
            ) { plant ->
                Box(modifier = Modifier.padding(horizontal = 6.dp)) {
                    PlantCard(
                        plant = plant,
                        currentLanguage = currentLanguage,
                        isFavorite = favoriteIds.contains(plant.id),
                        onFavoriteToggle = { onFavoriteToggle(plant.id) },
                        onClick = { onPlantClick(plant) }
                    )
                }
            }
        }
    }
}
