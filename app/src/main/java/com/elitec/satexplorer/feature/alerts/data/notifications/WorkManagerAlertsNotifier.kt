package com.elitec.satexplorer.feature.alerts.data.notifications

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.elitec.satexplorer.feature.alerts.domain.repository.AlertsNotifier
import java.util.concurrent.TimeUnit

class WorkManagerAlertsNotifier(
    private val context: Context
) : AlertsNotifier {

    override fun scheduleClosestSatelliteNotification(satelliteName: String, tMinus: String) {
        val delaySeconds = parseToDelaySeconds(tMinus)

        val request = OneTimeWorkRequestBuilder<SatellitePassNotificationWorker>()
            .setInitialDelay(delaySeconds, TimeUnit.SECONDS)
            .setInputData(
                Data.Builder()
                    .putString(SatellitePassNotificationWorker.KEY_SATELLITE_NAME, satelliteName)
                    .putString(SatellitePassNotificationWorker.KEY_T_MINUS, tMinus)
                    .build()
            )
            .addTag("satellite_pass_alert")
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "closest_satellite_pass",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    private fun parseToDelaySeconds(tMinus: String): Long {
        val parts = tMinus.split(":")
        val minutes = parts.getOrNull(0)?.toLongOrNull() ?: 0L
        val seconds = parts.getOrNull(1)?.toLongOrNull() ?: 0L
        return (minutes * 60L + seconds).coerceAtLeast(5L)
    }
}