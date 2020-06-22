package io.matthewnelson.topl_service.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.NotificationVisibility
import io.matthewnelson.topl_service.R
import io.matthewnelson.topl_service.service.TorService
import java.text.NumberFormat
import java.util.*

/**
 * Everything to do with [TorService]'s notification.
 *
 * See [io.matthewnelson.topl_service.TorServiceController.Builder.NotificationBuilder]
 * */
internal class ServiceNotification(
    private val channelName: String,
    private val channelID: String,
    private val channelDescription: String,
    private val notificationID: Int,

    var activityWhenTapped: Class<*>? = null,
    var activityIntentKey: String? = null,
    var activityIntentExtras: String? = null,
    var activityIntentRequestCode: Int = 0,

    @DrawableRes var imageOn: Int = R.drawable.tor_stat_on,
    @DrawableRes var imageOff: Int = R.drawable.tor_stat_off,
    @DrawableRes var imageData: Int = R.drawable.tor_stat_dataxfer,
    @DrawableRes var imageError: Int = R.drawable.tor_stat_notifyerr,

    @ColorRes var colorWhenOn: Int = R.color.tor_notification_color_white,

    @NotificationVisibility var visibility: Int = NotificationCompat.VISIBILITY_SECRET,

    var enableRestartButton: Boolean = false,
    var enableStopButton: Boolean = false
): NotificationConsts() {

    companion object {
        private lateinit var serviceNotification: ServiceNotification

        fun initialize(serviceNotificay: ServiceNotification) {
            if (!::serviceNotification.isInitialized)
                serviceNotification = serviceNotificay
        }

        fun get(): ServiceNotification {
            if (!::serviceNotification.isInitialized)
                serviceNotification = ServiceNotification(
                    "CHANGE ME",
                    "TorService Channel",
                    "BSG is a national treasure",
                    615615
                )

            return serviceNotification
        }
    }

    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager
    private val notifyLock = Object()

    private fun notify(builder: NotificationCompat.Builder) {
        notificationBuilder = builder
        if (::notificationManager.isInitialized)
            notificationManager.notify(notificationID, builder.build())
    }


    ////////////////////////////
    /// Notification Channel ///
    ////////////////////////////
    /**
     * Called once per application start in
     * [io.matthewnelson.topl_service.TorServiceController.Builder.build]
     * */
    fun setupNotificationChannel(context: Context) {
        val nm: NotificationManager? = context.applicationContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm?.let { notificationManager = it }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelID,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = channelDescription
            channel.setSound(null, null)
            nm?.createNotificationChannel(channel)
        }
    }


    //////////////////////////
    /// Foreground Service ///
    //////////////////////////
    /**
     * Called at [TorService.onCreate] and sets the [notificationBuilder] variable such that
     * it can be re-used/updated throughout the lifecycle of the service.
     * */
    fun startForegroundNotification(torService: TorService) {
        val builder = NotificationCompat.Builder(torService.applicationContext, channelID)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setContentText("Waiting...")
            .setContentTitle("Tor")
            .setGroup("Tor")
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
            .setGroupSummary(false)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setSmallIcon(imageOn)
            .setSound(null)
            .setVisibility(visibility)

        if (activityWhenTapped != null) {
            builder.setContentIntent(getContentPendingIntent(torService))
        }

        notificationBuilder = builder
        torService.startForeground(notificationID, builder.build())
    }

    private fun getContentPendingIntent(torService: TorService): PendingIntent {
        val contentIntent = Intent(torService.applicationContext, activityWhenTapped)

        if (!activityIntentKey.isNullOrEmpty() && !activityIntentExtras.isNullOrEmpty())
            contentIntent.putExtra(activityIntentKey, activityIntentExtras)

        return PendingIntent.getActivity(
            torService.applicationContext,
            activityIntentRequestCode,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


    /////////////////
    /// Bandwidth ///
    /////////////////
    private val numberFormat = NumberFormat.getInstance(Locale.getDefault())

    fun updateBandwidth(download: Long, upload: Long) =
        synchronized(notifyLock) {
            val builder = notificationBuilder
            builder.setContentText("${formatBandwidth(download)} ↓ / ${formatBandwidth(upload)} ↑")
            notify(builder)
        }

    // Obtained from: https://gitweb.torproject.org/tor-android-service.git/tree/service/
    //                src/main/java/org/torproject/android/service/TorEventHandler.java
    // Original method name: formatCount()
    private fun formatBandwidth(value: Long): String =
        if (value < 1e6)
            numberFormat.format(
                Math.round( ( ( (value * 10 / 1024 ).toInt() ) /10 ).toFloat() )
            ) + "kbps"
        else
            numberFormat.format(
                Math.round( ( ( (value * 100 / 1024 / 1024).toInt() ) /100 ).toFloat() )
            ) + "mbps"


    ////////////
    /// Icon ///
    ////////////
    fun updateIcon(@ImageState imageState: Int) =
        synchronized(notifyLock) {
            val builder = notificationBuilder
            when (imageState) {
                ImageState.ON -> {
                    builder.setSmallIcon(imageOn)
                }
                ImageState.OFF -> {
                    builder.setSmallIcon(imageOff)
                }
                ImageState.DATA -> {
                    builder.setSmallIcon(imageData)
                }
                ImageState.ERROR -> {
                    builder.setSmallIcon(imageError)
                }
                else -> {}
            }
            notify(builder)
        }
}