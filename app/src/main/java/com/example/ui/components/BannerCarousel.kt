package com.example.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Language
import kotlinx.coroutines.delay

@Composable
fun BannerCarousel(
    currentLanguage: Language,
    onStartQuizClick: () -> Unit,
    onExploreClick: () -> Unit
) {
    var currentPage by remember { mutableIntStateOf(0) }

    val banners = listOf(
        BannerItem(
            titleUy = "ئۇيغۇر تىبابىتى دورىلىق ئۆسۈملۈكلەر",
            titleEn = "Classic Uyghur Medicinal Herbs & Remedies",
            titleTr = "Geleneksel Uygur Tıbbı Şifalı Bitkisi",
            titleZh = "维吾尔医药传统草本药典",
            subtitleUy = "مىزاج ۋە ئىشلىتىش ئۇسۇلى تولۇق كىرگۈزۈلدى",
            subtitleEn = "Comprehensive guide on Mizaj, benefits & recipes",
            subtitleTr = "Mizaç, fayda ve reçetelerle dolu rehber",
            subtitleZh = "包含体质、功效与实用药方全集",
            badgeUy = "ئۆسۈملۈكلەر جەدۋىلى",
            badgeEn = "Botanical Guide",
            badgeTr = "Bitki Rehberi",
            badgeZh = "草本典籍"
        ),
        BannerItem(
            titleUy = "مىزاجىڭىزنى ئېنىقلاپ بېقىڭ",
            titleEn = "Discover Your Personal Mizaj (Temperament)",
            titleTr = "Kendi Mizacınızı Keşfedin",
            titleZh = "测试并测算您的维药体质",
            subtitleUy = "ئىسسىق، سوغۇق، ھۆل، قۇرۇق مىزاجلارغا ئاساسەن يېمەكلىك تەڭشەڭ",
            subtitleEn = "Balance Hot, Cold, Moist & Dry temperaments with diet",
            subtitleTr = "Sıcak, Soğuk, Nemli ve Kuru mizaç dengesi",
            subtitleZh = "测算燥热湿寒，科学进行饮食调理",
            badgeUy = "مىزاج ئېنىقلاش",
            badgeEn = "Mizaj Quiz",
            badgeTr = "Mizaç Testi",
            badgeZh = "体质测算"
        ),
        BannerItem(
            titleUy = "ئۇيغۇر تىبابىتى كالىندارى ۋە پەسىللىك كۈتۈنۈش",
            titleEn = "Traditional Tibbi Health Calendar",
            titleTr = "Geleneksel Tıbbi Sağlık Takvimi",
            titleZh = "维药四季养生日历与每日宜忌",
            subtitleUy = "تۆت پەسىل بويىچە سالامەتلىك ئاسراش تەۋسىيەلىرى",
            subtitleEn = "Seasonal wellness advice according to traditional rules",
            subtitleTr = "Dört mevsim şifa ve bakım tavsiyeleri",
            subtitleZh = "遵从传统顺应四季的调理要领",
            badgeUy = "تەبىئىي كالىندار",
            badgeEn = "Health Calendar",
            badgeTr = "Sağlık Takvimi",
            badgeZh = "养生日历"
        ),
        BannerItem(
            titleUy = "تۆت خىلىت ۋە مىزاج تەڭپۇڭلۇقى",
            titleEn = "Four Humors & Temperamental Harmony",
            titleTr = "Dört Ahlat ve Mizaç Dengesi",
            titleZh = "四体液与体质平衡论",
            subtitleUy = "قان، سەپرا، سودا، بەلغەم تەڭپۇڭلۇقى بىلەن تەننى ساغلاملاشتۇرۇش",
            subtitleEn = "Maintain vital health by balancing body humors",
            subtitleTr = "Kan, Safra, Sevda ve Balgam dengesiyle sağlık",
            subtitleZh = "通过四体液平衡，调理身体内部环境",
            badgeUy = "مىزاج تەڭپۇڭلىقى",
            badgeEn = "Mizaj Balance",
            badgeTr = "Mizaç Dengesi",
            badgeZh = "体液平衡"
        ),
        BannerItem(
            titleUy = "شىپالىق شەربەتلەر ۋە چايلار",
            titleEn = "Healing Elixirs, Syrups & Teas",
            titleTr = "Şifalı Şerbetler ve Çaylar",
            titleZh = "天然药用草本茶与养生糖浆",
            subtitleUy = "ئانار، شىركەنچىبىن، نىلۇپەر ۋە گۈلقەنت ئىچىملىكلىرى",
            subtitleEn = "Pomegranate, Oxymel, Water Lily & Rose preserve teas",
            subtitleTr = "Nar, Sirkencubin, Nilüfer ve Gül reçeteleri",
            subtitleZh = "石榴茶、糖醋饮、睡莲与玫瑰浆煎服法",
            badgeUy = "شىپالىق چايلار",
            badgeEn = "Herbal Teas",
            badgeTr = "Bitki Çayları",
            badgeZh = "养生茶饮"
        ),
        BannerItem(
            titleUy = "كۈندىلىك سالامەتلىك ئاسراش تەۋسىيەلىرى",
            titleEn = "Daily Healthy Living & Diet Guidelines",
            titleTr = "Günlük Yaşam ve Beslenme Rehberi",
            titleZh = "每日膳食饮食与生活方式建议",
            subtitleUy = "ئورگاننى قۇۋۋەتلەش، ئاشقازان ۋە جىگەرنى ئاسراش ئۇسۇللىرى",
            subtitleEn = "Strengthen vital organs, liver, stomach & immunity",
            subtitleTr = "Hayati organlar, mide ve karaciğer koruma",
            subtitleZh = "调理脾胃、保肝补气与增强免役力",
            badgeUy = "كۈندىلىك ئاسراش",
            badgeEn = "Daily Care",
            badgeTr = "Günlük Bakım",
            badgeZh = "日常保健"
        ),
        BannerItem(
            titleUy = "تەبىئىي دورا ۋە رېتسىپلار",
            titleEn = "Natural Herbal Compounds & Preparations",
            titleTr = "Doğal Bitkisel Terkipler ve Hazırlıklar",
            titleZh = "维药传统复方与天然饮片",
            subtitleUy = "تەبىئىي دورىلارنىڭ تەركىبى، تەسىرى ۋە ئىشلىتىش قېيدىلىرى",
            subtitleEn = "Authentic natural compounds, active usage & dosage",
            subtitleTr = "Geleneksel doğal terkipler ve kullanım dozajı",
            subtitleZh = "传统草本复方的君臣佐使与配伍原则",
            badgeUy = "تەبىئىي تەبىئەت",
            badgeEn = "Natural Medicine",
            badgeTr = "Doğal İlaçlar",
            badgeZh = "天然草药"
        )
    )

    // Auto-scroll effect every 5 seconds (5000ms)
    LaunchedEffect(banners.size) {
        while (true) {
            delay(5000L)
            currentPage = (currentPage + 1) % banners.size
        }
    }

    val currentBanner = banners[currentPage]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.img_hero_banner),
                contentDescription = "Hero Banner",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.3f),
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // Banner Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Tag & Page Count Indicator (Matching reference image "1/7")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.secondary)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = currentBanner.getBadge(currentLanguage),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    // Indicator badge matching screenshot style "1/7"
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black.copy(alpha = 0.6f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${currentPage + 1}/${banners.size}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                // Middle Title & Subtitle
                Column {
                    Text(
                        text = currentBanner.getTitle(currentLanguage),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 17.sp
                        ),
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = currentBanner.getSubtitle(currentLanguage),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        ),
                        maxLines = 2
                    )
                }

                // Bottom Action & Arrow Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (currentPage == 1) onStartQuizClick() else onExploreClick()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Explore,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = when (currentLanguage) {
                                Language.UYGHUR -> "كۆرۈپ باقاي"
                                Language.ENGLISH -> "Explore"
                                Language.TURKISH -> "Keşfet"
                                Language.CHINESE -> "查看"
                            },
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    // Slide Arrows
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                currentPage = if (currentPage > 0) currentPage - 1 else banners.size - 1
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = "Prev",
                                tint = Color.White
                            )
                        }

                        IconButton(
                            onClick = {
                                currentPage = (currentPage + 1) % banners.size
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Next",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class BannerItem(
    val titleUy: String,
    val titleEn: String,
    val titleTr: String,
    val titleZh: String,
    val subtitleUy: String,
    val subtitleEn: String,
    val subtitleTr: String,
    val subtitleZh: String,
    val badgeUy: String,
    val badgeEn: String,
    val badgeTr: String,
    val badgeZh: String
) {
    fun getTitle(language: Language): String = when (language) {
        Language.UYGHUR -> titleUy
        Language.ENGLISH -> titleEn
        Language.TURKISH -> titleTr
        Language.CHINESE -> titleZh
    }

    fun getSubtitle(language: Language): String = when (language) {
        Language.UYGHUR -> subtitleUy
        Language.ENGLISH -> subtitleEn
        Language.TURKISH -> subtitleTr
        Language.CHINESE -> subtitleZh
    }

    fun getBadge(language: Language): String = when (language) {
        Language.UYGHUR -> badgeUy
        Language.ENGLISH -> badgeEn
        Language.TURKISH -> badgeTr
        Language.CHINESE -> badgeZh
    }
}
