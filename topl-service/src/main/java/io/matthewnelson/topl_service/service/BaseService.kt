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
 */
package io.matthewnelson.topl_service.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Binder
import android.os.IBinder
import io.matthewnelson.topl_core.OnionProxyManager
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_service.prefs.TorServicePrefsListener
import io.matthewnelson.topl_service.receiver.TorServiceReceiver
import io.matthewnelson.topl_service.util.ServiceConsts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

internal abstract class BaseService: Service() {

    companion object {
        var buildConfigVersionCode: Int = -1
            private set
        var buildConfigDebug: Boolean? = null
            private set
        lateinit var geoipAssetPath: String
            private set
        lateinit var geoip6AssetPath: String
            private set

        fun initialize(
            buildConfigVersionCode: Int,
            buildConfigDebug: Boolean,
            geoipAssetPath: String,
            geoip6AssetPath: String
        ) {
            this.buildConfigVersionCode = buildConfigVersionCode
            this.buildConfigDebug = buildConfigDebug
            this.geoipAssetPath = geoipAssetPath
            this.geoip6AssetPath = geoip6AssetPath
        }

        // For things that can't be saved to TorServicePrefs, such as BuildConfig.VERSION_CODE
        fun getLocalPrefs(context: Context): SharedPreferences =
            context.getSharedPreferences("TorServiceLocalPrefs", Context.MODE_PRIVATE)
    }

    abstract val context: Context

    abstract val supervisorJob: Job
    abstract val scopeMain: CoroutineScope
    abstract val serviceActionProcessor: ServiceActionProcessor
    abstract val torServicePrefsListener: TorServicePrefsListener


    ////////////////
    /// Receiver ///
    ////////////////
    abstract val torServiceReceiver: TorServiceReceiver
    abstract fun registerReceiver()
    abstract fun unregisterReceiver()


    ///////////////////////////
    /// ServiceNotification ///
    ///////////////////////////
    abstract val serviceNotification: ServiceNotification
    abstract fun removeNotification()
    abstract fun stopForegroundService()


    ///////////////
    /// Binding ///
    ///////////////
    private val torServiceBinder = TorServiceBinder()
    abstract fun unbindService()

    inner class TorServiceBinder: Binder() {

        private fun throwIllegalArgument(action: String?) {
            throw IllegalArgumentException(
                "$action is not an accepted argument for ${this.javaClass.simpleName}"
            )
        }

        fun submitServiceActionIntent(serviceActionIntent: Intent) {
            val action = serviceActionIntent.action
            if (action != null && action.contains(ServiceConsts.ServiceAction.SERVICE_ACTION)) {

                when (action) {
                    ServiceConsts.ServiceAction.DESTROY,
                    ServiceConsts.ServiceAction.NEW_ID,
                    ServiceConsts.ServiceAction.RESTART_TOR,
                    ServiceConsts.ServiceAction.START,
                    ServiceConsts.ServiceAction.STOP -> {
                        // Do not accept the above ServiceActions through use of this method.
                        // DESTROY = internal Service use only (for onDestroy)
                        // NEW_ID, RESTART_TOR, STOP = via BroadcastReceiver
                        // START = to start TorService
                        throwIllegalArgument(action)
                    }
                    else -> {
                        serviceActionProcessor.processIntent(serviceActionIntent)
                    }
                }
            } else {
                throwIllegalArgument(action)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return torServiceBinder
    }


    /////////////////
    /// TOPL-Core ///
    /////////////////
    abstract val onionProxyManager: OnionProxyManager
}