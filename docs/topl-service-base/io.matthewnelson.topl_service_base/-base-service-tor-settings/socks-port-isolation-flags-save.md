[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseServiceTorSettings](index.md) / [socksPortIsolationFlagsSave](./socks-port-isolation-flags-save.md)

# socksPortIsolationFlagsSave

`@WorkerThread abstract fun socksPortIsolationFlagsSave(isolationFlags: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/BaseServiceTorSettings.kt#L493)

Saves the value for [isolationFlags](socks-port-isolation-flags-save.md#io.matthewnelson.topl_service_base.BaseServiceTorSettings$socksPortIsolationFlagsSave(kotlin.collections.List((kotlin.String)))/isolationFlags) to [TorServicePrefs](../-tor-service-prefs/index.md). If the value is the same as what is
declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../-tor-service-prefs/index.md) is queried to remove the setting if
it exists.

### Parameters

`isolationFlags` - A List of [io.matthewnelson.topl_core_base.BaseConsts.IsolationFlag](../../..//topl-core-base/io.matthewnelson.topl_core_base/-base-consts/-isolation-flag/index.md)'s
for the [socksPort](socks-port.md)

**See Also**

[io.matthewnelson.topl_core_base.BaseConsts.IsolationFlag](../../..//topl-core-base/io.matthewnelson.topl_core_base/-base-consts/-isolation-flag/index.md)

[TorSettings.socksPortIsolationFlags](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/socks-port-isolation-flags.md)

