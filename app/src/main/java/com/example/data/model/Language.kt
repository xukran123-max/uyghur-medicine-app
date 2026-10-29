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
    XAM_DORILAR("خام دورىلار", "Raw Medicinal Herbs", "Ham Şifalı Bitkiler", "eco"),
    PISHSHIQ_DORILAR("پىششىق دورىلار", "Compound Remedies", "Mürekkeep İlaçlar", "medication"),
    ADDIY_RETSIPLAR("ئاددى رېتسىپلار", "Simple Home Recipes", "Şifalı Basit Reçeteler", "restaurant"),
    KESELLIKLER("كېسەللىكلەر", "Diseases & Treatments", "Hastalıklar & Tedavi", "healing"),
    TEBABET_MUNBIRI("تېبابەت مۇنبىرى", "Medicine Forum & Courses", "Tıp Forumu & Eğitimler", "school"),

    // Legacy enum entries for backward compatibility
    HERB("خام دورىلار", "Raw Medicinal Herbs", "Ham Şifalı Bitkiler", "eco"),
    FRUIT("خام دورىلار", "Raw Medicinal Herbs", "Ham Şifalı Bitkiler", "eco"),
    SPICE("ئاددى رېتسىپلار", "Simple Home Recipes", "Şifalı Basit Reçeteler", "restaurant"),
    FLOWER("خام دورىلار", "Raw Medicinal Herbs", "Ham Şifalı Bitkiler", "eco"),
    SEED("پىششىق دورىلار", "Compound Remedies", "Mürekkeep İlaçlar", "medication");

    fun getDisplayName(language: Language): String = when (language) {
        Language.UYGHUR -> nameUy
        Language.TURKISH -> nameTr
        Language.ENGLISH -> nameEn
    }

    companion object {
        val mainCategories = listOf(
            XAM_DORILAR,
            PISHSHIQ_DORILAR,
            ADDIY_RETSIPLAR,
            KESELLIKLER,
            TEBABET_MUNBIRI
        )
    }
}
