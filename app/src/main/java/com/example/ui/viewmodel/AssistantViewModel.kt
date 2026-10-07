package com.example.ui.viewmodel

import android.app.Application
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

class AssistantViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AssistantRepository()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isStreaming = MutableStateFlow(false)
    val isStreaming: StateFlow<Boolean> = _isStreaming.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var streamJob: Job? = null

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
        Language.CHINESE -> listOf(
            "头痛有哪些传统草本疗法？",
            "胃寒如何调理身体？",
            "改善失眠的草药有哪些？",
            "增强免疫力的天然药材"
        )
    }

    fun sendMessage(input: String, language: Language) {
        val trimmed = input.trim()
        if (trimmed.isEmpty() || _isStreaming.value) return

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
                                        Language.UYGHUR -> "ھازىر سۈنئىي ئەقىلگە ئۇلانغىلى بولمىدى. قايتا سىناڭ."
                                        Language.TURKISH -> "Şu anda asistana bağlanılamadı. Lütfen tekrar deneyiniz."
                                        Language.ENGLISH -> "Could not connect to the assistant. Please try again."
                                        Language.CHINESE -> "连接失败，请稍后重试。"
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
