package io.matthewnelson.topl_android_service.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import java.math.BigInteger
import java.security.SecureRandom
import io.matthewnelson.topl_android_service.receiver.ServiceAction.Companion.ACTION_RENEW
import io.matthewnelson.topl_android_service.receiver.ServiceAction.Companion.ACTION_RESTART
import io.matthewnelson.topl_android_service.receiver.ServiceAction.Companion.ACTION_START
import io.matthewnelson.topl_android_service.receiver.ServiceAction.Companion.ACTION_STOP
import io.matthewnelson.topl_android_service.TorService

internal class TorServiceReceiver: BroadcastReceiver() {

    companion object {
        val INTENT_FILTER_ACTION: String= BigInteger(130, SecureRandom()).toString(32)
        val EXTRAS_KEY: String = BigInteger(130, SecureRandom()).toString(32)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            if (intent.action != INTENT_FILTER_ACTION) return

            when (val extra = intent.getStringExtra(EXTRAS_KEY)) {
                ACTION_START, ACTION_STOP, ACTION_RESTART, ACTION_RENEW -> {
                    val torServiceIntent = Intent(context.applicationContext, TorService::class.java)
                    torServiceIntent.putExtra(EXTRAS_KEY, extra)
                    torServiceIntent.action = intent.action

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(torServiceIntent)
                    } else {
                        context.startService(torServiceIntent)
                    }
                }
                else -> { }
            }
        }
    }

}