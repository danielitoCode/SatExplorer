package com.elitec.satexplorer.feature.alerts.data.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.elitec.satexplorer.R

class SatellitePassNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val satellite = inputData.getString(KEY_SATELLITE_NAME).orEmpty().ifBlank { "Satellite" }
        val tMinus = inputData.getString(KEY_T_MINUS).orEmpty().ifBlank { "soon" }

        createChannelIfNeeded()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) return Result.success()
        }

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.iconclean)
            .setContentTitle("Satellite pass alert")
            .setContentText("$satellite will pass near your location in T-$tMinus")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(satellite.hashCode(), notification)
        return Result.success()
    }

    private fun createChannelIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Satellite proximity alerts",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Alerts for nearby satellite passes"
        }
        manager.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID = "sat_pass_alerts"
        const val KEY_SATELLITE_NAME = "key_satellite_name"
        const val KEY_T_MINUS = "key_t_minus"
    }
}