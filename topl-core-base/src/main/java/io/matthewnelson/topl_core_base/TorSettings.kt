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
*
* `===========================================================================`
* `+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++`
* `===========================================================================`
*
* The original code, prior to commit hash 74407114cbfa8ea6f2ac51417dda8be98d8aba86,
* was:
*
*     Copyright (c) Microsoft Open Technologies, Inc.
*     All Rights Reserved
*
*     Licensed under the Apache License, Version 2.0 (the "License");
*     you may not use this file except in compliance with the License.
*     You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
*
*
*     THIS CODE IS PROVIDED ON AN *AS IS* BASIS, WITHOUT WARRANTIES OR
*     CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING
*     WITHOUT LIMITATION ANY IMPLIED WARRANTIES OR CONDITIONS OF TITLE,
*     FITNESS FOR A PARTICULAR PURPOSE, MERCHANTABLITY OR NON-INFRINGEMENT.
*
*     See the Apache 2 License for the specific language governing permissions and
*     limitations under the License.
* */
package io.matthewnelson.topl_core_base

/**
 * This class is for defining default values for your torrc file. Extend this class and define
 * your own settings.
 *
 * Keep in mind that Orbot and TorBrowser are the 2 most widely used applications
 * using Tor, and to use settings that won't conflict (those settings are documented
 * as such, and contain further details).
 *
 * [TorSettings.Companion] contains pretty standard default values which'll get you a Socks5 proxy
 * running, nothing more.
 *
 * Would **highly recommend** reading up on what's what in the manual:
 *  - https://2019.www.torproject.org/docs/tor-manual.html.en
 * */
abstract class TorSettings: BaseConsts() {

    /**
     * Handy constants for declaring pre-defined values when you extend this class to set
     * things up if you're simply looking to use a Socks5 Proxy to connect to.
     * */
    companion object {
        const val DEFAULT__DORMANT_CLIENT_TIMEOUT = 10
        const val DEFAULT__DISABLE_NETWORK = true
        const val DEFAULT__ENTRY_NODES = ""
        const val DEFAULT__EXCLUDED_NODES = ""
        const val DEFAULT__EXIT_NODES = ""
        const val DEFAULT__HAS_BRIDGES = false
        const val DEFAULT__HAS_COOKIE_AUTHENTICATION = true
        const val DEFAULT__HAS_DEBUG_LOGS = false
        const val DEFAULT__HAS_DORMANT_CANCELED_BY_STARTUP = true
        const val DEFAULT__HAS_OPEN_PROXY_ON_ALL_INTERFACES = false
        const val DEFAULT__HAS_REACHABLE_ADDRESS = false
        const val DEFAULT__HAS_REDUCED_CONNECTION_PADDING = true
        const val DEFAULT__HAS_SAFE_SOCKS = false
        const val DEFAULT__HAS_STRICT_NODES = false
        const val DEFAULT__HAS_TEST_SOCKS = false
        const val DEFAULT__IS_AUTO_MAP_HOSTS_ON_RESOLVE = true
        const val DEFAULT__IS_RELAY = false
        const val DEFAULT__PROXY_HOST = ""
        const val DEFAULT__PROXY_PASSWORD = ""
        const val DEFAULT__PROXY_SOCKS5_HOST = "" // "127.0.0.1"
        const val DEFAULT__PROXY_USER = ""
        const val DEFAULT__REACHABLE_ADDRESS_PORTS = "" // "*:80,*:443"
        const val DEFAULT__RELAY_NICKNAME = ""
        const val DEFAULT__RUN_AS_DAEMON = true
        const val DEFAULT__USE_SOCKS5 = false
    }

    /**
     * Adds to the torrc file "ConnectionPadding <0, 1, or auto>"
     *
     * See [BaseConsts.ConnectionPadding.OFF]
     * */
    abstract val connectionPadding: @ConnectionPadding String

    /**
     * If not null/not empty, will add the string value to the torrc file
     *
     * Default [java.null]
     * */
    abstract val customTorrc: String?

    /**
     * Adds to the torrc file "DormantClientTimeout <your value> minutes"
     *
     * Minimum value 10. Any value less than or equal to 9 will fall back to using the value of 10
     * when writing the config to the torrc file. Set `null` to disable
     *
     * See [DEFAULT__DORMANT_CLIENT_TIMEOUT]
     * */
    abstract val dormantClientTimeout: Int?

    /**
     * OnionProxyManager will enable this on startup using the TorControlConnection based off
     * of the device's network state. Setting this to `true` is highly recommended.
     *
     * Adds to the torrc file "DisableNetwork <1 or 0>"
     *
     * See [DEFAULT__DISABLE_NETWORK]
     * */
    abstract val disableNetwork: Boolean

    /**
     * TorBrowser and Orbot use "5400" by default. It may be wise to pick something
     * that won't conflict.
     *
     * Disabled by default by Tor. Set to "O" to disable. Can also be "auto", or a specific
     * port between "1024" and "65535"
     *
     * Adds to the torrc file "DNSPort <port or auto> <[dnsPortIsolationFlags]>"
     *
     * See [BaseConsts.PortOption.DISABLED]
     * */
    abstract val dnsPort: String

