package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.data.model.MedicinalPlant
import com.example.ui.components.PlantCard

@Composable
fun FavoritesScreen(
    currentLanguage: Language,
    favoritePlants: List<MedicinalPlant>,
    favoriteIds: Set<Int>,
    onFavoriteToggle: (Int) -> Unit,
    onPlantClick: (MedicinalPlant) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(
                                color = Color(0xFFE74C3C).copy(alpha = 0.12f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color(0xFFE74C3C),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.size(10.dp))

                    Text(
                        text = when (currentLanguage) {
                            Language.UYGHUR -> "ياقتۇرغانلىرىم"
                            Language.ENGLISH -> "My Favorites"
                            Language.TURKISH -> "Favorilerim"
                        },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                if (favoritePlants.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = when (currentLanguage) {
                                Language.UYGHUR -> "${favoritePlants.size} تۈر"
                                Language.ENGLISH -> "${favoritePlants.size} items"
                                Language.TURKISH -> "${favoritePlants.size} öğe"
                            },
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        if (favoritePlants.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Empty Favorites",
                            modifier = Modifier.size(36.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = when (currentLanguage) {
                            Language.UYGHUR -> "ھازىرچە ياقتۇرغان مەزمۇن يوق"
                            Language.ENGLISH -> "No saved remedies yet"
                            Language.TURKISH -> "Henüz kaydedilmiş bitki yok"
                        },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = when (currentLanguage) {
                            Language.UYGHUR -> "دورىلار ۋە مەزمۇنلاردىكى يۈرەك بەلگىسىنى چېكىپ، ياقتۇرغانلىرىڭىزنى بۇ يەرگە يىغىۋالالايسىز."
                            Language.ENGLISH -> "Tap the heart icon on any remedy to save your favorites here."
                            Language.TURKISH -> "İçeriklerdeki kalp simgesine dokunarak favorilerinizi buraya toplayabilirsiniz."
                        },
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = favoritePlants,
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
