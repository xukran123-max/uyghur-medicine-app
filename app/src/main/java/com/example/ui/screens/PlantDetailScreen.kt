package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.ui.components.ZoomableContainer
import com.example.ui.components.ZoomableImageViewerDialog
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.data.model.MedicinalPlant

@Composable
fun PlantDetailScreen(
    plant: MedicinalPlant,
    currentLanguage: Language,
    isFavorite: Boolean,
    isAdmin: Boolean = false,
    onEditClick: (() -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null,
    onFavoriteToggle: () -> Unit,
    onBackClick: () -> Unit
) {
    var showZoomDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Surface(
                shadowElevation = 2.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        text = plant.getName(currentLanguage),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isAdmin && onEditClick != null) {
                            IconButton(onClick = onEditClick) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Plant",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        if (isAdmin && onDeleteClick != null) {
                            IconButton(onClick = onDeleteClick) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Plant",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                        IconButton(onClick = onFavoriteToggle) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) Color(0xFFE74C3C) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header Card with Emoji Illustration
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                val detailImageRes = plant.imageResId ?: when (plant.id % 3) {
                    1 -> R.drawable.herbal_plant_anise_1786475403200
                    2 -> R.drawable.herbal_plants_collection_1786475389012
                    else -> R.drawable.herbal_plant_saffron_1786475416225
                }

                if (showZoomDialog) {
                    ZoomableImageViewerDialog(
                        imageResId = detailImageRes,
                        imageUrl = plant.imageUrl,
                        title = plant.getName(currentLanguage),
                        currentLanguage = currentLanguage,
                        onDismiss = { showZoomDialog = false }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                            .clickable { showZoomDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        val contentScale = if (plant.imageFit == "cover") ContentScale.Crop else ContentScale.Fit
                        ZoomableContainer(
                            modifier = Modifier.fillMaxSize()
                        ) { _, _ ->
                            val context = androidx.compose.ui.platform.LocalContext.current
                            val resolvedModel = androidx.compose.runtime.remember(plant.imageUrl) {
                                val raw = plant.imageUrl?.trim()
                                when {
                                    raw.isNullOrBlank() -> null
                                    raw.startsWith("http://") || raw.startsWith("https://") -> raw
                                    raw.startsWith("/images/plants/") -> "https://uyghurmedicine.com$raw"
                                    raw.startsWith("/") && java.io.File(raw).exists() -> java.io.File(raw)
                                    else -> {
                                        val localFile = java.io.File(context.filesDir, "plant_images/${raw.substringAfterLast("/")}")
                                        if (localFile.exists()) localFile else "https://uyghurmedicine.com/images/plants/${raw.substringAfterLast("/")}"
                                    }
                                }
                            }

                            if (resolvedModel != null) {
                                coil.compose.AsyncImage(
                                    model = resolvedModel,
                                    contentDescription = plant.getName(currentLanguage),
                                    placeholder = painterResource(id = detailImageRes),
                                    error = painterResource(id = detailImageRes),
                                    fallback = painterResource(id = detailImageRes),
                                    contentScale = contentScale,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = detailImageRes),
                                    contentDescription = plant.getName(currentLanguage),
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        // Pinch Zoom badge in top-right
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color.Black.copy(alpha = 0.65f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ZoomIn,
                                    contentDescription = "Zoom",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = when (currentLanguage) {
                                        Language.UYGHUR -> "ئىككى بارماقتا كېڭەيتىڭ"
                                        Language.ENGLISH -> "Pinch to zoom"
                                        Language.TURKISH -> "Çift parmakla büyüt"
                                    },
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = plant.getName(currentLanguage),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = plant.latinName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Mizaj & Category Tags Row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Category Badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = plant.category.getDisplayName(currentLanguage),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        // Mizaj Badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(plant.mizajType.colorHex).copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "${plant.mizajType.getDisplayName(currentLanguage)} • ${plant.getMizajDegree(currentLanguage)}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(plant.mizajType.colorHex)
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Target Organs Tag
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${when(currentLanguage) {
                                Language.UYGHUR -> "تەسىر قىلىدىغان ئەزا: "
                                Language.ENGLISH -> "Target Organs: "
                                Language.TURKISH -> "Etki Eden Organlar: "
                            }}${plant.getOrganTarget(currentLanguage)}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 1: Health Benefits (خۇسۇسىيىتى)
            DetailSectionCard(
                title = when (currentLanguage) {
                    Language.UYGHUR -> "خۇسۇسىيىتى"
                    Language.ENGLISH -> "Health Benefits"
                    Language.TURKISH -> "Faydaları"
                },
                icon = Icons.Default.Info,
                iconTint = MaterialTheme.colorScheme.primary,
                content = plant.getBenefits(currentLanguage)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Section 2: Usage & Dosage (ئىشلىتىش ئۇسۇلى)
            DetailSectionCard(
                title = when (currentLanguage) {
                    Language.UYGHUR -> "ئىشلىتىش ئۇسۇلى"
                    Language.ENGLISH -> "Usage & Preparations"
                    Language.TURKISH -> "Kullanım Şekli"
                },
                icon = Icons.Default.LocalPharmacy,
                iconTint = MaterialTheme.colorScheme.secondary,
                content = plant.getUsage(currentLanguage)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Section 3: Cautions (ئەسكەرتىش)
            DetailSectionCard(
                title = when (currentLanguage) {
                    Language.UYGHUR -> "ئەسكەرتىش"
                    Language.ENGLISH -> "Cautions & Notes"
                    Language.TURKISH -> "Önemli Notlar"
                },
                icon = Icons.Default.Warning,
                iconTint = MaterialTheme.colorScheme.tertiary,
                content = plant.getCaution(currentLanguage)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Links Card (تېز ئۇلانمىلار)
            val uriHandler = LocalUriHandler.current

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        )
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Link,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = when (currentLanguage) {
                                    Language.UYGHUR -> "تېز ئۇلانمىلار"
                                    Language.ENGLISH -> "Quick Links"
                                    Language.TURKISH -> "Hızlı Bağlantılar"
                                },
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Button 1: Products (مەھسۇلاتلار / teklimakan.com)
                        Surface(
                            onClick = { uriHandler.openUri("https://www.teklimakan.com") },
                            modifier = Modifier
                                .weight(1f)
                                .height(68.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFFEF8ED),
                            border = BorderStroke(1.dp, Color(0xFFF39C12).copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 6.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = null,
                                    tint = Color(0xFFD35400),
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = when (currentLanguage) {
                                            Language.UYGHUR -> "مەھسۇلاتلار"
                                            Language.ENGLISH -> "Products"
                                            Language.TURKISH -> "Ürünler"
                                        },
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFD35400)
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = "teklimakan.com",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 11.sp,
                                            color = Color.DarkGray
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        // Button 2: Contact Immediately (دەرھال ئالاقىلىشىڭ / +905551609999)
                        Surface(
                            onClick = { uriHandler.openUri("https://wa.me/+905551609999") },
                            modifier = Modifier
                                .weight(1f)
                                .height(68.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFE8F8F0),
                            border = BorderStroke(1.dp, Color(0xFF2ECC71).copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 6.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = null,
                                    tint = Color(0xFF128C7E),
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = when (currentLanguage) {
                                            Language.UYGHUR -> "دەرھال ئالاقىلىشىڭ"
                                            Language.ENGLISH -> "Contact Us"
                                            Language.TURKISH -> "Hemen İletişime Geçin"
                                        },
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF128C7E)
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = "+905551609999",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 11.sp,
                                            color = Color.DarkGray
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Names in All 4 Languages Reference Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = when (currentLanguage) {
                            Language.UYGHUR -> "باشقا تىللاردىكى نامى"
                            Language.ENGLISH -> "Names in Other Languages"
                            Language.TURKISH -> "Diğer Dillerdeki Adı"
                        },
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("• ئۇيغۇرچە: ${plant.nameUy}", style = MaterialTheme.typography.bodyMedium)
                    Text("• English: ${plant.nameEn}", style = MaterialTheme.typography.bodyMedium)
                    Text("• Türkçe: ${plant.nameTr}", style = MaterialTheme.typography.bodyMedium)
                    Text("• 中文: ${plant.nameZh}", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DetailSectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )
            )
        }
    }
}