    /**
     * Express isolation flags to be added when enabling the [dnsPort]
     *
     * See [BaseConsts.IsolationFlag] for available options
     *
     * **Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#SocksPort
     * */
    abstract val dnsPortIsolationFlags: List<@IsolationFlag String>?

    /**
     * Set with a comma separated list of Entry Nodes.
     *
     * Adds to the torrc file "EntryNodes <node,node,node,...>"
     *
     * **Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#EntryNodes
     *
     * See [DEFAULT__ENTRY_NODES]
     * */
    abstract val entryNodes: String?

    /**
     * Set with a comma separated list of Exit Nodes to be excluded.
     *
     * Adds to the torrc file "ExcludeExitNodes <node,node,node,...>"
     *
     * **Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#ExcludeExitNodes
     *
     * See [DEFAULT__EXCLUDED_NODES]
     * */
    abstract val excludeNodes: String?

    /**
     * Set with a comma separated list of Exit Nodes to use.
     *
     * Adds to the torrc file "ExitNodes <node,node,node,...>"
     *
     * **Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#ExitNodes
     *
     * See [DEFAULT__EXIT_NODES]
     * */
    abstract val exitNodes: String?

    /**
     * If `true`, adds to the torrc file "UseBridges 1" and will proc the adding of bridges.
     *
     * **Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#UseBridges
     *
     * See [DEFAULT__HAS_BRIDGES]
     * */
    abstract val hasBridges: Boolean

    /**
     * **Highly** recommended to be set to `true` for securing the ControlPort
     *
     * Adds to the torrc file:
     *
     *   "CookieAuthentication 1"
     *   "CookieAuthFile <[TorConfigFiles.cookieAuthFile] path>
     *
     * **Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#CookieAuthentication
     * **Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#CookieAuthFile
     *
     * See [DEFAULT__HAS_COOKIE_AUTHENTICATION]
     * */
    abstract val hasCookieAuthentication: Boolean

    /**
     * Adds to the torrc file:
     *
     *   "Log debug syslog"
     *   "Log info syslog"
     *
     * See [DEFAULT__HAS_DEBUG_LOGS]
     * */
    abstract val hasDebugLogs: Boolean

    /**
     * **Highly** recommended to be set to `true` for Android applications.
     *
     * If true, adds to the torrc file "DormantCanceledByStartup 1"
     *
     * **Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#DormantCanceledByStartup
     *
     * See [DEFAULT__HAS_DORMANT_CANCELED_BY_STARTUP]
     * */
    abstract val hasDormantCanceledByStartup: Boolean

    /**
     * If true, adds to the torrc file "SocksListenAddress 0.0.0.0"
     *
     * See [DEFAULT__HAS_OPEN_PROXY_ON_ALL_INTERFACES]
     * */
    abstract val hasOpenProxyOnAllInterfaces: Boolean

    /**
     * If true, adds to the torrc file "ReachableAddresses <[reachableAddressPorts]>"
     *
     * **Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#ReachableAddresses
     *
     * See [DEFAULT__HAS_REACHABLE_ADDRESS]
     * */
    abstract val hasReachableAddress: Boolean

    /**
     * If true, adds to the torrc file "ReducedConnectionPadding 1"
     *
     * **Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#ReducedConnectionPadding
     *
     * See [DEFAULT__HAS_REDUCED_CONNECTION_PADDING]
     * */
    abstract val hasReducedConnectionPadding: Boolean

    /**
     * If true, adds to the torrc file "SafeSocks 1"
     *
     * **Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#SafeSocks
     *
     * See [DEFAULT__HAS_SAFE_SOCKS]
     * */
    abstract val hasSafeSocks: Boolean

    /**
     * If true, adds to the torrc file "StrictNodes 1"
     *
     * **Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#StrictNodes
     *
     * See [DEFAULT__HAS_STRICT_NODES]
     * */
    abstract val hasStrictNodes: Boolean

    /**
     * If true, adds to the torrc file "TestSocks 1"
     *
     * **Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#TestSocks
     *
     * See [DEFAULT__HAS_TEST_SOCKS]
     * */
    abstract val hasTestSocks: Boolean

    /**
     * Could be "auto" or a specific port, such as "8288".
     *
     * TorBrowser and Orbot use "8218" and "8118", respectively, by default.
     * It may be wise to pick something that won't conflict if you're using this setting.
     *
     * **Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#HTTPTunnelPort
     *
     * See [BaseConsts.PortOption.DISABLED]
     * */
    abstract val httpTunnelPort: String

    /**
     * Express isolation flags to be added when enabling the [httpTunnelPort]
     *
     * **Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#SocksPort
     *
     * See [BaseConsts.IsolationFlag] for available options
     * */
    abstract val httpTunnelPortIsolationFlags: List<@IsolationFlag String>?

    /**
     * See [DEFAULT__IS_AUTO_MAP_HOSTS_ON_RESOLVE]
     *
     * **Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#AutomapHostsOnResolve
     * */
    abstract val isAutoMapHostsOnResolve: Boolean

