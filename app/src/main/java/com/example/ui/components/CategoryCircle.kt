package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.data.model.PlantCategory

@Composable
fun CategoryCircleRow(
    currentLanguage: Language,
    selectedCategory: PlantCategory?,
    onCategorySelect: (PlantCategory?) -> Unit
) {
    val categories = listOf(
        CategoryCircleData(
            category = null,
            titleUy = "ھەممىسى",
            titleEn = "All Herbs",
            titleTr = "Tümü",
            titleZh = "全部",
            icon = Icons.Default.Category,
            bgColor = Color(0xFFE8F5E9),
            iconColor = Color(0xFF2E7D32)
        ),
        CategoryCircleData(
            category = PlantCategory.HERB,
            titleUy = PlantCategory.HERB.nameUy,
            titleEn = PlantCategory.HERB.nameEn,
            titleTr = PlantCategory.HERB.nameTr,
            titleZh = PlantCategory.HERB.nameZh,
            icon = Icons.Default.Eco,
            bgColor = Color(0xFFE0F2F1),
            iconColor = Color(0xFF00695C)
        ),
        CategoryCircleData(
            category = PlantCategory.FRUIT,
            titleUy = PlantCategory.FRUIT.nameUy,
            titleEn = PlantCategory.FRUIT.nameEn,
            titleTr = PlantCategory.FRUIT.nameTr,
            titleZh = PlantCategory.FRUIT.nameZh,
            icon = Icons.Default.Restaurant,
            bgColor = Color(0xFFFFEBEE),
            iconColor = Color(0xFFC62828)
        ),
        CategoryCircleData(
            category = PlantCategory.SPICE,
            titleUy = PlantCategory.SPICE.nameUy,
            titleEn = PlantCategory.SPICE.nameEn,
            titleTr = PlantCategory.SPICE.nameTr,
            titleZh = PlantCategory.SPICE.nameZh,
            icon = Icons.Default.LocalPharmacy,
            bgColor = Color(0xFFFFF8E1),
            iconColor = Color(0xFFF57F17)
        ),
        CategoryCircleData(
            category = PlantCategory.FLOWER,
            titleUy = PlantCategory.FLOWER.nameUy,
            titleEn = PlantCategory.FLOWER.nameEn,
            titleTr = PlantCategory.FLOWER.nameTr,
            titleZh = PlantCategory.FLOWER.nameZh,
            icon = Icons.Default.LocalFlorist,
            bgColor = Color(0xFFF3E5F5),
            iconColor = Color(0xFF6A1B9A)
        ),
        CategoryCircleData(
            category = PlantCategory.SEED,
            titleUy = PlantCategory.SEED.nameUy,
            titleEn = PlantCategory.SEED.nameEn,
            titleTr = PlantCategory.SEED.nameTr,
            titleZh = PlantCategory.SEED.nameZh,
            icon = Icons.Default.Medication,
            bgColor = Color(0xFFE8EAF6),
            iconColor = Color(0xFF283593)
        )
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { catData ->
            val isSelected = selectedCategory == catData.category
            val title = catData.getTitle(currentLanguage)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(72.dp)
                    .clickable { onCategorySelect(catData.category) }
                    .testTag("category_circle_${catData.category?.name?.lowercase() ?: "all"}")
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(catData.bgColor)
                        .border(
                            width = if (isSelected) 3.dp else 0.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = catData.icon,
                        contentDescription = title,
                        tint = catData.iconColor,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 11.sp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    ),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private data class CategoryCircleData(
    val category: PlantCategory?,
    val titleUy: String,
    val titleEn: String,
    val titleTr: String,
    val titleZh: String,
    val icon: ImageVector,
    val bgColor: Color,
    val iconColor: Color
) {
    fun getTitle(language: Language): String = when (language) {
        Language.UYGHUR -> titleUy
        Language.ENGLISH -> titleEn
        Language.TURKISH -> titleTr
        Language.CHINESE -> titleZh
    }
}
