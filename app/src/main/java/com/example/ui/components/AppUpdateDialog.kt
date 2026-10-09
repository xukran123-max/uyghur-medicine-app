package com.example.ui.components

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.data.update.AppUpdateInfo

@Composable
fun AppUpdateDialog(
    updateInfo: AppUpdateInfo,
    currentLanguage: Language,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = {
            if (!updateInfo.mustUpdate) {
                onDismiss()
            }
        },
        icon = {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SystemUpdate,
                    contentDescription = "Update",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        title = {
            Text(
                text = updateInfo.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = updateInfo.message,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (updateInfo.mustUpdate)
                        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                    else
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = if (updateInfo.mustUpdate) {
                                when (currentLanguage) {
                                    Language.UYGHUR -> "⚠️ بۇ قېتىملىق يېڭىلاش مەجبۇرىي. ئەپنى يېڭىلىغاندىن كېيىن داۋاملىق ئىشلەتكىلى بولىدۇ."
                                    Language.TURKISH -> "⚠️ Bu güncelleme zorunludur. Devam etmek için lütfen güncelleyin."
                                    Language.ENGLISH -> "⚠️ This update is required to continue using the application."
                                }
                            } else {
                                when (currentLanguage) {
                                    Language.UYGHUR -> "⏳ يېڭىلاش مۆھلىتى: يەنە ${updateInfo.daysLeft} كۈن قالدى (مۆھلەت توشقاندا مەجبۇرىي يېڭىلىنىدۇ)."
                                    Language.TURKISH -> "⏳ Güncelleme süresi: ${updateInfo.daysLeft} gün kaldı."
                                    Language.ENGLISH -> "⏳ Grace period: ${updateInfo.daysLeft} days remaining."
                                }
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = if (updateInfo.mustUpdate) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateInfo.downloadUrl))
                        context.startActivity(intent)
                        val toastMsg = when (currentLanguage) {
                            Language.UYGHUR -> "APK چۈشۈرۈلۈۋاتىدۇ. چۈشۈپ بولغاندىن كېيىن ئۇقتۇرۇشتىن چېكىپ قاچىلاشنى تاماملاڭ!"
                            Language.TURKISH -> "APK indiriliyor. İndirme tamamlandığında dosyaya dokunup kurulumu tamamlayın!"
                            Language.ENGLISH -> "Downloading APK. Once finished, tap the file to install the update!"
                        }
                        Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show()
                        onDismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = when (currentLanguage) {
                        Language.UYGHUR -> "ھازىرلا يېڭىلاش (APK چۈشۈرۈش)"
                        Language.TURKISH -> "Şimdi Güncelle (APK İndir)"
                        Language.ENGLISH -> "Update Now (Download APK)"
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            if (!updateInfo.mustUpdate) {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = when (currentLanguage) {
                            Language.UYGHUR -> "كېيىنرەك يېڭىلايمەن"
                            Language.TURKISH -> "Daha Sonra"
                            Language.ENGLISH -> "Later"
                        }
                    )
                }
            }
        }
    )
}
