package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
    val context = LocalContext.current
    fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val searchPlaceholder = when (currentLanguage) {
        Language.UYGHUR -> "دورىلىق ئۆسۈملۈكلەردىن ئىزدەڭ..."
        Language.ENGLISH -> "Search Uyghur medicinal herbs..."
        Language.TURKISH -> "Şifalı bitkiler arasında arayın..."
    }

    val sectionTitle = when {
        selectedCategory != null -> selectedCategory.getDisplayName(currentLanguage)
        searchQuery.isNotEmpty() -> when (currentLanguage) {
            Language.UYGHUR -> "ئىزدەش نەتىجىسى"
            Language.ENGLISH -> "Search Results"
            Language.TURKISH -> "Arama Sonuçları"
        }
        else -> when (currentLanguage) {
            Language.UYGHUR -> "دورىلىق ئۆسۈملۈكلەر"
            Language.ENGLISH -> "Medicinal Herbs & Plants"
            Language.TURKISH -> "Şifalı Bitkiler"
        }
    }

    val seeAllText = when (currentLanguage) {
        Language.UYGHUR -> "ھەممىسى"
        Language.ENGLISH -> "All"
        Language.TURKISH -> "Tümü"
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
            // 1. Category Circles at the Top (Right under Search)
            item(span = { GridItemSpan(2) }) {
                CategoryCircleRow(
                    currentLanguage = currentLanguage,
                    selectedCategory = selectedCategory,
                    onCategorySelect = { cat ->
                        if (cat == PlantCategory.TEBIBIY_KITABLAR) {
                            openUrl("https://www.uyghurmedicine.com/kitablar/?app=1")
                        } else {
                            onCategorySelect(cat)
                        }
                    }
                )
            }

            // 2. Hero Banner Slider & Featured Cards (Only on Home All view)
            if (selectedCategory == null && searchQuery.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    BannerCarousel(
                    currentLanguage = currentLanguage,
                    onStartQuizClick = onStartQuizClick,
                    onExploreClick = onSeeAllClick
                )
            }

            // 3. Featured Card: Tebabet Munbiri & Mizaj Course
            item(span = { GridItemSpan(2) }) {
                Card(
                    onClick = {
                        openUrl("https://www.uyghurmedicine.com/tebabet-munbiri/mizaj-kursi-semsinur?app=1")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFAF5EB)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF8B5A2B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🎓",
                                fontSize = 22.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = when (currentLanguage) {
                                        Language.UYGHUR -> "تېبابەت مۇنبىرى: مىجەز كۇرسى"
                                        Language.ENGLISH -> "Forum: Mizaj Course"
                                        Language.TURKISH -> "Tıp Forumu: Mizaç Eğitimi"
                                    },
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF5C381E)
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f, fill = false)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = Color(0xFFF0E0C8),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = when (currentLanguage) {
                                            Language.UYGHUR -> "يېڭى كۇرس"
                                            Language.ENGLISH -> "NEW"
                                            Language.TURKISH -> "YENİ"
                                        },
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFF5C381E),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        ),
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = when (currentLanguage) {
                                    Language.UYGHUR -> "شەمشىنۇر مىجەز تەلىم-تەربىيە كۇرسى — تەپسىلاتى ۋە تىزىملىتىش"
                                    Language.ENGLISH -> "Mizaj training program & enrollment details"
                                    Language.TURKISH -> "Mizaç eğitimi programı ve kayıt detayları"
                                },
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Open Forum",
                            tint = Color(0xFF8B5A2B)
                        )
                    }
                }
            }

            // 4. Featured Card: Simple Home Remedies (ياڭاق دەملىمىسى)
            item(span = { GridItemSpan(2) }) {
                Card(
                    onClick = {
                        openUrl("https://www.uyghurmedicine.com/addiy-retsiplar/bel-borek-yaqang-demlimisi?app=1")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF0FDF4)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF15803D)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🍵",
                                fontSize = 22.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = when (currentLanguage) {
                                        Language.UYGHUR -> "ئاددى رېتسىپ: ياڭاق دەملىمىسى"
                                        Language.ENGLISH -> "Recipe: Walnut Infusion"
                                        Language.TURKISH -> "Reçete: Ceviz Çayı"
                                    },
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF166534)
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f, fill = false)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = Color(0xFFDCFCE7),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = when (currentLanguage) {
                                            Language.UYGHUR -> "كۈندىلىك شىپا"
                                            Language.ENGLISH -> "REMEDY"
                                            Language.TURKISH -> "ŞİFA"
                                        },
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFF166534),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        ),
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = when (currentLanguage) {
                                    Language.UYGHUR -> "بەل ۋە بۆرەك قۇۋۋەتلەش ياڭاق دەملىمىسى — تەييارلاش ئۇسۇلى"
                                    Language.ENGLISH -> "Walnut herbal tea for lower back and kidneys"
                                    Language.TURKISH -> "Böbrek ve bel güçlendirici ceviz çayı tarifi"
                                },
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Open Recipe",
                            tint = Color(0xFF15803D)
                        )
                    }
                }
            }

            // 5. Featured Card: Medical Books Digital Library (Single, non-duplicated)
            item(span = { GridItemSpan(2) }) {
                Card(
                    onClick = {
                        openUrl("https://www.uyghurmedicine.com/kitablar/?app=1")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF7F2EC)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF6D4C41)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "📖",
                                fontSize = 22.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = when (currentLanguage) {
                                        Language.UYGHUR -> "تېببىي كىتابلار قامۇسى"
                                        Language.ENGLISH -> "Medical Books Library"
                                        Language.TURKISH -> "Tıbbi Kitaplar Kitaplığı"
                                    },
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4E342E)
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f, fill = false)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = Color(0xFFD7CCC8),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = when (currentLanguage) {
                                            Language.UYGHUR -> "قامۇس"
                                            Language.ENGLISH -> "LIBRARY"
                                            Language.TURKISH -> "KÜTÜPHANE"
                                        },
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFF4E342E),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        ),
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = when (currentLanguage) {
                                    Language.UYGHUR -> "قەدىمىي قوليازمىلار، ئەل-قانۇن ۋە رېتسىپ كىتابلىرىنى ئوقۇڭ"
                                    Language.ENGLISH -> "Browse ancient manuscripts, Canon of Medicine & classics"
                                    Language.TURKISH -> "El-Kanun ve kadim tıp eserlerini çevirerek okuyun"
                                },
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Open Library",
                            tint = Color(0xFF6D4C41)
                        )
                    }
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

            if (plantsList.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp, horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "🌱",
                                fontSize = 40.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = when (currentLanguage) {
                                    Language.UYGHUR -> "بۇ سەھىپىدە كۆرسىتىدىغان مەزمۇن تېپىلمىدى."
                                    Language.ENGLISH -> "No items found in this category."
                                    Language.TURKISH -> "Bu kategoride henüz içerik bulunamadı."
                                },
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
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
