package com.example.data.model

enum class TraditionalProperty(
    val nameUy: String,
    val nameEn: String,
    val nameTr: String,
    val colorHex: Long,
    val emoji: String
) {
    HOT("ئىسسىق", "Hot", "Sıcak", 0xFFD35400, "🔥"),
    COLD("سوغۇق", "Cold", "Soğuk", 0xFF0288D1, "❄️"),
    DRY("قۇرۇق", "Dry", "Kuru", 0xFFE67E22, "🌵"),
    MOIST("ھۆل", "Moist", "Nemli", 0xFF0097A7, "💧");

    fun getDisplayName(language: Language): String = when (language) {
        Language.UYGHUR -> nameUy
        Language.TURKISH -> nameTr
        Language.ENGLISH -> nameEn
    }
}
