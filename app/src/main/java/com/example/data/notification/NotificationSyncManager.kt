package com.example.data.notification

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val type: String,
    val targetUrl: String,
    val timestamp: Long
)

object NotificationSyncManager {

    private const val PREFS_NAME = "uyghur_medicine_notifications"
    private const val KEY_LAST_SEEN_ID = "last_seen_notification_id"
    private const val API_URL = "https://uyghurmedicine.com/api/app/notifications/"

    private val httpClient = OkHttpClient.Builder()
        .followRedirects(true)
        .followSslRedirects(true)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    suspend fun checkAndNotify(context: Context): List<NotificationItem> = withContext(Dispatchers.IO) {
        val newNotifications = mutableListOf<NotificationItem>()
        try {
            val request = Request.Builder()
                .url(API_URL)
                .header("Accept", "application/json")
                .header("x-app-client", "uyghurtibabiti")
                .get()
                .build()

            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful) return@withContext emptyList()

            val body = response.body?.string() ?: return@withContext emptyList()
            val json = JSONObject(body)
            if (!json.optBoolean("ok", false)) return@withContext emptyList()

            val array = json.optJSONArray("notifications") ?: return@withContext emptyList()
            val allList = mutableListOf<NotificationItem>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                allList.add(
                    NotificationItem(
                        id = obj.optString("id", ""),
                        title = obj.optString("title", ""),
                        message = obj.optString("message", ""),
                        type = obj.optString("type", "announcement"),
                        targetUrl = obj.optString("targetUrl", ""),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis())
                    )
                )
            }

            if (allList.isEmpty()) return@withContext emptyList()

            val prefs = getPrefs(context)
            val lastSeenId = prefs.getString(KEY_LAST_SEEN_ID, null)

            if (lastSeenId == null) {
                // First install / first run: record latest ID to avoid spamming past history
                prefs.edit().putString(KEY_LAST_SEEN_ID, allList[0].id).apply()
                return@withContext allList
            }

            // Find items newer than lastSeenId
            var foundLast = false
            for (item in allList) {
                if (item.id == lastSeenId) {
                    foundLast = true
                    break
                }
                newNotifications.add(item)
            }

            // If lastSeenId was not in the list (older than 30 items), show only the latest 3
            val toNotify = if (!foundLast && newNotifications.size > 3) {
                newNotifications.take(3)
            } else {
                newNotifications
            }

            // Trigger system notifications
            toNotify.forEach { notif ->
                AppNotificationHelper.showNotification(
                    context = context,
                    notificationId = notif.id.hashCode(),
                    title = notif.title,
                    message = notif.message,
                    targetUrl = notif.targetUrl
                )
            }

            // Update last seen ID
            prefs.edit().putString(KEY_LAST_SEEN_ID, allList[0].id).apply()

            return@withContext allList
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext newNotifications
    }
}
