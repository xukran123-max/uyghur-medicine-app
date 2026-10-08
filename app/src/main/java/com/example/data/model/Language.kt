package com.example.data.model

enum class Language(val code: String, val displayName: String, val nativeName: String, val isRtl: Boolean) {
    UYGHUR("ug", "Uyghur", "ئۇيغۇرچە", true),
    ENGLISH("en", "English", "English", false),
    TURKISH("tr", "Turkish", "Türkçe", false),
    CHINESE("zh", "Chinese", "中文", false)
}

enum class MizajType(
    val nameUy: String,
    val nameEn: String,
    val nameTr: String,
    val nameZh: String,
    val colorHex: Long
) {
    HOT_DRY("ئىسسىق قۇرۇق", "Hot & Dry", "Sıcak & Kuru", "燥热", 0xFFD35400),
    HOT_MOIST("ئىسسىق ھۆل", "Hot & Moist", "Sıcak & Nemli", "湿热", 0xFFC0392B),
    COLD_DRY("سوغۇق قۇرۇق", "Cold & Dry", "Soğuk & Kuru", "燥寒", 0xFF2980B9),
    COLD_MOIST("سوغۇق ھۆل", "Cold & Moist", "Soğuk & Nemli", "湿寒", 0xFF16A085),
    NEUTRAL("مۇۆتەدىل", "Balanced", "Dengeli", "平和", 0xFF27AE60);

    fun getDisplayName(language: Language): String = when (language) {
        Language.UYGHUR -> nameUy
        Language.ENGLISH -> nameEn
        Language.TURKISH -> nameTr
        Language.CHINESE -> nameZh
    }
}

enum class PlantCategory(
    val nameUy: String,
    val nameEn: String,
    val nameTr: String,
    val nameZh: String,
    val iconName: String
) {
    HERB("ئۆسۈملۈكلەر", "Herbs & Plants", "Bitkiler", "草本植物", "eco"),
    FRUIT("مېۋە-چىۋىلەر", "Fruits & Nuts", "Meyve & Yemişler", "水果坚果", "nutrition"),
    SPICE("دورا-دەرمەكلەر", "Spices & Remedies", "Baharat & Tıbbi İlaçlar", "香料生药", "local_pharmacy"),
    FLOWER("گىياھلار", "Botanical Herbs", "Şifalı Bitkiler", "药用草本", "local_florist"),
    SEED("پىششىق دورىلار", "Compound Remedies", "Mürekkeep İlaçlar", "成药复方", "medication");

    fun getDisplayName(language: Language): String = when (language) {
        Language.UYGHUR -> nameUy
        Language.ENGLISH -> nameEn
        Language.TURKISH -> nameTr
        Language.CHINESE -> nameZh
    }
}
