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
*     needed to implement TorOnionProxyLibrary-Android, as listed below:
*
*      - From the `topl-core-base` module:
*          - All Classes/methods/variables
*
*      - From the `topl-service-base` module:
*          - All Classes/methods/variables
*
*      - From the `topl-service` module:
*          - The TorServiceController class and it's contained classes/methods/variables
*          - The ServiceNotification.Builder class and it's contained classes/methods/variables
*          - The BackgroundManager.Builder class and it's contained classes/methods/variables
*          - The BackgroundManager.Companion class and it's contained methods/variables
*
*     The following code is excluded from "The Interfaces":
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
package io.matthewnelson.topl_service_base

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.WorkerThread

/**
 * This class provides a standardized way for library users to change settings used
 * by the `topl-service` module such that the values expressed as default
 * [io.matthewnelson.topl_service_base.ApplicationDefaultTorSettings] when initializing things
 * can be modified by the implementing application.
 *
 * The values saved to [TorServicePrefs] are always preferred over the defaults declared
 * when initializing the `topl-service` module.
 *
 * Restarting Tor is currently required for the new settings to take effect.
 * */
class TorServicePrefs(context: Context): BaseServiceConsts() {

    companion object {
        const val TOR_SERVICE_PREFS_NAME = "TorServicePrefs"
        const val NULL_INT_VALUE = Int.MIN_VALUE
        const val NULL_STRING_VALUE = "NULL_STRING_VALUE"
    }

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(TOR_SERVICE_PREFS_NAME, Context.MODE_PRIVATE)
    }


    /////////////////
    /// Listeners ///
    /////////////////
    /**
     * Registers a [SharedPreferences.OnSharedPreferenceChangeListener] for the
     * associated SharedPreference
     * */
    fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    /**
     * Unregisters a [SharedPreferences.OnSharedPreferenceChangeListener] for the
     * associated SharedPreference
     * */
    fun unregisterListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }


    /////////////
    /// Query ///
    /////////////
    /**
     * Checks if the SharedPreference contains a value for the supplied [prefsKey].
     * Accepts the following annotation type String values:
     *  - [BaseServiceConsts.PrefKeyBoolean]
     *  - [BaseServiceConsts.PrefKeyInt]
     *  - [BaseServiceConsts.PrefKeyList]
     *  - [BaseServiceConsts.PrefKeyString]
     *
     *  @param [prefsKey] String of type ServiceConsts.PrefKey*
     *  @return True if the SharedPreference contains a value for the associated
     *   [prefsKey], false if not
     *  * */
    @WorkerThread
    fun contains(prefsKey: String): Boolean =
        prefs.contains(prefsKey)

    @WorkerThread
    @Throws(NullPointerException::class)
    fun getAll(): Map<String, *> =
        prefs.all

    /**
     * Returns a Boolean value for the provided [BaseServiceConsts.PrefKeyBoolean]. If no
     * value is stored in the SharedPreference, [defValue] will be returned.
     *
     * @param [booleanKey] String of type [BaseServiceConsts.PrefKeyBoolean]
     * @param [defValue] Use the [io.matthewnelson.topl_core_base.TorSettings] value
     *  associated with the [booleanKey].
     * @return The Boolean value associated with the [booleanKey], otherwise [defValue]
     * */
    @WorkerThread
    fun getBoolean(@PrefKeyBoolean booleanKey: String, defValue: Boolean): Boolean =
        prefs.getBoolean(booleanKey, defValue)

    /**
     * Returns an Int value for the provided [BaseServiceConsts.PrefKeyInt]. If no
     * value is stored in the SharedPreference, [defValue] will be returned.
     *
     * @param [intKey] String of type [BaseServiceConsts.PrefKeyInt]
     * @param [defValue] Use the [io.matthewnelson.topl_core_base.TorSettings] value
     *  associated with the [intKey].
     * @return The Int value associated with [intKey], otherwise [defValue]
     * */
    @WorkerThread
    fun getInt(@PrefKeyInt intKey: String, defValue: Int?): Int? {
        val value = prefs.getInt(intKey, defValue ?: NULL_INT_VALUE)
        return if (value == NULL_INT_VALUE) {
            null
        } else {
            value
        }
    }

    /**
     * Returns a List of Strings for the provided [BaseServiceConsts.PrefKeyList]. If no
     * value is stored in the SharedPreference, [defValue] will be returned.
     *
     * @param [listKey] String of type [BaseServiceConsts.PrefKeyList]
     * @param [defValue] Use the [io.matthewnelson.topl_core_base.TorSettings] value
     *  associated with the [listKey].
     * @return The List of Strings associated with the [listKey], otherwise [defValue]
     * */
    @WorkerThread
    fun getList(@PrefKeyList listKey: String, defValue: List<String>): List<String> {
        val csv: String = prefs.getString(listKey, defValue.joinToString()) ?: defValue.joinToString()
        return if (csv.trim().isEmpty()) {
            defValue
        } else {
            csv.split(", ")
        }
    }

    /**
     * Returns a String value for the provided [BaseServiceConsts.PrefKeyString]. If no
     * value is stored in the SharedPreference, [defValue] will be returned.
     *
     * @param [stringKey] String of type [BaseServiceConsts.PrefKeyString]
     * @param [defValue] Use the [io.matthewnelson.topl_core_base.TorSettings] value
     *  associated with the [stringKey].
     * @return The String value associated with [stringKey], otherwise [defValue]
     * */
    @WorkerThread
    fun getString(@PrefKeyString stringKey: String, defValue: String?): String? {
        val value = prefs.getString(stringKey, defValue ?: NULL_STRING_VALUE)
        return if (value == NULL_STRING_VALUE) {
            null
        } else {
            value
        }
    }


    //////////////
    /// Modify ///
    //////////////

    @WorkerThread
    fun clear() {
        val editor = prefs.edit().clear()
        if (!editor.commit())
            editor.apply()
    }

    /**
     * Removes from the SharedPreference the value associated with [prefsKey] if there is one.
     * Accepts the following annotation type String values:
     *  - [BaseServiceConsts.PrefKeyBoolean]
     *  - [BaseServiceConsts.PrefKeyInt]
     *  - [BaseServiceConsts.PrefKeyList]
     *  - [BaseServiceConsts.PrefKeyString]
     *
     *  @param [prefsKey] String of type ServiceConsts.PrefKey*
     *  * */
    @WorkerThread
    fun remove(prefsKey: String) {
        val editor = prefs.edit().remove(prefsKey)
        if (!editor.commit())
            editor.apply()
    }

    /**
     * Inserts a Boolean value into the SharedPreference for the supplied [booleanKey].
     *
     * @param [booleanKey] String of type [BaseServiceConsts.PrefKeyBoolean]
     * @param [value] Your Boolean value
     * */
    @WorkerThread
    fun putBoolean(@PrefKeyBoolean booleanKey: String, value: Boolean) {
        val editor = prefs.edit().putBoolean(booleanKey, value)
        if (!editor.commit())
            editor.apply()
    }

    /**
     * Inserts an Int value into the SharedPreference for the supplied [intKey].
     *
     * @param [intKey] String of type [BaseServiceConsts.PrefKeyInt]
     * @param [value] Your Int? value
     * */
    @WorkerThread
    fun putInt(@PrefKeyInt intKey: String, value: Int?) {
        val editor = prefs.edit().putInt(intKey, value ?: NULL_INT_VALUE)
        if (!editor.commit())
            editor.apply()
    }

    /**
     * Inserts a List of Strings as a comma separated String into the SharedPreference
     * for the supplied [listKey].
     *
     * @param [listKey] String of type [BaseServiceConsts.PrefKeyList]
     * @param [value] Your List<String> value
     * */
    @WorkerThread
    fun putList(@PrefKeyList listKey: String, value: List<String>) {
        val editor = prefs.edit().putString(listKey, value.joinToString())
        if (!editor.commit())
            editor.apply()
    }

    /**
     * Inserts a String value into the SharedPreference for the supplied [stringKey].
     *
     * @param [stringKey] String of type [BaseServiceConsts.PrefKeyString]
     * @param [value] Your String value
     * */
    @WorkerThread
    fun putString(@PrefKeyString stringKey: String, value: String?) {
        val editor = prefs.edit().putString(stringKey, value ?: NULL_STRING_VALUE)
        if (!editor.commit())
            editor.apply()
    }
}