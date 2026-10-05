package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.data.model.MedicinalPlant
import com.example.data.model.MizajType
import com.example.data.model.PlantCategory
import com.example.data.model.TraditionalProperty
import com.example.ui.components.PlantCard

@Composable
fun CatalogScreen(
    currentLanguage: Language,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: PlantCategory?,
    onCategorySelect: (PlantCategory?) -> Unit,
    selectedMizaj: MizajType?,
    onMizajSelect: (MizajType?) -> Unit,
    selectedProperty: TraditionalProperty?,
    onPropertySelect: (TraditionalProperty?) -> Unit,
    plantsList: List<MedicinalPlant>,
    favoriteIds: Set<Int>,
    onFavoriteToggle: (Int) -> Unit,
    onPlantClick: (MedicinalPlant) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Search & Counter Section
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Title & Count
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (currentLanguage) {
                            Language.UYGHUR -> "دورىلىق ئۆسۈملۈكلەر جەدۋىلى"
                            Language.ENGLISH -> "Medicinal Plants Catalog"
                            Language.TURKISH -> "Şifalı Bitkiler Kataloğu"
                            Language.CHINESE -> "维药草本植物典籍"
                        },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "${plantsList.size} ${when(currentLanguage) {
                                Language.UYGHUR -> "تۈر"
                                Language.ENGLISH -> "items"
                                Language.TURKISH -> "bitki"
                                Language.CHINESE -> "项"
                            }}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Input field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("catalog_search_input"),
                    placeholder = {
                        Text(
                            text = when (currentLanguage) {
                                Language.UYGHUR -> "ئىسمى، خۇسۇسىيىتى ياكى لاتىنچە ئىسمىنى ئىزدەڭ..."
                                Language.ENGLISH -> "Search by name, benefits or Latin name..."
                                Language.TURKISH -> "Bitki adı, faydası veya Latince adı ile ara..."
                                Language.CHINESE -> "搜索名称、拉丁名或药理功效..."
                            },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
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
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Section 1: Property Filter Pills (Cold, Hot, Dry, Moist)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (currentLanguage) {
                            Language.UYGHUR -> "خۇسۇسىيەت:"
                            Language.ENGLISH -> "Property:"
                            Language.TURKISH -> "Özellik:"
                            Language.CHINESE -> "药性:"
                        },
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    CustomFilterPill(
                        text = when (currentLanguage) {
                            Language.UYGHUR -> "ھەممىسى"
                            Language.ENGLISH -> "All"
                            Language.TURKISH -> "Tümü"
                            Language.CHINESE -> "全部"
                        },
                        isSelected = selectedProperty == null,
                        onClick = { onPropertySelect(null) }
                    )

                    TraditionalProperty.entries.forEach { prop ->
                        val isSelected = selectedProperty == prop
                        CustomFilterPill(
                            text = "${prop.emoji} ${prop.getDisplayName(currentLanguage)}",
                            isSelected = isSelected,
                            onClick = { onPropertySelect(prop) },
                            selectedBgColor = Color(prop.colorHex)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Section 2: Mizaj Filter Pills (Hot-Dry, Hot-Moist, etc.)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (currentLanguage) {
                            Language.UYGHUR -> "مىزاج خىلىتى:"
                            Language.ENGLISH -> "Mizaj:"
                            Language.TURKISH -> "Mizaç:"
                            Language.CHINESE -> "体质:"
                        },
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    CustomFilterPill(
                        text = when (currentLanguage) {
                            Language.UYGHUR -> "ھەممىسى"
                            Language.ENGLISH -> "All"
                            Language.TURKISH -> "Tümü"
                            Language.CHINESE -> "全部"
                        },
                        isSelected = selectedMizaj == null,
                        onClick = { onMizajSelect(null) }
                    )

                    MizajType.entries.forEach { mizaj ->
                        val isSelected = selectedMizaj == mizaj
                        CustomFilterPill(
                            text = mizaj.getDisplayName(currentLanguage),
                            isSelected = isSelected,
                            onClick = { onMizajSelect(mizaj) },
                            selectedBgColor = Color(mizaj.colorHex)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Section 3: Category Filter Pills
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (currentLanguage) {
                            Language.UYGHUR -> "تۈرلەر:"
                            Language.ENGLISH -> "Category:"
                            Language.TURKISH -> "Kategori:"
                            Language.CHINESE -> "分类:"
                        },
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    CustomFilterPill(
                        text = when (currentLanguage) {
                            Language.UYGHUR -> "ھەممىسى"
                            Language.ENGLISH -> "All"
                            Language.TURKISH -> "Tümü"
                            Language.CHINESE -> "全部"
                        },
                        isSelected = selectedCategory == null,
                        onClick = { onCategorySelect(null) }
                    )

                    PlantCategory.entries.forEach { cat ->
                        val isSelected = selectedCategory == cat
                        CustomFilterPill(
                            text = cat.getDisplayName(currentLanguage),
                            isSelected = isSelected,
                            onClick = { onCategorySelect(cat) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Grid List
        if (plantsList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (currentLanguage) {
                        Language.UYGHUR -> "تەلىپىڭىزگە ماس كېلىدىغان ئۆسۈملۈك تېپىلمىدى"
                        Language.ENGLISH -> "No medicinal plants match your search"
                        Language.TURKISH -> "Aramanıza uygun bitki bulunamadı"
                        Language.CHINESE -> "未找到符合条件的相关草本资料"
                    },
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = plantsList,
                    key = { it.id }
                ) { plant ->
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

@Composable
fun CustomFilterPill(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    selectedBgColor: Color = MaterialTheme.colorScheme.primary,
    selectedTextColor: Color = Color.White
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) selectedBgColor else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
        modifier = Modifier.height(36.dp)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) selectedTextColor else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                ),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
