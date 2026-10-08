package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CalendarScreen(
    currentLanguage: Language
) {
    val dateString = remember {
        val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
        sdf.format(Date())
    }

    val dailyTasks = remember {
        mutableStateMapOf(
            1 to true,
            2 to false,
            3 to false,
            4 to false
        )
    }

    val headerTitle = when (currentLanguage) {
        Language.UYGHUR -> "ئۇيغۇر تىبابىتى كالىندارى"
        Language.ENGLISH -> "Uyghur Tibbi Calendar"
        Language.TURKISH -> "Uygur Tıbbı Takvimi"
    }

    val seasonTag = when (currentLanguage) {
        Language.UYGHUR -> "ياز پەسلى"
        Language.ENGLISH -> "Summer"
        Language.TURKISH -> "Yaz Mevsimi"
    }

    val hijriDate = when (currentLanguage) {
        Language.UYGHUR -> "ھىجرىيە 1448-يىلى 2-ئاي"
        Language.ENGLISH -> "Hijri Month 2, 1448 AH"
        Language.TURKISH -> "Hicri 2. Ay, 1448"
    }

    val adviceTitle = when (currentLanguage) {
        Language.UYGHUR -> "ياز پەسلىدىكى مىزاج تەڭشەش ۋە ئاسراش"
        Language.ENGLISH -> "Summer Season Mizaj Guidelines"
        Language.TURKISH -> "Yaz Mevsimi Mizaç Bakım Rehberi"
    }

    val adviceBody = when (currentLanguage) {
        Language.UYGHUR -> "ياز پەسلىنىڭ مىزاجى ئىسسىق ۋە قۇرۇق بولىدۇ. بۇ پەسىلدە سوغۇق ھۆل مىزاجلىق ئانار شەربىتى، نىلۇپەر چىيى بىلەن ئىسپىغۇل ئىچىپ جىگەر ھارارىتىنى قايتۇرۇش تەۋسىيە قىلىنىدۇ. ئاچچىق، ياغلىق تاماقلاردىن ئۆزىڭىزنى يىراق تۇتۇڭ."
        Language.ENGLISH -> "Summer's temperament is Hot & Dry. Consume cooling Moist remedies like Pomegranate juice, Water Lily tea and Psyllium to cool liver heat. Avoid greasy spiced foods."
        Language.TURKISH -> "Yaz mevsiminin mizacı Sıcak ve Kuru'dur. Nar suyu, nilüfer çayı ve karnıyarık otu gibi Soğuk & Nemli gıdalarla karaciğer ateşini düşürün. Acı ve yağlı gıdalardan kaçının."
    }

    val checklistTitle = when (currentLanguage) {
        Language.UYGHUR -> "كۈندىلىك سالامەتلىك ئاسراش جەدۋىلى"
        Language.ENGLISH -> "Daily Health Checklist"
        Language.TURKISH -> "Günlük Sağlık Takip Listesi"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Today's Date Banner Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Calendar",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = headerTitle,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.secondary)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = seasonTag,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = dateString,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )
                )

                Text(
                    text = hijriDate,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.secondary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Current Season Health Advice
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
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
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = "Summer",
                        tint = Color(0xFFD35400),
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = adviceTitle,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = adviceBody,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 22.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = checklistTitle,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        val tasksList = listOf(
            DailyTaskItem(1, "1. ئەتىگەندە ئاچ قورساقتا ھەسەل ئارىلاشتۇرۇلغان ئىللىق سۇ ئىچىش", "1. Drink warm honey water on empty stomach in the morning", "1. Sabah aç karnına ılık ballı su içmek"),
            DailyTaskItem(2, "2. چۈشتە 15 مىنۇت مېڭىنى ئارام ئالدۇرۇش (مېڭە ئاسراش)", "2. 15 min mental & brain rest at midday", "2. Öğlen 15 dakika zihin ve beyin dinlendirmesi"),
            DailyTaskItem(3, "3. چايغا لاچىندانە، رۇمبەدىيان ياكى زەنجىپىل سېلىپ ئىچىش", "3. Add cardamom, anise or ginger to daily herbal tea", "3. Çaya kakule, anason veya zencefil eklemek"),
            DailyTaskItem(4, "4. كۈندۈزدە بادام، ياڭاق بىلەن ئەنجۇر ياكى خۇرما يېيىش", "4. Eat almonds, walnuts, dried figs or dates as mid-day snack", "4. Gün içinde badem, ceviz, incir veya hurma tüketmek"),
            DailyTaskItem(5, "5. كەچتە پۇتنى ئىللىق سۇدا شىپالىق تۇز بىلەن چىلاش", "5. Soak feet in warm herbal salt water before sleeping", "5. Akşam yatmadan önce ayakları ılık tuzlu suda bekletmek"),
            DailyTaskItem(6, "6. كېچە سائەت 11 دىن بۇرۇن ئۇخلاپ مىزاج تەڭپۇڭلۇقىنى ساقلاش", "6. Sleep before 11 PM to preserve internal Mizaj balance", "6. Mizaç dengesini korumak için saat 23:00'ten önce uyumak"),
            DailyTaskItem(7, "7. كۈندە 20 مىنۇت يېنىق ھەرىكەت ۋە چوڭ نەپەس ئېلىش مەشىقى قىلىش", "7. 20 min light physical activity & deep breathing exercise", "7. Günde 20 dakika hafif egzersiz ve derin nefes çalışması")
        )

        tasksList.forEach { task ->
            val isChecked = dailyTasks[task.id] == true
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { checked ->
                            dailyTasks[task.id] = checked
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.testTag("daily_task_check_${task.id}")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = task.getLabel(currentLanguage),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (isChecked) FontWeight.Normal else FontWeight.Medium,
                            color = if (isChecked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }
    }
}

private data class DailyTaskItem(
    val id: Int,
    val uy: String,
    val en: String,
    val tr: String
) {
    fun getLabel(language: Language): String = when (language) {
        Language.UYGHUR -> uy
        Language.ENGLISH -> en
        Language.TURKISH -> tr
    }
}
