package com.example.data.repository

import android.content.Context
import com.example.data.model.MedicinalPlant
import com.example.data.model.MizajType
import com.example.data.model.PlantCategory
import com.example.data.model.PlantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit

class PlantsStorageRepository(private val context: Context) {

    private val storageFile: File
        get() = File(context.filesDir, "custom_plants.json")

    private val httpClient = OkHttpClient.Builder()
        .followRedirects(true)
        .followSslRedirects(true)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val syncApiUrl = "https://uyghurmedicine.com/api/app/plants/"

    @Synchronized
    fun getPlants(): List<MedicinalPlant> {
        val file = storageFile
        if (!file.exists()) {
            val defaultList = PlantRepository.plantsList
            saveToFile(defaultList)
            return defaultList
        }

        return try {
            val jsonStr = file.readText(Charsets.UTF_8)
            val jsonArray = JSONArray(jsonStr)
            val list = mutableListOf<MedicinalPlant>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(jsonToPlant(obj))
            }
            if (list.isEmpty()) {
                PlantRepository.plantsList
            } else {
                list
            }
        } catch (e: Exception) {
            e.printStackTrace()
            PlantRepository.plantsList
        }
    }

    @Synchronized
    fun savePlant(plant: MedicinalPlant): List<MedicinalPlant> {
        val current = getPlants().toMutableList()
        val index = current.indexOfFirst { it.id == plant.id }
        if (index >= 0) {
            current[index] = plant
        } else {
            val newId = if (plant.id > 0 && current.none { it.id == plant.id }) {
                plant.id
            } else {
                (current.maxOfOrNull { it.id } ?: 0) + 1
            }
            current.add(0, plant.copy(id = newId))
        }
        saveToFile(current)
        return current
    }

    @Synchronized
    fun deletePlant(plantId: Int): List<MedicinalPlant> {
        val current = getPlants().filter { it.id != plantId }
        saveToFile(current)
        return current
    }

    @Synchronized
    fun resetToDefaults(): List<MedicinalPlant> {
        val defaultList = PlantRepository.plantsList
        saveToFile(defaultList)
        return defaultList
    }

    /**
     * Pulls latest plants updates from the server.
     * Returns true if local list was updated with server data.
     */
    suspend fun syncWithServer(): List<MedicinalPlant>? = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(syncApiUrl)
                .header("Accept", "application/json")
                .get()
                .build()

            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful) return@withContext null

            val responseBody = response.body?.string() ?: return@withContext null
            val json = JSONObject(responseBody)
            if (json.optBoolean("ok", false) && json.optBoolean("hasCustomPlants", false)) {
                val plantsArray = json.optJSONArray("plants") ?: return@withContext null
                val serverPlants = mutableListOf<MedicinalPlant>()
                for (i in 0 until plantsArray.length()) {
                    val obj = plantsArray.getJSONObject(i)
                    serverPlants.add(jsonToPlant(obj))
                }
                if (serverPlants.isNotEmpty()) {
                    saveToFile(serverPlants)
                    return@withContext serverPlants
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext null
    }

    /**
     * Pushes current local plant list to the server so all other users will receive it.
     */
    suspend fun pushToServer(plants: List<MedicinalPlant>, adminPassword: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val payload = JSONObject().apply {
                put("password", adminPassword)
                val arr = JSONArray()
                for (p in plants) {
                    arr.put(plantToJson(p))
                }
                put("plants", arr)
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = payload.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url(syncApiUrl)
                .header("X-Admin-Password", adminPassword)
                .post(requestBody)
                .build()

            val response = httpClient.newCall(request).execute()
            return@withContext response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext false
        }
    }

    private fun saveToFile(plants: List<MedicinalPlant>) {
        try {
            val jsonArray = JSONArray()
            for (plant in plants) {
                jsonArray.put(plantToJson(plant))
            }
            storageFile.writeText(jsonArray.toString(2), Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Uploads plant image from local URI to server / Cloudflare R2.
     * Falls back to local app storage if offline.
     */
    suspend fun uploadPlantImage(uri: Uri): Result<String> = withContext(Dispatchers.IO) {
        try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri)
                ?: return@withContext Result.failure(Exception("رەسىم ھۆججىتىنى ئوقۇغىلى بولمىدى"))
            val bytes = inputStream.use { it.readBytes() }

            if (bytes.size > 20 * 1024 * 1024) {
                return@withContext Result.failure(Exception("رەسىم ھەجىمى 20MB دىن ئاشماسلىقى كېرەك"))
            }

            val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
            val ext = when {
                mimeType.contains("png") -> ".png"
                mimeType.contains("webp") -> ".webp"
                else -> ".jpg"
            }
            val fileName = "app_upload_${System.currentTimeMillis()}$ext"

            // 1. Save local copy for instant offline availability
            val localDir = File(context.filesDir, "plant_images").apply { if (!exists()) mkdirs() }
            val localFile = File(localDir, fileName)
            localFile.writeBytes(bytes)

            // 2. Try online upload to server / R2
            try {
                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "file",
                        fileName,
                        bytes.toRequestBody(mimeType.toMediaTypeOrNull())
                    )
                    .build()

                val request = Request.Builder()
                    .url("https://uyghurmedicine.com/api/upload-image")
                    .header("Accept", "application/json")
                    .post(requestBody)
                    .build()

                val response = httpClient.newCall(request).execute()
                val responseBody = response.body?.string() ?: ""
                if (response.isSuccessful) {
                    val json = JSONObject(responseBody)
                    if (json.optBoolean("ok", false)) {
                        val serverUrl = json.optString("url")
                        if (serverUrl.isNotEmpty()) {
                            return@withContext Result.success(serverUrl)
                        }
                    }
                }
            } catch (netErr: Exception) {
                netErr.printStackTrace()
            }

            // Fallback to local file URI if offline or network failure
            Result.success(localFile.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    private fun plantToJson(plant: MedicinalPlant): JSONObject {
        return JSONObject().apply {
            put("id", plant.id)
            put("nameUy", plant.nameUy)
            put("nameEn", plant.nameEn)
            put("nameTr", plant.nameTr)
            put("nameZh", plant.nameZh)
            put("latinName", plant.latinName)
            put("category", plant.category.name)
            put("mizajType", plant.mizajType.name)
            put("mizajDegreeUy", plant.mizajDegreeUy)
            put("mizajDegreeEn", plant.mizajDegreeEn)
            put("mizajDegreeTr", plant.mizajDegreeTr)
            put("mizajDegreeZh", plant.mizajDegreeZh)
            put("benefitsUy", plant.benefitsUy)
            put("benefitsEn", plant.benefitsEn)
            put("benefitsTr", plant.benefitsTr)
            put("benefitsZh", plant.benefitsZh)
            put("usageUy", plant.usageUy)
            put("usageEn", plant.usageEn)
            put("usageTr", plant.usageTr)
            put("usageZh", plant.usageZh)
            put("cautionUy", plant.cautionUy)
            put("cautionEn", plant.cautionEn)
            put("cautionTr", plant.cautionTr)
            put("cautionZh", plant.cautionZh)
            put("organTargetUy", plant.organTargetUy)
            put("organTargetEn", plant.organTargetEn)
            put("organTargetTr", plant.organTargetTr)
            put("organTargetZh", plant.organTargetZh)
            put("iconEmoji", plant.iconEmoji)
            put("imageResId", plant.imageResId ?: -1)
            put("imageUrl", plant.imageUrl ?: "")
            put("imageFit", plant.imageFit ?: "contain")
            put("isFeatured", plant.isFeatured)
            put("publishTarget", plant.publishTarget ?: "ALL")
        }
    }

    private fun jsonToPlant(obj: JSONObject): MedicinalPlant {
        val catName = obj.optString("category", PlantCategory.HERB.name)
        val category = try { PlantCategory.valueOf(catName) } catch (_: Exception) { PlantCategory.HERB }

        val mizName = obj.optString("mizajType", MizajType.HOT_DRY.name)
        val mizaj = try { MizajType.valueOf(mizName) } catch (_: Exception) { MizajType.HOT_DRY }

        val imageRes = obj.optInt("imageResId", -1)

        return MedicinalPlant(
            id = obj.optInt("id", 0),
            nameUy = obj.optString("nameUy", ""),
            nameEn = obj.optString("nameEn", ""),
            nameTr = obj.optString("nameTr", ""),
            nameZh = obj.optString("nameZh", ""),
            latinName = obj.optString("latinName", ""),
            category = category,
            mizajType = mizaj,
            mizajDegreeUy = obj.optString("mizajDegreeUy", ""),
            mizajDegreeEn = obj.optString("mizajDegreeEn", ""),
            mizajDegreeTr = obj.optString("mizajDegreeTr", ""),
            mizajDegreeZh = obj.optString("mizajDegreeZh", ""),
            benefitsUy = obj.optString("benefitsUy", ""),
            benefitsEn = obj.optString("benefitsEn", ""),
            benefitsTr = obj.optString("benefitsTr", ""),
            benefitsZh = obj.optString("benefitsZh", ""),
            usageUy = obj.optString("usageUy", ""),
            usageEn = obj.optString("usageEn", ""),
            usageTr = obj.optString("usageTr", ""),
            usageZh = obj.optString("usageZh", ""),
            cautionUy = obj.optString("cautionUy", ""),
            cautionEn = obj.optString("cautionEn", ""),
            cautionTr = obj.optString("cautionTr", ""),
            cautionZh = obj.optString("cautionZh", ""),
            organTargetUy = obj.optString("organTargetUy", ""),
            organTargetEn = obj.optString("organTargetEn", ""),
            organTargetTr = obj.optString("organTargetTr", ""),
            organTargetZh = obj.optString("organTargetZh", ""),
            iconEmoji = obj.optString("iconEmoji", "🌿"),
            imageResId = if (imageRes > 0) imageRes else null,
            imageUrl = obj.optString("imageUrl", "").ifEmpty { null },
            imageFit = obj.optString("imageFit", "contain"),
            isFeatured = obj.optBoolean("isFeatured", false),
            publishTarget = obj.optString("publishTarget", "ALL")
        )
    }
}
