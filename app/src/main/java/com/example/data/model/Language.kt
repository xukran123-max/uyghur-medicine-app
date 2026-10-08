package com.example.data.model

enum class Language(val code: String, val displayName: String, val nativeName: String, val isRtl: Boolean) {
    UYGHUR("ug", "Uyghur", "ئۇيغۇرچە", true),
    TURKISH("tr", "Turkish", "Türkçe", false),
    ENGLISH("en", "English", "English", false);
}

enum class MizajType(
    val nameUy: String,
    val nameEn: String,
    val nameTr: String,
    val colorHex: Long
) {
    HOT_DRY("ئىسسىق قۇرۇق", "Hot & Dry", "Sıcak & Kuru", 0xFFD35400),
    HOT_MOIST("ئىسسىق ھۆل", "Hot & Moist", "Sıcak & Nemli", 0xFFC0392B),
    COLD_DRY("سوغۇق قۇرۇق", "Cold & Dry", "Soğuk & Kuru", 0xFF2980B9),
    COLD_MOIST("سوغۇق ھۆل", "Cold & Moist", "Soğuk & Nemli", 0xFF16A085),
    NEUTRAL("مۇۆتەدىل", "Balanced", "Dengeli", 0xFF27AE60);

    fun getDisplayName(language: Language): String = when (language) {
        Language.UYGHUR -> nameUy
        Language.TURKISH -> nameTr
        Language.ENGLISH -> nameEn
    }
}

enum class PlantCategory(
    val nameUy: String,
    val nameEn: String,
    val nameTr: String,
    val iconName: String
) {
    HERB("ئۆسۈملۈكلەر", "Herbs & Plants", "Bitkiler", "eco"),
    FRUIT("مېۋە-چىۋىلەر", "Fruits & Nuts", "Meyve & Yemişler", "nutrition"),
    SPICE("دورا-دەرمەكلەر", "Spices & Remedies", "Baharat & Tıbbi İlaçlar", "local_pharmacy"),
    FLOWER("گىياھلار", "Botanical Herbs", "Şifalı Bitkiler", "local_florist"),
    SEED("پىششىق دورىلار", "Compound Remedies", "Mürekkeep İlaçlar", "medication");

    fun getDisplayName(language: Language): String = when (language) {
        Language.UYGHUR -> nameUy
        Language.TURKISH -> nameTr
        Language.ENGLISH -> nameEn
    }
}
