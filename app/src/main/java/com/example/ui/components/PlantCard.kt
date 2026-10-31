package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Language
import com.example.data.model.MedicinalPlant

@Composable
fun PlantCard(
    plant: MedicinalPlant,
    currentLanguage: Language,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    onClick: () -> Unit
) {
    val imageRes = plant.getImageResId(currentLanguage) ?: plant.imageResId ?: when (plant.id % 3) {
        1 -> R.drawable.herbal_plant_anise_1786475403200
        2 -> R.drawable.herbal_plants_collection_1786475389012
        else -> R.drawable.herbal_plant_saffron_1786475416225
    }
    val currentImageUrl = plant.getImageUrl(currentLanguage) ?: plant.imageUrl

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("plant_card_${plant.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.5.dp,
            pressedElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(13.dp)
        ) {
            // Top Row: High Quality Image Thumbnail & Favorite Heart
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // High Quality Plant Image Thumbnail
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    val context = androidx.compose.ui.platform.LocalContext.current
                    val resolvedModel = androidx.compose.runtime.remember(currentImageUrl, currentLanguage) {
                        com.example.ui.util.ImageUtils.resolvePlantImageModel(currentImageUrl, context)
                    }

                    if (resolvedModel != null) {
                        coil.compose.AsyncImage(
                            model = resolvedModel,
                            contentDescription = plant.getName(currentLanguage),
                            placeholder = painterResource(id = imageRes),
                            error = painterResource(id = imageRes),
                            fallback = painterResource(id = imageRes),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = plant.getName(currentLanguage),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // Favorite Heart Button with gentle circular backdrop
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .clickable { onFavoriteToggle() }
                        .testTag("plant_card_favorite_${plant.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color(0xFFE74C3C) else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title: Plant Name (Clean, Bold, Primary Focus)
            Text(
                text = plant.getName(currentLanguage),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(5.dp))

            // Benefits Summary (Clean, readable, natural line height)
            Text(
                text = plant.getBenefits(currentLanguage),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ForumCourseCard(
    plant: MedicinalPlant,
    currentLanguage: Language,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    onClick: () -> Unit
) {
    val imageRes = plant.getImageResId(currentLanguage) ?: plant.imageResId ?: R.drawable.mizaj_kursi_yengi_banner
    val currentImageUrl = plant.getImageUrl(currentLanguage) ?: plant.imageUrl

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("forum_card_${plant.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFD4AF37).copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp,
            pressedElevation = 6.dp
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Widescreen Course / Forum Banner (Full Width)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFF0F2B1D)),
                contentAlignment = Alignment.Center
            ) {
                val context = androidx.compose.ui.platform.LocalContext.current
                val resolvedModel = androidx.compose.runtime.remember(currentImageUrl, currentLanguage) {
                    com.example.ui.util.ImageUtils.resolvePlantImageModel(currentImageUrl, context)
                }

                if (resolvedModel != null) {
                    coil.compose.AsyncImage(
                        model = resolvedModel,
                        contentDescription = plant.getName(currentLanguage),
                        placeholder = painterResource(id = imageRes),
                        error = painterResource(id = imageRes),
                        fallback = painterResource(id = imageRes),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = plant.getName(currentLanguage),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Favorite Heart on Top Corner
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.55f))
                        .clickable { onFavoriteToggle() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color(0xFFE74C3C) else Color.White,
                        modifier = Modifier.size(19.dp)
                    )
                }

                // Top Badge on Top Start
                Surface(
                    shape = RoundedCornerShape(bottomEnd = 12.dp),
                    color = Color(0xFF8B5A2B),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = "🎓 تېبابەت مۇنبىرى",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Text(
                    text = plant.getName(currentLanguage),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = plant.getBenefits(currentLanguage),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFD4AF37).copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = if (plant.id == 52) "30 سائەتلىك كۇرس" else "ئىلمىي مەزمۇن",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF8B5A2B),
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = when (currentLanguage) {
                                Language.UYGHUR -> "تەپسىلاتىنى كۆرۈش"
                                Language.ENGLISH -> "View Details"
                                Language.TURKISH -> "Detayları Gör"
                            },
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = "→", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
