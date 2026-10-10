package com.example.ui.util

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.File

object ImageUtils {
    /**
     * Resolves various image model formats into an object Coil AsyncImage can handle directly:
     * 1. Data URLs: data:image/jpeg;base64,... or data:image/webp;base64,... -> Decoded Bitmap or ByteArray
     * 2. Web URLs: http:// or https:// -> String URL
     * 3. Web relative paths: /images/plants/... -> Full URL https://uyghurmedicine.com/images/plants/...
     * 4. Content URIs: content://... -> android.net.Uri
     * 5. File paths / File URIs: /path/to/file or file://... -> File
     * 6. App internal filesDir fallback: plant_images/filename
     */
    fun resolvePlantImageModel(rawUrl: String?, context: Context): Any? {
        val raw = rawUrl?.trim() ?: return null
        if (raw.isBlank()) return null

        return try {
            when {
                // Base64 Data URL (e.g. data:image/jpeg;base64,... or data:image/webp;base64,...)
                raw.startsWith("data:image/", ignoreCase = true) -> {
                    val base64Data = if (raw.contains("base64,", ignoreCase = true)) {
                        raw.substringAfter("base64,", "")
                    } else {
                        raw.substringAfter(",", "")
                    }
                    if (base64Data.isNotBlank()) {
                        val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
                        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size) ?: decodedBytes
                    } else {
                        null
                    }
                }

                // Full Web URL
                raw.startsWith("http://", ignoreCase = true) || raw.startsWith("https://", ignoreCase = true) -> {
                    raw
                }

                // Web relative path
                raw.startsWith("/images/plants/", ignoreCase = true) -> {
                    "https://uyghurmedicine.com$raw"
                }

                // Existing local file
                raw.startsWith("/") && File(raw).exists() -> {
                    File(raw)
                }

                // File URI
                raw.startsWith("file://", ignoreCase = true) -> {
                    val file = File(raw.removePrefix("file://"))
                    if (file.exists()) file else raw
                }

                // Content URI from media picker
                raw.startsWith("content://", ignoreCase = true) -> {
                    Uri.parse(raw)
                }

                // Local filesDir or fallback web URL
                else -> {
                    val fileName = raw.substringAfterLast("/")
                    val localFile = File(context.filesDir, "plant_images/$fileName")
                    if (localFile.exists()) {
                        localFile
                    } else {
                        "https://uyghurmedicine.com/images/plants/$fileName"
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
