[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseServiceTorSettings](index.md) / [isAutoMapHostsOnResolveSave](./is-auto-map-hosts-on-resolve-save.md)

# isAutoMapHostsOnResolveSave

`@WorkerThread abstract fun isAutoMapHostsOnResolveSave(boolean: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/BaseServiceTorSettings.kt#L717)

Saves the value for [isAutoMapHostsOnResolve](is-auto-map-hosts-on-resolve.md) to [TorServicePrefs](../-tor-service-prefs/index.md). If the value is the same
as what is declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../-tor-service-prefs/index.md) is queried to remove the
setting if it exists.

### Parameters

`boolean` - to enable/disable

**See Also**

[TorSettings.isAutoMapHostsOnResolve](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/is-auto-map-hosts-on-resolve.md)

