package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language

@Composable
fun SettingsScreen(
    currentLanguage: Language,
    onLanguageChange: (Language) -> Unit,
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit,
    fontSizeScale: Float,
    onFontScaleChange: (Float) -> Unit
) {
    val headerTitle = when (currentLanguage) {
        Language.UYGHUR -> "ئىقتىدار ۋە تىل تەڭشىكى"
        Language.ENGLISH -> "App & Language Settings"
        Language.TURKISH -> "Uygulama ve Dil Ayarları"
        Language.CHINESE -> "应用与语言系统设置"
    }

    val headerSub = when (currentLanguage) {
        Language.UYGHUR -> "4 خىل تىل تەڭشىكى، كېچە كۈندۈز ئەندىزىسى ۋە خەت چوڭلۇقى"
        Language.ENGLISH -> "4 languages, Light/Dark mode & Font scaling"
        Language.TURKISH -> "4 dil seçeneği, Gece/Gündüz modu ve Yazı boyutu"
        Language.CHINESE -> "4种语言支持、日夜模式及字体调控"
    }

    val langSectionTitle = when (currentLanguage) {
        Language.UYGHUR -> "تىل تەڭشىكى (4 خىل تىل)"
        Language.ENGLISH -> "Language (4 Languages)"
        Language.TURKISH -> "Dil Seçimi (4 Dil)"
        Language.CHINESE -> "语言选择 (4种语言)"
    }

    val themeSectionTitle = when (currentLanguage) {
        Language.UYGHUR -> "كېچە-كۈندۈزلۈك تەڭشەك (قارا ئەندىزە)"
        Language.ENGLISH -> "Day / Night Mode (Dark Theme)"
        Language.TURKISH -> "Gece / Gündüz Modu (Karanlık Tema)"
        Language.CHINESE -> "日夜模式切换 (暗黑模式)"
    }

    val fontSectionTitle = when (currentLanguage) {
        Language.UYGHUR -> "ئۇيغۇرچە خەت چوڭلۇقى تەڭشىكى"
        Language.ENGLISH -> "Font Size Scaling"
        Language.TURKISH -> "Yazı Boyutu Ölçeği"
        Language.CHINESE -> "字体大小与排版比例"
    }

    val fontScaleText = when (currentLanguage) {
        Language.UYGHUR -> "نۇسخا چوڭلۇقى: "
        Language.ENGLISH -> "Current Font Scale: "
        Language.TURKISH -> "Yazı Ölçeği: "
        Language.CHINESE -> "当前字体比例: "
    }

    val aboutSectionTitle = when (currentLanguage) {
        Language.UYGHUR -> "ئۇيغۇر تىبابىتى ھەققىدە"
        Language.ENGLISH -> "About Uyghur Traditional Medicine"
        Language.TURKISH -> "Uygur Geleneksel Tıbbı Hakkında"
        Language.CHINESE -> "关于维吾尔传统医药文化"
    }

    val aboutBody = when (currentLanguage) {
        Language.UYGHUR -> "ئۇيغۇر تىبابىتى 2500 يىلدىن ئارتۇق تارىخقا ئىگە، يىپەك يولى تېببىي مەدەنىيىتىنىڭ جەۋھىرى. ئۇ كىشىلىك تەبىئەتنىڭ ئىسسىق، سوغۇق، ھۆل، قۇرۇقتىن ئىبارەت تۆت مىزاجى ۋە تەبىئىي ئۆسۈملۈك بىلەن داۋالاش سىستېمىسىغا ئاساسلىنىدۇ."
        Language.ENGLISH -> "Uyghur Traditional Medicine (Tibabiti) spans over 2,500 years of Silk Road healing heritage. It harmonizes body balance through the 4 Humors (Mizaj) system and natural herbal medicine."
        Language.TURKISH -> "Uygur Tıbbı, İpek Yolu kültürünün 2500 yıllık şifa mirasıdır. Dört mizaç (Sıcak, Soğuk, Nemli, Kuru) ve doğal bitkisel reçetelerle vücudu dengeler."
        Language.CHINESE -> "维吾尔医药学拥有2500多年的古丝绸之路传承，遵循‘四体液与四体质（热、寒、湿、燥）’天然调理理论。"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Settings",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = headerTitle,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )

                    Text(
                        text = headerSub,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 1. Language Selection Card (4 Languages)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = langSectionTitle,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Language.entries.forEach { lang ->
                    val isSelected = currentLanguage == lang
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onLanguageChange(lang) }
                            .padding(vertical = 8.dp, horizontal = 4.dp)
                            .testTag("lang_option_${lang.code}"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onLanguageChange(lang) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.primary
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${lang.nativeName} (${lang.displayName})",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Active",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Day / Night Mode Toggle
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = "Theme",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = themeSectionTitle,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )

                        Text(
                            text = if (isDarkMode) "Night / Dark Mode" else "Day / Light Mode",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                Switch(
                    checked = isDarkMode,
                    onCheckedChange = onDarkModeToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("dark_mode_switch")
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Uyghur Font Size Adjustment
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FormatSize,
                        contentDescription = "Font Size",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = fontSectionTitle,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = fontSizeScale,
                    onValueChange = onFontScaleChange,
                    valueRange = 0.85f..1.25f,
                    steps = 3,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary
                    )
                )

                Text(
                    text = "$fontScaleText ${(fontSizeScale * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Official Links & Online Marketplace
        val context = LocalContext.current
        fun openUrl(url: String) {
            try {
                val fullUrl = if (url.startsWith("http://") || url.startsWith("https://")) url else "https://$url"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fullUrl))
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val socialTitle = when (currentLanguage) {
            Language.UYGHUR -> "ئورگان ئۇلانمىلار"
            Language.ENGLISH -> "Official Links"
            Language.TURKISH -> "Resmi Bağlantılar"
            Language.CHINESE -> "官方链接"
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = socialTitle,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Official Website
                Surface(
                    onClick = { openUrl("https://www.uyghurmedicine.com") },
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFEBF7EE),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🌐 تور بېكەت ئادرىسىمىز (Uyghur Tıbbi): www.uyghurmedicine.com",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20)
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Instagram
                Surface(
                    onClick = { openUrl("https://www.instagram.com/uygur_tibbi") },
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFDF0F5),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📷 ئىنستىگرام (Instagram): https://www.instagram.com/uygur_tibbi",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF833AB4)
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Facebook
                Surface(
                    onClick = { openUrl("https://www.facebook.com/uygurtibbi") },
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFEFF5FE),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🌐 فېيسبوك (Facebook): https://www.facebook.com/uygurtibbi",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1877F2)
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Marketplace
                Surface(
                    onClick = { openUrl("https://www.teklimakan.com") },
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFEF8ED),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🛍️ مەھسۇلات بازىرى (Online Pazaryeri): www.teklimakan.com",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD35400)
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // WhatsApp
                Surface(
                    onClick = { openUrl("https://wa.me/+905551609999") },
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFE8F8F0),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "💬 WhatsApp: https://wa.me/+905551609999",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF128C7E)
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5. About Uyghur Medicine heritage
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "About",
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = aboutSectionTitle,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = aboutBody,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
