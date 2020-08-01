/*
* TorOnionProxyLibrary-Android (a.k.a. topl-android) is a derivation of
* work from the Tor_Onion_Proxy_Library project that started at commit
* hash `74407114cbfa8ea6f2ac51417dda8be98d8aba86`. Contributions made after
* said commit hash are:
*
*     Copyright (C) 2020 Matthew Nelson
*
*     This program is free software: you can redistribute it and/or modify it
*     under the terms of the GNU General Public License as published by the
*     Free Software Foundation, either version 3 of the License, or (at your
*     option) any later version.
*
*     This program is distributed in the hope that it will be useful, but
*     WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
*     or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
*     for more details.
*
*     You should have received a copy of the GNU General Public License
*     along with this program. If not, see <https://www.gnu.org/licenses/>.
*
* `===========================================================================`
* `+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++`
* `===========================================================================`
*
* The following exception is an additional permission under section 7 of the
* GNU General Public License, version 3 (“GPLv3”).
*
*     "The Interfaces" is henceforth defined as Application Programming Interfaces
*     that are publicly available classes/functions/etc (ie: do not contain the
*     visibility modifiers `internal`, `private`, `protected`, or are within
*     classes/functions/etc that contain the aforementioned visibility modifiers)
*     to TorOnionProxyLibrary-Android users that are needed to implement
*     TorOnionProxyLibrary-Android and reside in ONLY the following modules:
*
*      - topl-core-base
*      - topl-service
*
*     The following are excluded from "The Interfaces":
*
*       - All other code
*
*     Linking TorOnionProxyLibrary-Android statically or dynamically with other
*     modules is making a combined work based on TorOnionProxyLibrary-Android.
*     Thus, the terms and conditions of the GNU General Public License cover the
*     whole combination.
*
*     As a special exception, the copyright holder of TorOnionProxyLibrary-Android
*     gives you permission to combine TorOnionProxyLibrary-Android program with free
*     software programs or libraries that are released under the GNU LGPL and with
*     independent modules that communicate with TorOnionProxyLibrary-Android solely
*     through "The Interfaces". You may copy and distribute such a system following
*     the terms of the GNU GPL for TorOnionProxyLibrary-Android and the licenses of
*     the other code concerned, provided that you include the source code of that
*     other code when and as the GNU GPL requires distribution of source code and
*     provided that you do not modify "The Interfaces".
*
*     Note that people who make modified versions of TorOnionProxyLibrary-Android
*     are not obligated to grant this special exception for their modified versions;
*     it is their choice whether to do so. The GNU General Public License gives
*     permission to release a modified version without this exception; this exception
*     also makes it possible to release a modified version which carries forward this
*     exception. If you modify "The Interfaces", this exception does not apply to your
*     modified version of TorOnionProxyLibrary-Android, and you must remove this
*     exception when you distribute your modified version.
* */
package io.matthewnelson.topl_service.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.TorServiceController.Builder
import io.matthewnelson.topl_service.service.BaseService
import io.matthewnelson.topl_service.service.components.ServiceActionProcessor
import io.matthewnelson.topl_service.service.TorService
import io.matthewnelson.topl_service.service.components.BackgroundManager
import io.matthewnelson.topl_service.util.ServiceConsts
import io.matthewnelson.topl_service.util.ServiceConsts.ServiceAction
import java.math.BigInteger
import java.security.SecureRandom

/**
 * Is registered at startup of [TorService], and unregistered when it is stopped.
 * Sending an intent here to start [TorService] will do nothing as all intents are piped
 * to [ServiceActionProcessor] directly. To start the service (and Tor), call the
 * [io.matthewnelson.topl_service.TorServiceController.startTor] method.
 *
 * @param [torService]
 * */
internal class TorServiceReceiver(private val torService: BaseService): BroadcastReceiver() {

    companion object {
        // Secures the intent filter at each application startup.
        // Also serves as the key to string extras containing the ServiceAction to be executed.
        val SERVICE_INTENT_FILTER: String = BigInteger(130, SecureRandom()).toString(32)

        @Volatile
        var isRegistered = false
            private set

        /**
         * Adding a StringExtra to the Intent by passing a value for [extrasString] will
         * always use the [action] as the key for retrieving it.
         *
         * @param [context] [Context]
         * @param [action] A [ServiceConsts.ServiceAction] to be processed by [TorService]
         * @param [extrasString] To be included in the intent.
         * */
        fun sendBroadcast(context: Context, @ServiceAction action: String, extrasString: String?) {
            val broadcastIntent = Intent(SERVICE_INTENT_FILTER)
            broadcastIntent.putExtra(SERVICE_INTENT_FILTER, action)
            broadcastIntent.setPackage(context.applicationContext.packageName)

            if (extrasString != null)
                broadcastIntent.putExtra(action, extrasString)

            context.applicationContext.sendBroadcast(broadcastIntent)
        }
    }

    private val broadcastLogger = torService.getBroadcastLogger(TorServiceReceiver::class.java)

    fun register() {
        torService.context.applicationContext
            .registerReceiver(this, IntentFilter(SERVICE_INTENT_FILTER))
        if (!isRegistered)
            broadcastLogger.debug("Has been registered")
        isRegistered = true
    }

    fun unregister() {
        if (isRegistered) {
            try {
                torService.context.applicationContext.unregisterReceiver(this)
                isRegistered = false
                broadcastLogger.debug("Has been unregistered")
            } catch (e: IllegalArgumentException) {
                broadcastLogger.exception(e)
            }
        }
    }

    // TODO: Funnel everything through the binder that is instantiated in order to handle
    //  BackgroundManager execution stuff properly and in a single spot.
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            // Only accept Intents from this package.
            if (context.applicationInfo.dataDir != torService.context.applicationInfo.dataDir) return

            when (val serviceAction = intent.getStringExtra(SERVICE_INTENT_FILTER)) {

                // Only accept these 3 ServiceActions.
                ServiceAction.NEW_ID, ServiceAction.RESTART_TOR, ServiceAction.STOP -> {
                    val newIntent = Intent(serviceAction)

                    // To STOP, user either clicks notification Action STOP (if enabled),
                    // or TorServiceController.StopTor was called (sending a broadcast here).
                    // Either way we need to stop listening to the Activity LCEs so the return
                    // to foreground doesn't go off.
                    if (serviceAction == ServiceAction.STOP)
                        torService.unregisterBackgroundManager(executeRestart = false)

                    // If the broadcast intent has any string extras, their key will be the
                    // ServiceAction that was included.
                    intent.getStringExtra(serviceAction)?.let {
                        newIntent.putExtra(serviceAction, it)
                    }
                    torService.processIntent(newIntent)
                }
                else -> {
                    broadcastLogger.warn(
                        "This class does not accept $serviceAction as an argument."
                    )
                }
            }
        }
    }
}