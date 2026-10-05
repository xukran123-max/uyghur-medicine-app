package com.example.data.model

enum class TraditionalProperty(
    val nameUy: String,
    val nameEn: String,
    val nameTr: String,
    val nameZh: String,
    val colorHex: Long,
    val emoji: String
) {
    HOT("ئىسسىق", "Hot", "Sıcak", "热", 0xFFD35400, "🔥"),
    COLD("سوغۇق", "Cold", "Soğuk", "寒", 0xFF0288D1, "❄️"),
    DRY("قۇرۇق", "Dry", "Kuru", "燥", 0xFFE67E22, "🌵"),
    MOIST("ھۆل", "Moist", "Nemli", "湿", 0xFF0097A7, "💧");

    fun getDisplayName(language: Language): String = when (language) {
        Language.UYGHUR -> nameUy
        Language.ENGLISH -> nameEn
        Language.TURKISH -> nameTr
        Language.CHINESE -> nameZh
    }
}
