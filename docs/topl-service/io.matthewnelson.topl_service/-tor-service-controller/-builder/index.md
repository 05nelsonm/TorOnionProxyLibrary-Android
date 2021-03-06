[topl-service](../../../index.md) / [io.matthewnelson.topl_service](../../index.md) / [TorServiceController](../index.md) / [Builder](./index.md)

# Builder

`class Builder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L115)

The [TorServiceController.Builder](./index.md) is where you get to customize how [TorService](#) works
for your application. Call it in `Application.onCreate` and follow along.

``` kotlin
//  private fun generateTorServiceNotificationBuilder(context: Context): ServiceNotification.Builder {
        return ServiceNotification.Builder(
            channelName = "TOPL-Android Demo",
            channelDescription = "TorOnionProxyLibrary-Android Demo",
            channelID = "TOPL-Android Demo",
            notificationID = 615
        )
            .setImageTorNetworkingEnabled(drawableRes = R.drawable.tor_stat_network_enabled)
            .setImageTorNetworkingDisabled(drawableRes = R.drawable.tor_stat_network_disabled)
            .setImageTorDataTransfer(drawableRes = R.drawable.tor_stat_network_dataxfer)
            .setImageTorErrors(drawableRes = R.drawable.tor_stat_notifyerr)
            .setVisibility(visibility = NotificationCompat.VISIBILITY_PRIVATE)
            .setCustomColor(colorRes = R.color.primaryColor)
            .enableTorRestartButton(enable = true)
            .enableTorStopButton(enable = true)
            .showNotification(show = true)

            // Set the notification's contentIntent for when the user clicks the notification
            .also { builder ->
                context.applicationContext.packageManager
                    ?.getLaunchIntentForPackage(context.applicationContext.packageName)
                    ?.let { intent ->

                        // Set in your manifest for the launch activity so the intent won't launch
                        // a new activity over top of your already created activity if the app is
                        // open when the user clicks the notification:
                        //
                        // android:launchMode="singleInstance"
                        //
                        // For more info on launchMode and Activity Intent flags, see:
                        //
                        // https://medium.com/swlh/truly-understand-tasks-and-back-stack-intent-flags-of-activity-2a137c401eca

                        builder.setContentIntent(
                            PendingIntent.getActivity(
                                context.applicationContext,
                                0, // Your desired request code
                                intent,
                                0 // flags
                            // can also include a bundle if desired
                            )
                        )
                }
            }
//  }
```

``` kotlin
//  private fun generateBackgroundManagerPolicy(): BackgroundManager.Builder.Policy {
        return BackgroundManager.Builder()

            // All available options present. Only 1 is able to be chosen.
            .respectResourcesWhileInBackground(secondsFrom5To45 = 20)
            //.runServiceInForeground(killAppIfTaskIsRemoved = true)
//  }
```

``` kotlin
//  private fun setupTorServices(application: Application, torConfigFiles: TorConfigFiles ) {
        TorServiceController.Builder(
            application = application,
            torServiceNotificationBuilder = generateTorServiceNotificationBuilder(application),
            backgroundManagerPolicy = generateBackgroundManagerPolicy(),
            buildConfigVersionCode = BuildConfig.VERSION_CODE,

            // Can instantiate directly here then access it from
            // TorServiceController.Companion.getTorSettings() and cast what's returned
            // as MyTorSettings
            defaultTorSettings = MyTorSettings(),

            // These should live somewhere in your module's assets directory,
            // ex: my-project/my-application-module/src/main/assets/common/geoip
            // ex: my-project/my-application-module/src/main/assets/common/geoip6
            geoipAssetPath = "common/geoip",
            geoip6AssetPath = "common/geoip6"
        )
            .addTimeToDisableNetworkDelay(milliseconds = 1_000L)
            .addTimeToRestartTorDelay(milliseconds = 100L)
            .addTimeToStopServiceDelay(milliseconds = 100L)
            .disableStopServiceOnTaskRemoved(disable = false)
            .setBuildConfigDebug(buildConfigDebug = BuildConfig.DEBUG)

            // Can instantiate directly here then access it from
            // TorServiceController.Companion?.appEventBroadcaster and cast what's returned
            // as MyEventBroadcaster
            .setEventBroadcaster(eventBroadcaster = MyEventBroadcaster())
            .setServiceExecutionHooks(executionHooks = MyServiceExecutionHooks())

            // Only needed if you wish to customize the directories/files used by Tor if
            // the defaults aren't to your liking.
            .useCustomTorConfigFiles(torConfigFiles = torConfigFiles)

            .build()
