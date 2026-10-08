package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Language
import com.example.data.model.MedicinalPlant
import com.example.data.model.MizajType
import com.example.data.model.PlantCategory

@Composable
fun AdminLoginDialog(
    currentLanguage: Language,
    onDismiss: () -> Unit,
    onLoginSuccess: () -> Unit,
    onVerifyPassword: (String) -> Boolean
) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = "باشقۇرغۇچى كىرىش (پارول)",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "ئەپ مەزمۇنلىرىنى (دورا، مىزاج، رېتسىپ) تەھرىرلەش ئۈچۈن مەخپىي نومۇرنى كىرگۈزۈڭ (دەسلەپكى پارول: 123456):",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        isError = false
                    },
                    label = { Text("مەخپىي نومۇر") },
                    singleLine = true,
                    isError = isError,
                    supportingText = if (isError) {
                        { Text("مەخپىي نومۇر خاتا! قايتا كىرگۈزۈڭ.", color = MaterialTheme.colorScheme.error) }
                    } else null,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (onVerifyPassword(password.trim())) {
                        onLoginSuccess()
                    } else {
                        isError = true
                    }
                }
            ) {
                Text("كىرىش")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("بىكار قىلىش")
            }
        }
    )
}

@Composable
fun AdminChangePasswordDialog(
    onDismiss: () -> Unit,
    onChangePassword: (oldPass: String, newPass: String) -> Boolean
) {
    var oldPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Key,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(text = "مەخپىي نومۇر ئۆزگەرتىش", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = oldPass,
                    onValueChange = { oldPass = it; errorMsg = null },
                    label = { Text("كونا پارول") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = newPass,
                    onValueChange = { newPass = it; errorMsg = null },
                    label = { Text("يېڭى پارول") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = confirmPass,
                    onValueChange = { confirmPass = it; errorMsg = null },
                    label = { Text("يېڭى پارولنى جەزملەش") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (errorMsg != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMsg!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (newPass.length < 4) {
                        errorMsg = "يېڭى پارول ئەڭ ئاز 4 خانىلىق بولسۇن"
                        return@Button
                    }
                    if (newPass != confirmPass) {
                        errorMsg = "ئىككى قېتىم كىرگۈزگەن يېڭى پارول ئوخشاش ئەمەس"
                        return@Button
                    }
                    if (onChangePassword(oldPass, newPass)) {
                        Toast.makeText(context, "پارول مۇۋەپپەقىيەتلىك ئۆزگەرتىلدى!", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    } else {
                        errorMsg = "كونا پارول خاتا!"
                    }
                }
            ) {
                Text("ساقلاش")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("بىكار قىلىش")
            }
        }
    )
}

@Composable
fun AdminConfirmDeleteDialog(
    plantName: String,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(text = "دورىنى ئۆچۈرۈش", fontWeight = FontWeight.Bold)
        },
        text = {
            Text(text = "«$plantName» نى ئەپتىن راستىنلا ئۆچۈرەمسىز؟ بۇ مەشغۇلاتنى ئەسلىگە كەلتۈرگىلى بولمايدۇ.")
        },
        confirmButton = {
            Button(
                onClick = onConfirmDelete,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("ئۆچۈرۈش")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("بىكار قىلىش")
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditPlantDialog(
    initialPlant: MedicinalPlant?,
    currentLanguage: Language,
    onDismiss: () -> Unit,
    onSave: (MedicinalPlant) -> Unit
) {
    val isNew = initialPlant == null

    var nameUy by remember { mutableStateOf(initialPlant?.nameUy ?: "") }
    var nameTr by remember { mutableStateOf(initialPlant?.nameTr ?: "") }
    var nameEn by remember { mutableStateOf(initialPlant?.nameEn ?: "") }
    var nameZh by remember { mutableStateOf(initialPlant?.nameZh ?: "") }
    var latinName by remember { mutableStateOf(initialPlant?.latinName ?: "") }

    var selectedCategory by remember { mutableStateOf(initialPlant?.category ?: PlantCategory.HERB) }
    var selectedMizaj by remember { mutableStateOf(initialPlant?.mizajType ?: MizajType.HOT_DRY) }

    var mizajDegreeUy by remember { mutableStateOf(initialPlant?.mizajDegreeUy ?: "2-درىجىدە ئىسسىق، 2-درىجىدە قۇرۇق") }
    var benefitsUy by remember { mutableStateOf(initialPlant?.benefitsUy ?: "") }
    var usageUy by remember { mutableStateOf(initialPlant?.usageUy ?: "") }
    var cautionUy by remember { mutableStateOf(initialPlant?.cautionUy ?: "") }
    var organTargetUy by remember { mutableStateOf(initialPlant?.organTargetUy ?: "") }
    var iconEmoji by remember { mutableStateOf(initialPlant?.iconEmoji ?: "🌿") }
    var imageUrl by remember { mutableStateOf(initialPlant?.imageUrl ?: "") }
    var imageFit by remember { mutableStateOf(initialPlant?.imageFit ?: "contain") }
    var isFeatured by remember { mutableStateOf(initialPlant?.isFeatured ?: false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .heightIn(max = 680.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isNew) "➕ يېڭى دورا قوشۇش" else "✏️ دورا مەزمۇنىنى تەھرىرلەش",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Section 1: Names
                Text(text = "📌 دورىنىڭ نامى (ھەرقايسى تىللاردا):", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = nameUy,
                    onValueChange = { nameUy = it },
                    label = { Text("ئۇيغۇرچە نامى *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = nameTr,
                    onValueChange = { nameTr = it },
                    label = { Text("تۈركچە نامى (Türkçe)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = nameEn,
                    onValueChange = { nameEn = it },
                    label = { Text("ئىنگلىزچە نامى (English)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = nameZh,
                    onValueChange = { nameZh = it },
                    label = { Text("خەنزۇچە نامى (中文)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = latinName,
                    onValueChange = { latinName = it },
                    label = { Text("لاتىنچە پەن نامى (Latin)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Section 2: Category
                Text(text = "📂 دورا تۈرى (Category):", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PlantCategory.entries.forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat.nameUy) },
                            leadingIcon = if (selectedCategory == cat) {
                                { Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Section 3: Mizaj
                Text(text = "⚖️ مىزاجى (Mizaj):", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    MizajType.entries.forEach { miz ->
                        FilterChip(
                            selected = selectedMizaj == miz,
                            onClick = { selectedMizaj = miz },
                            label = { Text(miz.nameUy) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(miz.colorHex).copy(alpha = 0.2f),
                                selectedLabelColor = Color(miz.colorHex)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = mizajDegreeUy,
                    onValueChange = { mizajDegreeUy = it },
                    label = { Text("مىزاج دەرىجىسى (مەسىلەن: 2-درىجىدە ئىسسىق)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Section 4: Benefits, Usage, Caution
                Text(text = "🩺 شىپالىق تەسىرى ۋە ئىشلىتىلىشى:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = benefitsUy,
                    onValueChange = { benefitsUy = it },
                    label = { Text("شىپالىق خۇسۇسىيىتى (Benefits) *") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = usageUy,
                    onValueChange = { usageUy = it },
                    label = { Text("ئىشلىتىش ئۇسۇلى ۋە مىقدارى (Usage)") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = cautionUy,
                    onValueChange = { cautionUy = it },
                    label = { Text("دىققەت قىلىدىغان ئىشلار ۋە ئەكس تەسىرى (Caution)") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = organTargetUy,
                    onValueChange = { organTargetUy = it },
                    label = { Text("تەسىر قىلىدىغان ئەزا (مەسىلەن: ئاشقازان، ئۆپكە، جىگەر)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = iconEmoji,
                        onValueChange = { iconEmoji = it },
                        label = { Text("بەلگە (Emoji)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "كۆرۈنۈشى: $iconEmoji", fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("📷 رەسىم ئۇلانمىسى (Image URL)") },
                    placeholder = { Text("https://... ياكى /images/plants/...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("بىكار قىلىش")
                    }
                    Button(
                        onClick = {
                            if (nameUy.isBlank()) {
                                errorMessage = "ئۇيغۇرچە نامىنى چوقۇم يېزىڭ"
                                return@Button
                            }
                            if (benefitsUy.isBlank()) {
                                errorMessage = "شىپالىق خۇسۇسىيىتىنى چوقۇم يېزىڭ"
                                return@Button
                            }

                            val savedPlant = MedicinalPlant(
                                id = initialPlant?.id ?: 0,
                                nameUy = nameUy.trim(),
                                nameTr = nameTr.trim().ifEmpty { nameUy.trim() },
                                nameEn = nameEn.trim().ifEmpty { nameUy.trim() },
                                nameZh = nameZh.trim().ifEmpty { nameUy.trim() },
                                latinName = latinName.trim(),
                                category = selectedCategory,
                                mizajType = selectedMizaj,
                                mizajDegreeUy = mizajDegreeUy.trim(),
                                mizajDegreeEn = initialPlant?.mizajDegreeEn ?: "",
                                mizajDegreeTr = initialPlant?.mizajDegreeTr ?: "",
                                mizajDegreeZh = initialPlant?.mizajDegreeZh ?: "",
                                benefitsUy = benefitsUy.trim(),
                                benefitsEn = initialPlant?.benefitsEn ?: "",
                                benefitsTr = initialPlant?.benefitsTr ?: "",
                                benefitsZh = initialPlant?.benefitsZh ?: "",
                                usageUy = usageUy.trim(),
                                usageEn = initialPlant?.usageEn ?: "",
                                usageTr = initialPlant?.usageTr ?: "",
                                usageZh = initialPlant?.usageZh ?: "",
                                cautionUy = cautionUy.trim(),
                                cautionEn = initialPlant?.cautionEn ?: "",
                                cautionTr = initialPlant?.cautionTr ?: "",
                                cautionZh = initialPlant?.cautionZh ?: "",
                                organTargetUy = organTargetUy.trim(),
                                organTargetEn = initialPlant?.organTargetEn ?: "",
                                organTargetTr = initialPlant?.organTargetTr ?: "",
                                organTargetZh = initialPlant?.organTargetZh ?: "",
                                iconEmoji = iconEmoji.ifEmpty { "🌿" },
                                imageResId = initialPlant?.imageResId,
                                imageUrl = imageUrl.trim().ifEmpty { null },
                                imageFit = imageFit,
                                isFeatured = isFeatured
                            )
                            onSave(savedPlant)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("ساقلاش")
                    }
                }
            }
        }
    }
}
