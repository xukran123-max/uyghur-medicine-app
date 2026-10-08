package com.example.data.update

import com.example.BuildConfig
import com.example.data.model.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class AppUpdateInfo(
    val shouldUpdate: Boolean,
    val mustUpdate: Boolean,
    val latestVersion: String,
    val latestVersionCode: Int,
    val downloadUrl: String,
    val title: String,
    val message: String,
    val daysLeft: Int
)

object AppUpdateChecker {

    private val client = OkHttpClient.Builder()
        .followRedirects(true)
        .followSslRedirects(true)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private const val BASE_URL = "https://uyghurmedicine.com/api/app/version/"

    suspend fun checkUpdate(language: Language): AppUpdateInfo? = withContext(Dispatchers.IO) {
        try {
            val currentCode = BuildConfig.VERSION_CODE
            val currentVersion = BuildConfig.VERSION_NAME
            val url = "$BASE_URL?versionCode=$currentCode&version=$currentVersion"

            val request = Request.Builder()
                .url(url)
                .addHeader("x-app-client", "uyghurtibabiti")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext null
                val body = response.body?.string() ?: return@withContext null
                val json = JSONObject(body)

                if (!json.optBoolean("ok", false)) return@withContext null

                val shouldUpdate = json.optBoolean("shouldUpdate", false)
                val mustUpdate = json.optBoolean("mustUpdate", false)
                val latestVersion = json.optString("latestVersion", "1.2")
                val latestVersionCode = json.optInt("latestVersionCode", 3)
                val downloadUrl = json.optString("downloadUrl", "https://apk.uyghurmedicine.com/Uygur_Tibbi.apk")
                val daysLeft = json.optInt("daysLeft", 7)

                val defaultTitle = when (language) {
                    Language.UYGHUR -> "يېڭى نەشرى چىقتى (v$latestVersion)"
                    Language.TURKISH -> "Yeni Sürüm Mevcut (v$latestVersion)"
                    Language.ENGLISH -> "New Version Available (v$latestVersion)"
                }

                val defaultMsg = when (language) {
                    Language.UYGHUR -> "ئەپنىڭ ئەڭ يېڭى نەشرى تارقىتىلدى. «ھەكىم بىلەن پاراڭلىشىش (سۈنئىي ئەقىل)» ۋە كۆپ تىللىق يېڭى ئىقتىدارلار قوشۇلدى. ھازىرلا يېڭىلاۋېلىڭ."
                    Language.TURKISH -> "Uygulamanın yeni sürümü yayınlandı. Yapay zeka asistanı ve yeni özellikler eklendi. Şimdi güncelleyin."
                    Language.ENGLISH -> "A new version of the app is available with AI Hakim and multilingual improvements. Update now."
                }

                val title = json.optString("title", defaultTitle).ifEmpty { defaultTitle }
                val message = json.optString("message", defaultMsg).ifEmpty { defaultMsg }

                AppUpdateInfo(
                    shouldUpdate = shouldUpdate,
                    mustUpdate = mustUpdate,
                    latestVersion = latestVersion,
                    latestVersionCode = latestVersionCode,
                    downloadUrl = downloadUrl,
                    title = title,
                    message = message,
                    daysLeft = daysLeft
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
