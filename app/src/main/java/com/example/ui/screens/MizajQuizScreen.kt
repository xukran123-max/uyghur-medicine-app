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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.data.model.MedicinalPlant
import com.example.data.model.MizajType
import com.example.data.model.PlantRepository
import com.example.ui.components.PlantCard

@Composable
fun MizajQuizScreen(
    currentLanguage: Language,
    savedMizaj: MizajType?,
    onSaveMizaj: (MizajType) -> Unit,
    onClearMizaj: () -> Unit,
    favoriteIds: Set<Int>,
    onFavoriteToggle: (Int) -> Unit,
    onPlantClick: (MedicinalPlant) -> Unit
) {
    val selectedAnswers = remember { mutableStateMapOf<Int, Int>() }
    var calculatedMizaj by remember { mutableStateOf<MizajType?>(null) }

    val activeMizaj = savedMizaj ?: calculatedMizaj

    val questions = listOf(
        QuizQuestion(
            id = 1,
            titleUy = "1. تەبىئەت ئىسسىقلىقى ۋە ھاۋا ئىنكاسى (Body Temperature Sensation)",
            titleEn = "1. Reaction to heat & cold weather (Body Temperature Sensation)",
            titleTr = "1. Vücut Isı Algısı ve Hava Koşullarına Tepki",
            options = listOf(
                QuizOption(MizajType.HOT_DRY, "ئىسسىققا بەك سەزگۈر، يازدا بەك تەمتىلەيمەن", "Extremely sensitive to heat, prone to restlessness in summer", "Sıcağa çok hassasım, yazın daralırım"),
                QuizOption(MizajType.COLD_DRY, "سوغۇققا بەك سەزگۈر، قول-پۇتۇم دائىم مۇزلايدۇ", "Sensitive to cold, cold hands & feet", "Soğuğa çok hassasım, ellerim ve ayaklarım soğuk"),
                QuizOption(MizajType.NEUTRAL, "ئىسسىق بىلەن سوغۇققا تەڭپۇڭ، نورمال ئىنكاس قايتۇرىمەن", "Balanced response to all temperatures", "Sıcak ve soğuğa karşı dengeli")
            )
        ),
        QuizQuestion(
            id = 2,
            titleUy = "2. تېرىنىڭ رەڭگى ۋە نەم-قۇرۇقلۇقى (Skin Texture & Complexion)",
            titleEn = "2. Skin texture, dryness & complexion (Skin Characteristics)",
            titleTr = "2. Cilt Dokusu, Nem ve Ten Rengi Özellikleri",
            options = listOf(
                QuizOption(MizajType.HOT_DRY, "تېرەم قۇرۇق، داغ ياكى دانىخورەك چىقىشقا مايىل", "Dry skin, prone to spots or roughness", "Kuru ve lekelenmeye yatkın cilt"),
                QuizOption(MizajType.COLD_MOIST, "تېرەم ھۆل يۇمشاق، تەرقاڭ ئوڭاي چىقىدۇ", "Soft, moist skin, sweats easily", "Yumuşak, nemli cilt, kolay terler"),
                QuizOption(MizajType.NEUTRAL, "تېرەم تەبىئىي، قۇرۇق بىلەن ھۆل تەڭپۇڭ", "Naturally smooth & balanced skin", "Doğal, pürüzsüz ve dengeli cilt")
            )
        ),
        QuizQuestion(
            id = 3,
            titleUy = "3. تومۇر ۋە يۈرەك سوقۇشى (Tebip Pulse Diagnostic)",
            titleEn = "3. Pulse strength & heart rate (Pulse Diagnostic)",
            titleTr = "3. Nabız Hızı ve Kalp Atış Yapısı (Nabız Teşhisi)",
            options = listOf(
                QuizOption(MizajType.HOT_DRY, "تومۇر سوقۇشىم تېز، كۈچلۈك ۋە بېسىملىق", "Fast, strong, high-pressure pulse", "Hızlı, güçlü ve basınçlı nabız"),
                QuizOption(MizajType.COLD_MOIST, "تومۇر سوقۇشىم ئاستا، چوڭقۇر ۋە سۇس", "Slow, deep, low-pressure pulse", "Yavaş, derin ve zayıf nabız"),
                QuizOption(MizajType.NEUTRAL, "تومۇر سوقۇشىم ئوتتۇرىھال، تەڭپۇڭ", "Moderate, steady & balanced pulse", "Orta hızda ve dengeli nabız")
            )
        ),
        QuizQuestion(
            id = 4,
            titleUy = "4. ئاشقازان ۋە ھەزىم قىلىش ئىقتىدارى (Digestion & Appetite)",
            titleEn = "4. Digestion speed & stomach capacity (Digestion Analysis)",
            titleTr = "4. Mide Hacmi ve Sindirim Hızı",
            options = listOf(
                QuizOption(MizajType.HOT_MOIST, "ھەزىم قىلىشىم تېز، ئىشتاھىم ئوچۇق، ئاسان ئاچقۇرىمەن", "Rapid digestion, robust appetite", "Hızlı sindirim, iştahlı"),
                QuizOption(MizajType.COLD_MOIST, "ھەزىم قىلىشىم سۇس، ئاشقازىنىم يەل يىغىپ كۆپىدۇ", "Slow digestion, prone to bloating", "Yavaş sindirim, mide şişkinliği"),
                QuizOption(MizajType.NEUTRAL, "ئاشقازىنىم نورمال، ھەزىم تەڭپۇڭ", "Regular, efficient digestion", "Normal ve düzenli sindirim")
            )
        ),
        QuizQuestion(
            id = 5,
            titleUy = "5. ئۇيقۇ ۋە روھىي جىددىيلىك (Sleep Quality & Energy)",
            titleEn = "5. Sleep pattern & nervous system state (Sleep & Mental State)",
            titleTr = "5. Uyku Kalitesi ve Zihinsel Enerji Durumu",
            options = listOf(
                QuizOption(MizajType.HOT_DRY, "ئۇيقۇم يېنىك، روھىي ھالىتىم زىيادە جىددىي ۋە ھەرىكەتچان", "Light sleep, highly active tense mind", "Hafif uyku, zihinsel heyecan ve gerginlik"),
                QuizOption(MizajType.COLD_MOIST, "ئۇيقۇم بەك ئېغىر، سەھەردە قوپۇش ئېغىر ۋە ھارغىن", "Heavy deep sleep, sluggish morning wake", "Ağır uyku, sabahları yorgun kalkma"),
                QuizOption(MizajType.NEUTRAL, "ئۇيقۇم ئاراملىق، تەڭپۇڭ روھلۇق", "Restful, high quality sleep", "Dinlendirici ve kaliteli uyku")
            )
        ),
        QuizQuestion(
            id = 6,
            titleUy = "6. ئېغىز تەمى ۋە تەشنا بولۇش (Mouth Taste & Thirst Level)",
            titleEn = "6. Oral taste sensation & hydration thirst level",
            titleTr = "6. Ağız Tadı ve Susuzluk Hissi",
            options = listOf(
                QuizOption(MizajType.HOT_DRY, "ئېغىزىم ئاچچىق، تەشنا بولۇشچانلىقىم يۇقىرى، سۇ كۆپ ئىچىمەن", "Bitter mouth taste, frequent high thirst", "Ağızda acılık, sık susama hissi"),
                QuizOption(MizajType.COLD_MOIST, "ئېغىزىم تاتلىق ياكى تەمى يوق، ئۇسسۇزلۇق ئاز", "Sweet/bland mouth taste, low thirst", "Ağızda tatsızlık veya tatlılık, az susama"),
                QuizOption(MizajType.NEUTRAL, "ئېغىز تەمىم تەبىئىي، ئۇسسۇزلۇق نورمال", "Normal taste, natural thirst level", "Normal ağız tadı ve susuzluk düzeyi")
            )
        ),
        QuizQuestion(
            id = 7,
            titleUy = "7. بەدەن تۇرقى ۋە سۆڭەك گۆش تۇزۇلۈشى (Body Physique & Muscle Frame)",
            titleEn = "7. Musculoskeletal frame & body physique",
            titleTr = "7. Vücut Yapısı ve Kas/Kemik Çatısı",
            options = listOf(
                QuizOption(MizajType.HOT_DRY, "بەدىنىم ئورۇق، سۆڭىكىم كۆرىنىپ تورىدۇ", "Lean frame, defined bones & muscles", "İnce, kemikli ve zayıf vücut yapısı"),
                QuizOption(MizajType.COLD_MOIST, "بەدىنىم تولۇق، گۆشلۈك ۋە يۇمشاق", "Fuller frame, softer tissue structure", "Dolgun, yumuşak dokulu vücut yapısı"),
                QuizOption(MizajType.NEUTRAL, "بەدىنىم كېلىشكەن، سۆڭەك بىلەن گۆش تەڭپۇڭ", "Proportional, medium athletic frame", "Orantılı ve dengeli vücut yapısı")
            )
        ),
        QuizQuestion(
            id = 8,
            titleUy = "8. بۆرەك ئېنېرگىيەسى ۋە چىدامچانلىق (Kidney Energy & Endurance)",
            titleEn = "8. Physical endurance & lower back energy (Kidney Strength)",
            titleTr = "8. Bel-Böbrek Enerjisi ve Dayanıklılık",
            options = listOf(
                QuizOption(MizajType.HOT_MOIST, "ئېنېرگىيەم يۇقىرى، كۈچۈم ئۇرۇپ تورىدۇ", "High vitality & strong physical endurance", "Yüksek enerji ve güçlü fiziki dayanıklılık"),
                QuizOption(MizajType.COLD_DRY, "پۇت-بېلىم سۇس، ئاسان ھارغىنلىق ھېس قىلىمەن", "Fatigue prone, cold back/knees", "Bacak ve belde halsizlik, çabuk yorulma"),
                QuizOption(MizajType.NEUTRAL, "چىدامچانلىقىم ياخشى، ھارغىنلىق ئاز", "Good steady physical endurance", "İyi ve sürekli fiziki dayanıklılık")
            )
        ),
        QuizQuestion(
            id = 9,
            titleUy = "9. چىقىرىش ئىقتىدارى ۋە چىقىرىندى ئالاھىدىلىكى (Excretion Profile)",
            titleEn = "9. Perspiration, urination & metabolic excretion traits",
            titleTr = "9. Terleme ve Boşaltım Özellikleri",
            options = listOf(
                QuizOption(MizajType.HOT_DRY, "تېرىم باراقسان تەرلەيدۇ، كىچىك تەرىتىم سېرىق ۋە ئىسسىق", "Heavy sweat, warm yellow urination", "Yoğun terleme, koyu sarı idrar"),
                QuizOption(MizajType.COLD_MOIST, "تېرىم ئاز تەرلەيدۇ، كىچىك تەرىتىم ئاق ياكى سۇس", "Light sweat, pale clear urination", "Az terleme, açık renk idrar"),
                QuizOption(MizajType.NEUTRAL, "تەر ۋە كىچىك تەرەت تەبىئىي تەڭپۇڭ", "Normal perspiration & excretion color", "Dengeli terleme ve boşaltım")
            )
        )
    )

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
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "Mizaj",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = when (currentLanguage) {
                            Language.UYGHUR -> "9 تۈر بويىچە ئانالىز قىلىش جەدۋىلى"
                            Language.ENGLISH -> "9-Category Mizaj Analysis Table"
                            Language.TURKISH -> "9 Maddelik Mizaç Analiz Cetveli"
                        },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )

                    Text(
                        text = when (currentLanguage) {
                            Language.UYGHUR -> "ئۇيغۇر تىبابىتى 9 تۈرلۈك تەنسىھەت ئانالىز كۆرسەتكۈچى بويىچە بېكىتىش"
                            Language.ENGLISH -> "Comprehensive 9-pillar diagnostic analysis of temperament"
                            Language.TURKISH -> "9 temel mizaç ve sağlık göstergesiyle detaylı analiz"
                        },
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (activeMizaj != null) {
            val descriptionText = when (activeMizaj) {
                MizajType.HOT_DRY -> when (currentLanguage) {
                    Language.UYGHUR -> "ئىسسىق قۇرۇق مىزاجلىقلار سوغۇق ھۆل خاسىيەتلىك ئانار، نىلۇپەر ۋە ئىسپاغۇل ئارقىلىق ئىسسىقىنى تەڭشىشى لازىم."
                    Language.ENGLISH -> "Hot & Dry temperaments benefit from cooling Moist remedies like Pomegranate, Water Lily and Psyllium."
                    Language.TURKISH -> "Sıcak & Kuru mizaçlılar Nar, Nilüfer ve Karnıyarık otu gibi Soğuk & Nemli gıdalar tüketmelidir."
                }
                MizajType.COLD_DRY -> when (currentLanguage) {
                    Language.UYGHUR -> "سوغۇق قۇرۇق مىزاجلىقلار ئىسسىق ھۆل خاسىيەتلىك شېرىنمىيە، بادام ۋە خۇرما بىلەن بۆرەكنى ئىسسىتىشى كېرەك."
                    Language.ENGLISH -> "Cold & Dry temperaments benefit from warming Moist remedies like Licorice, Almonds and Dates."
                    Language.TURKISH -> "Soğuk & Kuru mizaçlılar Meyan kökü, Badem ve Hurma gibi Sıcak & Nemli besinler tüketmelidir."
                }
                MizajType.COLD_MOIST -> when (currentLanguage) {
                    Language.UYGHUR -> "سوغۇق ھۆل مىزاجلىقلار ئىسسىق قۇرۇق خاسىيەتلىك زەنجىپىل، قارا دانە، زىرا بىلەن ئاشقازىنىنى قۇۋۋەتلىشى لازىم."
                    Language.ENGLISH -> "Cold & Moist temperaments benefit from Hot & Dry herbs like Ginger, Black Seed and Cumin."
                    Language.TURKISH -> "Soğuk & Nemli mizaçlılar Zencefil, Çörek otu ve Kimyon gibi Sıcak & Kuru bitkiler seçmelidir."
                }
                else -> when (currentLanguage) {
                    Language.UYGHUR -> "مۇۆتەدىل تەڭپۇڭ مىزاجلىقلار بارلىق تەبىئىي دورىلىق ئۆسۈملۈكلەرنى مۇۋاپىق مىقداردا ئىستېمال قىلسا بولىدۇ."
                    Language.ENGLISH -> "Balanced temperaments can maintain health with moderate intake of naturally diverse herbs."
                    Language.TURKISH -> "Dengeli mizaçlılar tüm doğal bitkileri ölçülü olarak tüketebilir."
                }
            }

            // Display Result Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(activeMizaj.colorHex).copy(alpha = 0.15f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Result",
                        tint = Color(activeMizaj.colorHex),
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = when (currentLanguage) {
                            Language.UYGHUR -> "سىزنىڭ مىزاجىڭىز:"
                            Language.ENGLISH -> "Your Diagnosed Mizaj:"
                            Language.TURKISH -> "Teşhis Edilen Mizacınız:"
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    Text(
                        text = activeMizaj.getDisplayName(currentLanguage),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(activeMizaj.colorHex)
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = descriptionText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            onClearMizaj()
                            calculatedMizaj = null
                        },
                        modifier = Modifier.testTag("retake_quiz_button")
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = when (currentLanguage) {
                                Language.UYGHUR -> "قايتا ئېنىقلاش"
                                Language.ENGLISH -> "Retake Test"
                                Language.TURKISH -> "Testi Tekrarla"
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Recommended Herbs List
            Text(
                text = when (currentLanguage) {
                    Language.UYGHUR -> "مىزاجىڭىزغا ماس كېلىدىغان تەۋسىيەلىك دورىلار:"
                    Language.ENGLISH -> "Recommended Herbs for Your Mizaj:"
                    Language.TURKISH -> "Mizacınıza Uygun Şifalı Bitkiler:"
                },
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            val matchingHerbs = PlantRepository.plantsList.filter { plant ->
                when (activeMizaj) {
                    MizajType.HOT_DRY -> plant.mizajType == MizajType.COLD_MOIST || plant.mizajType == MizajType.COLD_DRY
                    MizajType.COLD_MOIST -> plant.mizajType == MizajType.HOT_DRY || plant.mizajType == MizajType.HOT_MOIST
                    MizajType.COLD_DRY -> plant.mizajType == MizajType.HOT_MOIST || plant.mizajType == MizajType.HOT_DRY
                    else -> plant.isFeatured
                }
            }.take(6)

            matchingHerbs.forEach { plant ->
                Box(modifier = Modifier.padding(vertical = 4.dp)) {
                    PlantCard(
                        plant = plant,
                        currentLanguage = currentLanguage,
                        isFavorite = favoriteIds.contains(plant.id),
                        onFavoriteToggle = { onFavoriteToggle(plant.id) },
                        onClick = { onPlantClick(plant) }
                    )
                }
            }
        } else {
            // Questions List
            questions.forEach { q ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = q.getTitle(currentLanguage),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        q.options.forEachIndexed { index, option ->
                            val isSelected = selectedAnswers[q.id] == index
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedAnswers[q.id] = index },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = MaterialTheme.colorScheme.primary
                                    )
                                )

                                Text(
                                    text = option.getText(currentLanguage),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val counts = mutableMapOf<MizajType, Int>()
                    questions.forEach { q ->
                        val selectedIdx = selectedAnswers[q.id] ?: 0
                        val mizaj = q.options[selectedIdx].resultingMizaj
                        counts[mizaj] = (counts[mizaj] ?: 0) + 1
                    }
                    val topMizaj = counts.maxByOrNull { it.value }?.key ?: MizajType.NEUTRAL
                    calculatedMizaj = topMizaj
                    onSaveMizaj(topMizaj)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_quiz_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = when (currentLanguage) {
                        Language.UYGHUR -> "مىزاجىمنى ئېنىقلاش"
                        Language.ENGLISH -> "Calculate My Mizaj"
                        Language.TURKISH -> "Mizacımı Hesapla"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }
    }
}

private data class QuizQuestion(
    val id: Int,
    val titleUy: String,
    val titleEn: String,
    val titleTr: String,
    val options: List<QuizOption>
) {
    fun getTitle(language: Language): String = when (language) {
        Language.UYGHUR -> titleUy
        Language.TURKISH -> titleTr
        Language.ENGLISH -> titleEn
    }
}

private data class QuizOption(
    val resultingMizaj: MizajType,
    val uy: String,
    val en: String,
    val tr: String
) {
    fun getText(language: Language): String = when (language) {
        Language.UYGHUR -> uy
        Language.TURKISH -> tr
        Language.ENGLISH -> en
    }
}