//  }
```

### Parameters

`application` - [Application](https://developer.android.com/reference/android/app/Application.html), for obtaining context

`torServiceNotificationBuilder` - The [ServiceNotification.Builder](../../../io.matthewnelson.topl_service.notification/-service-notification/-builder/index.md) for
customizing [TorService](#)'s notification

`backgroundManagerPolicy` - The [BackgroundManager.Builder.Policy](../../../io.matthewnelson.topl_service.lifecycle/-background-manager/-builder/-policy.md) to be executed
while your application is in the background (the Recent App's tray).

`buildConfigVersionCode` - send [BuildConfig.VERSION_CODE](#). Mitigates copying of geoip
files to app updates only

`defaultTorSettings` - [ApplicationDefaultTorSettings](../../../..//topl-service-base/io.matthewnelson.topl_service_base/-application-default-tor-settings/index.md) used to create your torrc file
on start of Tor

`geoipAssetPath` - The path to where you have your geoip file located (ex: in
assets/common directory, send this variable "common/geoip")

`geoip6AssetPath` - The path to where you have your geoip6 file located (ex: in
assets/common directory, send this variable "common/geoip6")

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | The [TorServiceController.Builder](./index.md) is where you get to customize how [TorService](#) works for your application. Call it in `Application.onCreate` and follow along.`Builder(application: `[`Application`](https://developer.android.com/reference/android/app/Application.html)`, torServiceNotificationBuilder: Builder, backgroundManagerPolicy: Policy, buildConfigVersionCode: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, defaultTorSettings: `[`ApplicationDefaultTorSettings`](../../../..//topl-service-base/io.matthewnelson.topl_service_base/-application-default-tor-settings/index.md)`, geoipAssetPath: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, geoip6AssetPath: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)` |

### Functions

| Name | Summary |
|---|---|
| [addTimeToDisableNetworkDelay](add-time-to-disable-network-delay.md) | Default is set to 6_000ms, (what this method adds time to).`fun addTimeToDisableNetworkDelay(milliseconds: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): Builder` |
| [addTimeToRestartTorDelay](add-time-to-restart-tor-delay.md) | Default is set to 500ms, (what this method adds time to).`fun addTimeToRestartTorDelay(milliseconds: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): Builder` |
| [addTimeToStopServiceDelay](add-time-to-stop-service-delay.md) | Default is set to 100ms (what this method adds time to).`fun addTimeToStopServiceDelay(milliseconds: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): Builder` |
| [build](build.md) | Initializes [TorService](#) setup and enables the ability to call methods from the [Companion](#) object w/o throwing exceptions.`fun build(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [disableStopServiceOnTaskRemoved](disable-stop-service-on-task-removed.md) | When your task is removed from the Recent App's tray, [TorService.onTaskRemoved](#) is triggered. Default behaviour is to stop Tor, and then [TorService](#). Electing this option will inhibit the default behaviour from being carried out.`fun disableStopServiceOnTaskRemoved(disable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): Builder` |
| [setBuildConfigDebug](set-build-config-debug.md) | This makes it such that on your Application's **Debug** builds, the `topl-core` and `topl-service` modules will provide you with Logcat messages (when [TorSettings.hasDebugLogs](../../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/has-debug-logs.md) is enabled).`fun setBuildConfigDebug(buildConfigDebug: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): Builder` |
| [setEventBroadcaster](set-event-broadcaster.md) | Get broadcasts piped to your Application to do with them what you desire. What you send this will live at [Companion.appEventBroadcaster](../app-event-broadcaster.md) for the remainder of your application's lifecycle to refer to elsewhere in your App.`fun setEventBroadcaster(eventBroadcaster: `[`TorServiceEventBroadcaster`](../../../..//topl-service-base/io.matthewnelson.topl_service_base/-tor-service-event-broadcaster/index.md)`): Builder` |
| [setServiceExecutionHooks](set-service-execution-hooks.md) | Implement and set hooks to be executed in [TorService.onCreate](#), and [ServiceActionProcessor.processServiceAction](#) prior to starting of Tor, and post stopping of Tor.`fun setServiceExecutionHooks(executionHooks: `[`ServiceExecutionHooks`](../../../..//topl-service-base/io.matthewnelson.topl_service_base/-service-execution-hooks/index.md)`): Builder` |
| [useCustomTorConfigFiles](use-custom-tor-config-files.md) | If you wish to customize the file structure of how Tor is installed in your app, you can do so by instantiating your own [TorConfigFiles](../../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md) and customizing it via the [TorConfigFiles.Builder](../../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/-builder/index.md), or overridden method [TorConfigFiles.createConfig](../../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/-companion/create-config.md).`fun useCustomTorConfigFiles(torConfigFiles: `[`TorConfigFiles`](../../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md)`): Builder` |