    /**
     * See [DEFAULT__IS_RELAY]
     *
     * If setting this to true, see [relayPort] documentation.
     * */
    abstract val isRelay: Boolean

    /**
     * Must have the transport binaries for obfs4 and/or snowflake, depending
     * on if you wish to include them in your bridges file to use.
     *
     * See [BaseConsts.SupportedBridgeType] for options
     */
    abstract val listOfSupportedBridges: List<@SupportedBridgeType String>

    /**
     * See [DEFAULT__PROXY_HOST]
     * */
    abstract val proxyHost: String?

    /**
     * See [DEFAULT__PROXY_PASSWORD]
     * */
    abstract val proxyPassword: String?

    /**
     * Default = [java.null]
     * */
    abstract val proxyPort: Int?

    /**
     * Adds to the torrc file "Socks5Proxy [proxySocks5Host]:[proxySocks5ServerPort]"
     *
     * **Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#Socks5Proxy
     *
     * See [DEFAULT__PROXY_SOCKS5_HOST]
     * */
    abstract val proxySocks5Host: String?

    /**
     * Adds to the torrc file "Socks5Proxy [proxySocks5Host]:[proxySocks5ServerPort]"
     *
     * Default = [java.null]
     *
     * Try ((Math.random() * 1000) + 10000).toInt()
     * */
    abstract val proxySocks5ServerPort: Int?

    /**
     * Depending on the [BaseConsts.ProxyType], will add authenticated Socks5 or HTTPS proxy,
     * if other settings are configured properly.
     *
     * This only gets used if you declare the following settings set as:
     *
     *   [useSocks5] is set to `false`
     *   [hasBridges] is set to `false`
     *   [proxyType] is [BaseConsts.ProxyType.SOCKS_5] or [BaseConsts.ProxyType.HTTPS]
     *   [proxyHost] is set (eg. 127.0.0.1)
     *   [proxyPort] is `null`, or a port between 1024 and 65535
     *   [proxyUser] is set
     *   [proxyPassword] is set
     *
     * See [BaseConsts.ProxyType.DISABLED]
     * */
    abstract val proxyType: @ProxyType String

    /**
     * See [DEFAULT__PROXY_USER]
     * */
    abstract val proxyUser: String?

    /**
     * Adds to the torrc file "ReachableAddresses <[reachableAddressPorts]>"
     *
     * See [DEFAULT__REACHABLE_ADDRESS_PORTS]
     * */
    abstract val reachableAddressPorts: String

    /**
     * See [DEFAULT__RELAY_NICKNAME]
     *
     * See [relayPort] documentation.
     * */
    abstract val relayNickname: String?

    /**
     * TorBrowser and Orbot use 9001 by default. It may be wise to pick something
     * that won't conflict.
     *
     * Adds to the torrc file "ORPort <[relayPort]>"
     *
     * This only gets used if you declare the following settings set as:
     *   [hasReachableAddress] false
     *   [hasBridges] false
     *   [isRelay] true
     *   [relayNickname] "your nickname"
     *   [relayPort] "auto", or a port between "1024" and "65535"
     *
     * **Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#ORPort
     *
     * See [BaseConsts.PortOption.DISABLED]
     * */
    abstract val relayPort: String

    /**
     * If `true`, adds to the torrc file "RunAsDaemon 1"
     * See [DEFAULT__RUN_AS_DAEMON]
     * */
    abstract val runAsDaemon: Boolean

    /**
     * Could be "auto" or a specific port, such as "9051".
     *
     * TorBrowser uses "9150", and Orbot uses "9050" by default. It may be wise
     * to pick something that won't conflict.
     *
     * See [BaseConsts.PortOption]
     * */
    abstract val socksPort: String

    /**
     * Express isolation flags to be added when enabling the [socksPort]
     *
     * **Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#SocksPort
     *
     * See [BaseConsts.IsolationFlag] for available options
     * */
    abstract val socksPortIsolationFlags: List<@IsolationFlag String>?

    /**
     * Can be "auto", or a specified port such as "9141"
     *
     * See [listOfSupportedBridges] documentation.
     *
     * Orbot and TorBrowser default to "9140" and "9040" respectively. It may be wise to pick
     * something that won't conflict.
     *
     * See [BaseConsts.PortOption.DISABLED]
     * */
    abstract val transPort: String

    /**
     * Express isolation flags to be added when enabling the [transPort]
     *
     * **Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#SocksPort
     *
     * See [BaseConsts.IsolationFlag] for available options
     * */
    abstract val transPortIsolationFlags: List<@IsolationFlag String>?

    /**
     * See [DEFAULT__USE_SOCKS5]
     * */
    abstract val useSocks5: Boolean

    /**
     * TorBrowser and Orbot use "10.192.0.1/10", it may be wise to pick something
     * that won't conflict if you are using this setting.
     *
     * Docs: https://2019.www.torproject.org/docs/tor-manual.html.en#VirtualAddrNetworkIPv6
     * */
    abstract val virtualAddressNetwork: String?
}