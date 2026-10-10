package com.example.data.model

data class MedicinalPlant(
    val id: Int,
    val nameUy: String,
    val nameEn: String,
    val nameTr: String,
    val nameZh: String,
    val latinName: String,
    val category: PlantCategory,
    val mizajType: MizajType,
    val mizajDegreeUy: String,
    val mizajDegreeEn: String,
    val mizajDegreeTr: String,
    val mizajDegreeZh: String,
    val benefitsUy: String,
    val benefitsEn: String,
    val benefitsTr: String,
    val benefitsZh: String,
    val usageUy: String,
    val usageEn: String,
    val usageTr: String,
    val usageZh: String,
    val cautionUy: String,
    val cautionEn: String,
    val cautionTr: String,
    val cautionZh: String,
    val organTargetUy: String,
    val organTargetEn: String,
    val organTargetTr: String,
    val organTargetZh: String,
    val iconEmoji: String,
    val imageResId: Int? = null,
    val imageUrl: String? = null,
    val imageFit: String? = "contain",
    val isFeatured: Boolean = false,
    val publishTarget: String? = "ALL"
) {
    fun getName(language: Language): String = when (language) {
        Language.UYGHUR -> nameUy
        Language.TURKISH -> nameTr
        Language.ENGLISH -> nameEn
    }

    fun getMizajDegree(language: Language): String = when (language) {
        Language.UYGHUR -> mizajDegreeUy
        Language.TURKISH -> mizajDegreeTr
        Language.ENGLISH -> mizajDegreeEn
    }

    fun getBenefits(language: Language): String = when (language) {
        Language.UYGHUR -> benefitsUy
        Language.TURKISH -> benefitsTr
        Language.ENGLISH -> benefitsEn
    }

    fun getUsage(language: Language): String = when (language) {
        Language.UYGHUR -> usageUy
        Language.TURKISH -> usageTr
        Language.ENGLISH -> usageEn
    }

    fun getCaution(language: Language): String = when (language) {
        Language.UYGHUR -> cautionUy
        Language.TURKISH -> cautionTr
        Language.ENGLISH -> cautionEn
    }

    fun getOrganTarget(language: Language): String = when (language) {
        Language.UYGHUR -> organTargetUy
        Language.TURKISH -> organTargetTr
        Language.ENGLISH -> organTargetEn
    }

    fun matchesQuery(query: String): Boolean {
        if (query.isBlank()) return true
        val q = query.lowercase().trim()
        return nameUy.lowercase().contains(q) ||
                nameEn.lowercase().contains(q) ||
                nameTr.lowercase().contains(q) ||
                nameZh.lowercase().contains(q) ||
                latinName.lowercase().contains(q) ||
                benefitsUy.lowercase().contains(q) ||
                benefitsEn.lowercase().contains(q)
    }
}
