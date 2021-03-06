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
package io.matthewnelson.sampleapp.ui.fragments.settings.tor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Button
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.matthewnelson.sampleapp.R
import io.matthewnelson.sampleapp.ui.fragments.dashboard.DashMessage
import io.matthewnelson.sampleapp.ui.fragments.dashboard.DashboardFragment
import io.matthewnelson.sampleapp.ui.fragments.settings.CloseKeyBoardNavListener
import io.matthewnelson.sampleapp.ui.fragments.settings.tor.components.DnsPortOption
import io.matthewnelson.sampleapp.ui.fragments.settings.tor.components.HttpPortOption
import io.matthewnelson.sampleapp.ui.fragments.settings.tor.components.SocksPortOption
import io.matthewnelson.topl_core_base.BaseConsts.IsolationFlag
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service_base.BaseServiceTorSettings

class SettingsTorFragment : Fragment() {

    companion object {
        const val AUTO = "Auto"
        const val DISABLED = "Disabled"
        const val CUSTOM = "Custom"
    }

    private lateinit var serviceTorSettings: BaseServiceTorSettings
    private lateinit var socksPortOption: SocksPortOption
    private lateinit var httpPortOption: HttpPortOption
    private lateinit var dnsPortOption: DnsPortOption
//    private lateinit var transPortOption: TransPortOption

    private lateinit var buttonSocksFlags: Button
    private lateinit var buttonHttpFlags: Button
    private lateinit var buttonDnsFlags: Button
//    private lateinit var buttonTransFlags: Button
    private var saveButtonHeight = 0

    private lateinit var buttonSave: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        serviceTorSettings = TorServiceController.getServiceTorSettings()
        return inflater.inflate(R.layout.fragment_settings_tor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        socksPortOption = SocksPortOption(view, serviceTorSettings)
        httpPortOption = HttpPortOption(view, serviceTorSettings)
        dnsPortOption = DnsPortOption(view, serviceTorSettings)
//        transPortOption = TransPortOption(view, serviceTorSettings)

        findNavController().addOnDestinationChangedListener(CloseKeyBoardNavListener(view))

        findViews(view)
        setButtonClickListener(view)
        initIsolationFlags()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private val socksIsolationFlags = mutableListOf<@IsolationFlag String>()
    private val httpIsolationFlags = mutableListOf<@IsolationFlag String>()
    private val dnsIsolationFlags = mutableListOf<@IsolationFlag String>()
//    private val transIsolationFlags = mutableListOf<@IsolationFlag String>()

    fun setIsolationFlags(portType: String, selectedList: List<@IsolationFlag String>) {
        when (portType) {
            IsolationFlagsFragment.SOCKS_FLAGS -> {
                socksIsolationFlags.clear()
                socksIsolationFlags.addAll(selectedList)
            }
            IsolationFlagsFragment.HTTP_FLAGS -> {
                httpIsolationFlags.clear()
                httpIsolationFlags.addAll(selectedList)
            }
            IsolationFlagsFragment.DNS_FLAGS -> {
                dnsIsolationFlags.clear()
                dnsIsolationFlags.addAll(selectedList)
            }
//            IsolationFlagsFragment.TRANS_FLAGS -> {
//                transIsolationFlags.clear()
//                transIsolationFlags.addAll(selectedList)
//            }
        }
    }

    private fun initIsolationFlags() {
        serviceTorSettings.socksPortIsolationFlags?.let {
            setIsolationFlags(IsolationFlagsFragment.SOCKS_FLAGS, it)
        }
        serviceTorSettings.httpTunnelPortIsolationFlags?.let {
            setIsolationFlags(IsolationFlagsFragment.HTTP_FLAGS, it)
        }
        serviceTorSettings.dnsPortIsolationFlags?.let {
            setIsolationFlags(IsolationFlagsFragment.DNS_FLAGS, it)
        }
//        serviceTorSettings.transPortIsolationFlags?.let {
//            setIsolationFlags(IsolationFlagsFragment.TRANS_FLAGS, it)
//        }
    }

    private fun findViews(view: View) {
        buttonSocksFlags = view.findViewById(R.id.settings_tor_button_socks_isolation_flags)
        buttonHttpFlags = view.findViewById(R.id.settings_tor_button_http_isolation_flags)
        buttonDnsFlags = view.findViewById(R.id.settings_tor_button_dns_isolation_flags)
//        buttonTransFlags = view.findViewById(R.id.settings_tor_button_trans_isolation_flags)

        buttonSave = view.findViewById(R.id.settings_tor_button_save)

        val viewTreeObserver = view.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    saveButtonHeight = buttonSave.height + buttonSave.marginBottom
                }
            })
        }
    }

    private fun setButtonClickListener(view: View) {
        buttonSave.setOnClickListener {
            CloseKeyBoardNavListener.closeKeyboard(view)
            socksPortOption.saveSocksPort() ?: return@setOnClickListener
            httpPortOption.saveHttpPort() ?: return@setOnClickListener
            dnsPortOption.saveDnsPort() ?: return@setOnClickListener
//            transPortOption.saveTransPort() ?: return@setOnClickListener

            serviceTorSettings.socksPortIsolationFlagsSave(socksIsolationFlags)
            serviceTorSettings.httpPortIsolationFlagsSave(httpIsolationFlags)
            serviceTorSettings.dnsPortIsolationFlagsSave(dnsIsolationFlags)
//            serviceTorSettings.transPortIsolationFlagsSave(transIsolationFlags)

            DashboardFragment.showMessage(
                DashMessage("Settings Saved\nTor may need to be restarted", R.drawable.dash_message_color_green, 4_000L)
            )
        }
        buttonSocksFlags.setOnClickListener {
            openIsolationFlagsFragment(IsolationFlagsFragment.SOCKS_FLAGS)
        }
        buttonHttpFlags.setOnClickListener {
            openIsolationFlagsFragment(IsolationFlagsFragment.HTTP_FLAGS)
        }
        buttonDnsFlags.setOnClickListener {
            openIsolationFlagsFragment(IsolationFlagsFragment.DNS_FLAGS)
        }
//        buttonTransFlags.setOnClickListener {
//            openIsolationFlagsFragment(IsolationFlagsFragment.TRANS_FLAGS)
//        }
    }

    private fun openIsolationFlagsFragment(portType: String) {
        childFragmentManager.beginTransaction().apply {
            add(
                R.id.settings_tor_fragment_container,
                IsolationFlagsFragment(
                    saveButtonHeight,
                    portType,
                    serviceTorSettings.defaultTorSettings,
                    when (portType) {
                        IsolationFlagsFragment.SOCKS_FLAGS -> socksIsolationFlags.toMutableSet()
                        IsolationFlagsFragment.HTTP_FLAGS -> httpIsolationFlags.toMutableSet()
                        IsolationFlagsFragment.DNS_FLAGS -> dnsIsolationFlags.toMutableSet()
//                        IsolationFlagsFragment.TRANS_FLAGS -> transIsolationFlags.toMutableSet()
                        else -> return
                    }
                )
            )
            commit()
        }
    }
}