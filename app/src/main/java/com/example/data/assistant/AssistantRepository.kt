package com.example.data.assistant

import com.example.data.model.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

sealed class ChatStreamEvent {
    data class TextChunk(val text: String) : ChatStreamEvent()
    data class Emergency(val reason: String, val href: String) : ChatStreamEvent()
    data class Citations(val items: List<CitationItem>) : ChatStreamEvent()
    data class Error(val message: String) : ChatStreamEvent()
    object Done : ChatStreamEvent()
}

class AssistantRepository(
    private val baseUrl: String = "https://www.uyghurmedicine.com"
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private var cachedSession: String? = null
    private var sessionExpiresAt: Long = 0

    private fun getApiLocale(language: Language): String {
        return when (language) {
            Language.UYGHUR -> "ug"
            Language.TURKISH -> "tr"
            Language.ENGLISH -> "us"
            Language.CHINESE -> "us"
        }
    }

    private fun getSession(): String {
        val now = System.currentTimeMillis()
        if (cachedSession != null && sessionExpiresAt > now + 30_000) {
            return cachedSession!!
        }

        val jsonBody = JSONObject().put("token", "app").toString()
        val request = Request.Builder()
            .url("$baseUrl/api/chat/session")
            .addHeader("Content-Type", "application/json")
            .addHeader("x-app-client", "uyghurtibabiti")
            .post(jsonBody.toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw Exception("Session failed: ${response.code}")
            }
            val respStr = response.body?.string() ?: throw Exception("Empty session response")
            val obj = JSONObject(respStr)
            val sessionToken = obj.getString("session")
            val ttl = obj.optLong("ttl", 1800)
            cachedSession = sessionToken
            sessionExpiresAt = now + (ttl * 1000)
            return sessionToken
        }
    }

    fun sendMessage(
        message: String,
        history: List<ChatMessage>,
        language: Language
    ): Flow<ChatStreamEvent> = flow {
        val session = try {
            getSession()
        } catch (e: Exception) {
            emit(ChatStreamEvent.Error(e.message ?: "Failed to connect"))
            return@flow
        }

        val historyArray = JSONArray()
        history.takeLast(6).forEach { msg ->
            if (msg.role == "user" || msg.role == "assistant") {
                val turn = JSONObject()
                turn.put("role", msg.role)
                turn.put("content", msg.content)
                historyArray.put(turn)
            }
        }

        val reqObj = JSONObject()
        reqObj.put("session", session)
        reqObj.put("message", message.trim())
        reqObj.put("locale", getApiLocale(language))
        reqObj.put("history", historyArray)

        val request = Request.Builder()
            .url("$baseUrl/api/chat")
            .addHeader("Content-Type", "application/json")
            .addHeader("x-app-client", "uyghurtibabiti")
            .post(reqObj.toString().toRequestBody("application/json".toMediaType()))
            .build()

        val response = try {
            client.newCall(request).execute()
        } catch (e: Exception) {
            emit(ChatStreamEvent.Error(e.message ?: "Network error"))
            return@flow
        }

        if (!response.isSuccessful) {
            emit(ChatStreamEvent.Error("Server error: ${response.code}"))
            response.close()
            return@flow
        }

        val body = response.body
        if (body == null) {
            emit(ChatStreamEvent.Error("Empty response body"))
            return@flow
        }

        try {
            val reader = BufferedReader(InputStreamReader(body.byteStream(), Charsets.UTF_8))
            var eventName = ""
            var dataContent = ""

            while (true) {
                val line = reader.readLine() ?: break
                if (line.startsWith("event: ")) {
                    eventName = line.substring(7).trim()
                } else if (line.startsWith("data: ")) {
                    dataContent = line.substring(6).trim()
                } else if (line.isEmpty()) {
                    if (eventName.isNotEmpty() && dataContent.isNotEmpty()) {
                        handleSseEvent(eventName, dataContent, this)
                    }
                    eventName = ""
                    dataContent = ""
                }
            }
            if (eventName.isNotEmpty() && dataContent.isNotEmpty()) {
                handleSseEvent(eventName, dataContent, this)
            }
            emit(ChatStreamEvent.Done)
        } catch (e: Exception) {
            emit(ChatStreamEvent.Error(e.message ?: "Streaming error"))
        } finally {
            response.close()
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun handleSseEvent(
        event: String,
        data: String,
        collector: kotlinx.coroutines.flow.FlowCollector<ChatStreamEvent>
    ) {
        try {
            val json = JSONObject(data)
            when (event) {
                "text" -> {
                    val text = json.optString("text", "")
                    if (text.isNotEmpty()) {
                        collector.emit(ChatStreamEvent.TextChunk(text))
                    }
                }
                "emergency" -> {
                    val reason = json.optString("reason", "")
                    val href = json.optString("href", "")
                    collector.emit(ChatStreamEvent.Emergency(reason, href))
                }
                "citations" -> {
                    val itemsArr = json.optJSONArray("items")
                    if (itemsArr != null) {
                        val citations = mutableListOf<CitationItem>()
                        for (i in 0 until itemsArr.length()) {
                            val c = itemsArr.getJSONObject(i)
                            citations.add(
                                CitationItem(
                                    title = c.optString("title", ""),
                                    href = c.optString("href", ""),
                                    slug = c.optString("slug", "")
                                )
                            )
                        }
                        collector.emit(ChatStreamEvent.Citations(citations))
                    }
                }
                "done" -> {
                    collector.emit(ChatStreamEvent.Done)
                }
            }
        } catch (e: Exception) {
            // ignore malformed data
        }
    }
}
