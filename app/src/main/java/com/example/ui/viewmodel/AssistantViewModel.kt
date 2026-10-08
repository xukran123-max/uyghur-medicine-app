package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.assistant.AssistantRepository
import com.example.data.assistant.ChatMessage
import com.example.data.assistant.ChatStreamEvent
import com.example.data.model.Language
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AssistantViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val MAX_DAILY_QUESTIONS = 10
        private const val PREFS_NAME = "assistant_limits_prefs"
        private const val KEY_LAST_DATE = "last_question_date"
        private const val KEY_COUNT = "daily_question_count"
    }

    private val repository = AssistantRepository()
    private val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isStreaming = MutableStateFlow(false)
    val isStreaming: StateFlow<Boolean> = _isStreaming.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _showLimitReachedDialog = MutableStateFlow(false)
    val showLimitReachedDialog: StateFlow<Boolean> = _showLimitReachedDialog.asStateFlow()

    private val _todayQuestionCount = MutableStateFlow(0)
    val todayQuestionCount: StateFlow<Int> = _todayQuestionCount.asStateFlow()

    private var streamJob: Job? = null

    init {
        refreshDailyCount()
    }

    private fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Date())
    }

    private fun refreshDailyCount(): Int {
        val today = getTodayDateString()
        val lastDate = prefs.getString(KEY_LAST_DATE, "")
        val count = if (lastDate == today) {
            prefs.getInt(KEY_COUNT, 0)
        } else {
            0
        }
        _todayQuestionCount.value = count
        return count
    }

    private fun incrementDailyCount() {
        val today = getTodayDateString()
        val current = refreshDailyCount()
        val next = current + 1
        prefs.edit()
            .putString(KEY_LAST_DATE, today)
            .putInt(KEY_COUNT, next)
            .apply()
        _todayQuestionCount.value = next
    }

    fun dismissLimitDialog() {
        _showLimitReachedDialog.value = false
    }

    fun openLimitDialog() {
        _showLimitReachedDialog.value = true
    }

    fun getSuggestions(language: Language): List<String> = when (language) {
        Language.UYGHUR -> listOf(
            "باش ئاغرىقىغا قانداق تەبىئىي چارە بار؟",
            "ئاشقازان سوغۇقلىشىپ كەتسە قانداق قىلىش كېرەك؟",
            "ئۇيقۇسىزلىققا نېمە ياخشى؟",
            "ئىممۇنىتېتنى كۈچەيتىدىغان تەبىئىي ئۆسۈملۈكلەر"
        )
        Language.TURKISH -> listOf(
            "Baş ağrısına hangi doğal yöntemler iyi gelir?",
            "Mide üşütmesine ne iyi gelir?",
            "Uykusuzluk için hangi şifalı bitkiler kullanılır?",
            "Bağışıklığı güçlendiren şifalı bitkiler"
        )
        Language.ENGLISH -> listOf(
            "Natural remedies for headache",
            "What helps with cold stomach and digestion?",
            "Herbs for restful sleep",
            "Traditional herbs to boost immunity"
        )
    }

    fun sendMessage(input: String, language: Language) {
        val trimmed = input.trim()
        if (trimmed.isEmpty() || _isStreaming.value) return

        // Check daily question limit (max 10 questions per day)
        val currentCount = refreshDailyCount()
        if (currentCount >= MAX_DAILY_QUESTIONS) {
            _showLimitReachedDialog.value = true
            return
        }

        // Increment count
        incrementDailyCount()

        _errorMessage.value = null

        val userMessage = ChatMessage(
            role = "user",
            content = trimmed
        )
        val assistantMessage = ChatMessage(
            role = "assistant",
            content = "",
            isStreaming = true
        )

        val updated = _messages.value.toMutableList()
        updated.add(userMessage)
        updated.add(assistantMessage)
        _messages.value = updated
        _isStreaming.value = true

        streamJob?.cancel()
        streamJob = viewModelScope.launch {
            repository.sendMessage(trimmed, _messages.value.dropLast(1), language)
                .collect { event ->
                    when (event) {
                        is ChatStreamEvent.TextChunk -> {
                            val currentList = _messages.value.toMutableList()
                            val lastIndex = currentList.lastIndex
                            if (lastIndex >= 0 && currentList[lastIndex].role == "assistant") {
                                val currentAssistant = currentList[lastIndex]
                                currentList[lastIndex] = currentAssistant.copy(
                                    content = currentAssistant.content + event.text
                                )
                                _messages.value = currentList
                            }
                        }
                        is ChatStreamEvent.Citations -> {
                            val currentList = _messages.value.toMutableList()
                            val lastIndex = currentList.lastIndex
                            if (lastIndex >= 0 && currentList[lastIndex].role == "assistant") {
                                val currentAssistant = currentList[lastIndex]
                                currentList[lastIndex] = currentAssistant.copy(
                                    citations = event.items
                                )
                                _messages.value = currentList
                            }
                        }
                        is ChatStreamEvent.Emergency -> {
                            val currentList = _messages.value.toMutableList()
                            val lastIndex = currentList.lastIndex
                            if (lastIndex >= 0 && currentList[lastIndex].role == "assistant") {
                                val currentAssistant = currentList[lastIndex]
                                currentList[lastIndex] = currentAssistant.copy(
                                    isEmergency = true
                                )
                                _messages.value = currentList
                            }
                        }
                        is ChatStreamEvent.Error -> {
                            _errorMessage.value = event.message
                            val currentList = _messages.value.toMutableList()
                            val lastIndex = currentList.lastIndex
                            if (lastIndex >= 0 && currentList[lastIndex].role == "assistant") {
                                val currentAssistant = currentList[lastIndex]
                                if (currentAssistant.content.isEmpty()) {
                                    val errText = when (language) {
                                        Language.UYGHUR -> "ھازىر ياردەمچىگە ئۇلانغىلى بولمىدى. قايتا سىناڭ."
                                        Language.TURKISH -> "Şu anda asistana bağlanılamadı. Lütfen tekrar deneyiniz."
                                        Language.ENGLISH -> "Could not connect to the assistant. Please try again."
                                    }
                                    currentList[lastIndex] = currentAssistant.copy(
                                        content = errText,
                                        isStreaming = false
                                    )
                                } else {
                                    currentList[lastIndex] = currentAssistant.copy(isStreaming = false)
                                }
                                _messages.value = currentList
                            }
                        }
                        is ChatStreamEvent.Done -> {
                            val currentList = _messages.value.toMutableList()
                            val lastIndex = currentList.lastIndex
                            if (lastIndex >= 0 && currentList[lastIndex].role == "assistant") {
                                currentList[lastIndex] = currentList[lastIndex].copy(isStreaming = false)
                                _messages.value = currentList
                            }
                            _isStreaming.value = false
                        }
                    }
                }
            _isStreaming.value = false
        }
    }

    fun clearChat() {
        streamJob?.cancel()
        _isStreaming.value = false
        _messages.value = emptyList()
        _errorMessage.value = null
    }
}
