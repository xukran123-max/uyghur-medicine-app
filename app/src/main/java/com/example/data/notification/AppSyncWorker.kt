package com.example.data.notification

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.data.model.Language
import com.example.data.repository.PlantsStorageRepository
import com.example.data.update.AppUpdateChecker
import java.util.concurrent.TimeUnit

/**
 * ئارقا سەھنە خىزمەتچىسى (Background Sync Worker):
 * ئەپ تاقاق ياكى ئوچۇق ھالەتتە بولۇشىدىن قەتئىينەزەر،
 * يېڭى نەشر، يېڭى رېتسىپ ۋە ماقالىلەرنى قەرەللىك ھەم دەرھال تەكشۈرۈپ
 * تېلېفون سىستېمىسىغا ئاپتوماتىك ئەسكەرتىش چىقىرىدۇ.
 */
class AppSyncWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // 1. يېڭى ئەپ نەشرى بار-يوقلۇقىنى تەكشۈرۈش
            val updateInfo = AppUpdateChecker.checkUpdate(Language.UYGHUR)
            if (updateInfo != null && updateInfo.shouldUpdate) {
                AppNotificationHelper.showNotification(
                    context = appContext,
                    notificationId = 99901,
                    title = updateInfo.title,
                    message = updateInfo.message,
                    targetUrl = updateInfo.downloadUrl
                )
            }

            // 2. يېڭى ماقالىلەر، رېتسىپلار ۋە مەزمۇن ئۇقتۇرۇشلىرىنى تەكشۈرۈش
            NotificationSyncManager.checkAndNotify(appContext)

            // 3. ئارقا سەھنىدە دورا ئامبىرىنى بېكەت بىلەن ماس قەدەملەش
            try {
                PlantsStorageRepository(appContext).syncWithServer()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    companion object {
        private const val UNIQUE_PERIODIC_WORK = "uyghur_medicine_periodic_sync"
        private const val UNIQUE_IMMEDIATE_WORK = "uyghur_medicine_immediate_sync"

        /**
         * خىزمەتچىنى پىلانلاش:
         * 1) ھازىرلا دەرھال بىر قېتىم ئىجرا قىلىش (Immediate)
         * 2) كېيىن ھەر 15 مىنۇتتا بىر قېتىم قەرەللىك تەكرارلاش (Periodic)
         */
        fun schedule(context: Context) {
            try {
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                // A. دەرھال ئىجرا قىلىش (One-time)
                val immediateWork = OneTimeWorkRequestBuilder<AppSyncWorker>()
                    .setConstraints(constraints)
                    .build()

                WorkManager.getInstance(context).enqueueUniqueWork(
                    UNIQUE_IMMEDIATE_WORK,
                    ExistingWorkPolicy.REPLACE,
                    immediateWork
                )

                // B. قەرەللىك ئىجرا قىلىش (ئەڭ تۆۋەن چەك 15 مىنۇت)
                val periodicWork = PeriodicWorkRequestBuilder<AppSyncWorker>(
                    15, TimeUnit.MINUTES,
                    5, TimeUnit.MINUTES
                )
                    .setConstraints(constraints)
                    .build()

                WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                    UNIQUE_PERIODIC_WORK,
                    ExistingPeriodicWorkPolicy.UPDATE,
                    periodicWork
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
