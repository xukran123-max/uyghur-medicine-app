package com.example.data.assistant

import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val role: String, // "user" or "assistant"
    val content: String,
    val isEmergency: Boolean = false,
    val citations: List<CitationItem> = emptyList(),
    val isStreaming: Boolean = false
)

data class CitationItem(
    val title: String,
    val href: String,
    val slug: String? = null
)
