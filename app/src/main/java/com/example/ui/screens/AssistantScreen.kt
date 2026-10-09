package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.assistant.ChatMessage
import com.example.data.model.Language
import com.example.ui.viewmodel.AssistantViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AssistantScreen(
    currentLanguage: Language,
    viewModel: AssistantViewModel = viewModel()
) {
    val context = LocalContext.current
    val messages by viewModel.messages.collectAsState()
    val isStreaming by viewModel.isStreaming.collectAsState()
    val showLimitDialog by viewModel.showLimitReachedDialog.collectAsState()
    val todayCount by viewModel.todayQuestionCount.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Scroll to bottom when new messages arrive
    LaunchedEffect(messages.size, messages.lastOrNull()?.content?.length) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // Daily Limit Contact Dialog (WhatsApp / Instagram)
    if (showLimitDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissLimitDialog() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (currentLanguage) {
                            Language.UYGHUR -> "بۈگۈنلۈك سوئال چېكى توشتى"
                            Language.TURKISH -> "Günlük Soru Sınırına Ulaşıldı"
                            Language.ENGLISH -> "Daily Question Limit Reached"
                        },
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = when (currentLanguage) {
                            Language.UYGHUR -> "سىز بۈگۈن ئەڭ يۇقىرى چەك بولغان 10 قېتىملىق سوئال سوراش پۇرسىتىنى ئىشلىتىپ بولدىڭىز.\n\nھەكىم بىلەن داۋاملىق بىۋاسىتە پاراڭلىشىش ۋە تەپسىلىي مەسلىھەت سوراش ئۈچۈن، WhatsApp ياكى Instagram ئارقىلىق بىز بىلەن ئالاقىلاشسىڭىز بولىدۇ:"
                            Language.TURKISH -> "Bugün için belirlenen 10 soru hakkınızı doldurdunuz.\n\nHekimle doğrudan görüşmek veya danışmak için WhatsApp veya Instagram üzerinden bize ulaşabilirsiniz:"
                            Language.ENGLISH -> "You have reached your daily limit of 10 questions.\n\nTo continue consulting directly with the Hakim, you can message us via WhatsApp or Instagram:"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // WhatsApp Button
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/+905551609999"))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            },
                        color = Color(0xFF25D366)
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "💬 WhatsApp (+90 555 160 9999)",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Instagram Button
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/uygur_tibbi"))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            },
                        color = Color(0xFFE1306C)
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "📷 Instagram (@uygur_tibbi)",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { viewModel.dismissLimitDialog() }) {
                    Text(
                        text = when (currentLanguage) {
                            Language.UYGHUR -> "ياپ"
                            Language.TURKISH -> "Kapat"
                            Language.ENGLISH -> "Close"
                        }
                    )
                }
            }
        )
    }

    val headerTitle = when (currentLanguage) {
        Language.UYGHUR -> "ھەكىم بىلەن پاراڭلىشىڭ"
        Language.TURKISH -> "Hekim ile Görüşün"
        Language.ENGLISH -> "Consult the Hakim"
    }

    val headerSubtitle = when (currentLanguage) {
        Language.UYGHUR -> "ئۇيغۇر تېبابىتى قەدىمىي تەجرىبىلىرى ئاساسىدا"
        Language.TURKISH -> "Geleneksel Uygur Tıbbı Rehberi"
        Language.ENGLISH -> "Traditional Uyghur Medicine Wisdom"
    }

    val placeholderText = when (currentLanguage) {
        Language.UYGHUR -> "كېسەللىك، مىزاج ياكى دورا توغرۇلۇق سورىسىڭىز بولىدۇ..."
        Language.TURKISH -> "Hastalık, mizaç veya şifalı bitkiler hakkında sorun..."
        Language.ENGLISH -> "Ask about symptoms, mizaj or herbal remedies..."
    }

    val disclaimerText = when (currentLanguage) {
        Language.UYGHUR -> "⚠️ جاۋابلار پەقەت پايدىلىنىش ئۈچۈن بولۇپ، ئىشلىتىشتىن بۇرۇن چوقۇم مەسلېھەت سوراڭ."
        Language.TURKISH -> "⚠️ Yanıtlar yalnızca bilgilendirme amaçlıdır; doktor tavsiyesi yerine geçmez."
        Language.ENGLISH -> "⚠️ For informational purposes only. Consult a physician for medical advice."
    }

    val suggestions = viewModel.getSuggestions(currentLanguage)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = "Medical",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = headerTitle,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            // Daily counter badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        if (todayCount >= 10)
                                            MaterialTheme.colorScheme.errorContainer
                                        else
                                            Color(0xFF27AE60).copy(alpha = 0.15f)
                                    )
                                    .clickable {
                                        if (todayCount >= 10) viewModel.openLimitDialog()
                                    }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "$todayCount/10",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (todayCount >= 10)
                                        MaterialTheme.colorScheme.error
                                    else
                                        Color(0xFF27AE60)
                                )
                            }
                        }
                        Text(
                            text = headerSubtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (messages.isNotEmpty()) {
                    IconButton(
                        onClick = { viewModel.clearChat() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Clear Chat",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Messages area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (messages.isEmpty()) {
                // Empty state with welcome greeting & quick suggestions
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Handshake,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = when (currentLanguage) {
                                            Language.UYGHUR -> "ھۆرمەتلىك مېھمان، خۇش كەپسىز!"
                                            Language.TURKISH -> "Hoş Geldiniz!"
                                            Language.ENGLISH -> "Welcome!"
                                        },
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 16.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = when (currentLanguage) {
                                        Language.UYGHUR -> "مەن ئۇيغۇر تېبابىتى ھەكىمى تىۋىپ  ياردەمچىڭىز  ئابدۇشۈكۈر . سالامەتلىك، مىزاج، دورىلىق ئۆسۈملۈكلەر توغرىسىدا سوئاللىرىڭىز بولسا مەرھەمەت (كۈنىگە 10 سوئال)."
                                        Language.TURKISH -> "Geleneksel Uygur Tıbbı ilkelerine göre sağlık, mizaç ve şifalı bitkiler hakkında sorularınızı yanıtlayabilirim (Günde 10 soru)."
                                        Language.ENGLISH -> "I can answer questions regarding health, body temperament (mizaj), and herbal remedies based on traditional Uyghur medicine (10 questions daily)."
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = when (currentLanguage) {
                                Language.UYGHUR -> "💡 كۆپ سورىلىدىغان سوئاللار:"
                                Language.TURKISH -> "💡 Örnek Sorular:"
                                Language.ENGLISH -> "💡 Suggested Questions:"
                            },
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp, vertical = 6.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            suggestions.forEach { prompt ->
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                                    modifier = Modifier.clickable {
                                        viewModel.sendMessage(prompt, currentLanguage)
                                    }
                                ) {
                                    Text(
                                        text = prompt,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Chat messages list
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    items(messages, key = { it.id }) { msg ->
                        MessageBubble(
                            message = msg,
                            currentLanguage = currentLanguage,
                            onCitationClick = { url ->
                                try {
                                    val fullUrl = if (url.startsWith("http")) url else "https://www.uyghurmedicine.com$url"
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(fullUrl)))
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }

        // Disclaimer Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ) {
            Text(
                text = disclaimerText,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                fontSize = 10.sp
            )
        }

        // Input Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = placeholderText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            maxLines = 1
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    singleLine = false,
                    maxLines = 4,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (inputText.isNotBlank() && !isStreaming) {
                                viewModel.sendMessage(inputText, currentLanguage)
                                inputText = ""
                            }
                        }
                    )
                )

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank() && !isStreaming) {
                            viewModel.sendMessage(inputText, currentLanguage)
                            inputText = ""
                        }
                    },
                    enabled = inputText.isNotBlank() && !isStreaming,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            if (inputText.isNotBlank() && !isStreaming)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                        )
                ) {
                    if (isStreaming) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = if (inputText.isNotBlank()) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: ChatMessage,
    currentLanguage: Language,
    onCitationClick: (String) -> Unit
) {
    val isUser = message.role == "user"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.fillMaxWidth(if (isUser) 0.82f else 0.88f),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            Card(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isUser) 16.dp else 4.dp,
                    bottomEnd = if (isUser) 4.dp else 16.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isUser)
                        MaterialTheme.colorScheme.primary
                    else if (message.isEmergency)
                        MaterialTheme.colorScheme.errorContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    if (message.isEmergency) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = "Emergency",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = when (currentLanguage) {
                                    Language.UYGHUR -> "جىددىي قۇتقۇزۇش ئەسكەرتمىسى!"
                                    Language.TURKISH -> "Acil Durum Uyarısı!"
                                    Language.ENGLISH -> "Emergency Alert!"
                                },
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 13.sp
                            )
                        }
                    }

                    if (message.content.isEmpty() && message.isStreaming) {
                        Text(
                            text = when (currentLanguage) {
                                Language.UYGHUR -> "ھەكىم ئويلىنىۋاتىدۇ..."
                                Language.TURKISH -> "Düşünülüyor..."
                                Language.ENGLISH -> "Thinking..."
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    } else {
                        Text(
                            text = message.content,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                            color = if (isUser)
                                MaterialTheme.colorScheme.onPrimary
                            else if (message.isEmergency)
                                MaterialTheme.colorScheme.onErrorContainer
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Citations / related articles
                    if (message.citations.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = when (currentLanguage) {
                                Language.UYGHUR -> "📖 مۇناسىۋەتلىك ماقالىلەر:"
                                Language.TURKISH -> "📖 İlgili Makaleler:"
                                Language.ENGLISH -> "📖 Related Articles:"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        message.citations.forEach { c ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surface,
                                tonalElevation = 1.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp)
                                    .clickable { onCitationClick(c.href) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocalPharmacy,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = c.title,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
