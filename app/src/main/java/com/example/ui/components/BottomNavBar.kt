package com.example.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.ui.viewmodel.ScreenTab

@Composable
fun BottomNavBar(
    currentTab: ScreenTab,
    currentLanguage: Language,
    onTabSelected: (ScreenTab) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            TabItem(
                tab = ScreenTab.HOME,
                titleUy = "باش بەت",
                titleEn = "Home",
                titleTr = "Anasayfa",
                titleZh = "首页",
                icon = Icons.Default.Home
            ),
            TabItem(
                tab = ScreenTab.CATALOG,
                titleUy = "كۆرسەتكۈچ",
                titleEn = "Catalog",
                titleTr = "Katalog",
                titleZh = "草本",
                icon = Icons.Default.LocalPharmacy
            ),
            TabItem(
                tab = ScreenTab.CALENDAR,
                titleUy = "كالىندار",
                titleEn = "Calendar",
                titleTr = "Takvim",
                titleZh = "日历",
                icon = Icons.Default.CalendarMonth
            ),
            TabItem(
                tab = ScreenTab.MIZAJ_QUIZ,
                titleUy = "مىزاج",
                titleEn = "Mizaj Test",
                titleTr = "Mizaç",
                titleZh = "体质",
                icon = Icons.Default.Psychology
            ),
            TabItem(
                tab = ScreenTab.SETTINGS,
                titleUy = "تەڭشەك",
                titleEn = "Settings",
                titleTr = "Ayarlar",
                titleZh = "设置",
                icon = Icons.Default.Settings
            )
        )

        items.forEach { item ->
            val isSelected = currentTab == item.tab
            val title = item.getTitle(currentLanguage)

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(item.tab) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = title,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("nav_item_${item.tab.name.lowercase()}")
            )
        }
    }
}

private data class TabItem(
    val tab: ScreenTab,
    val titleUy: String,
    val titleEn: String,
    val titleTr: String,
    val titleZh: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    fun getTitle(language: Language): String = when (language) {
        Language.UYGHUR -> titleUy
        Language.ENGLISH -> titleEn
        Language.TURKISH -> titleTr
        Language.CHINESE -> titleZh
    }
}
