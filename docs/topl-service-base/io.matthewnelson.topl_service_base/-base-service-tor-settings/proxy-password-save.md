[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseServiceTorSettings](index.md) / [proxyPasswordSave](./proxy-password-save.md)

# proxyPasswordSave

`@WorkerThread abstract fun proxyPasswordSave(proxyPassword: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/BaseServiceTorSettings.kt#L309)

Saves the value for [proxyPassword](proxy-password-save.md#io.matthewnelson.topl_service_base.BaseServiceTorSettings$proxyPasswordSave(kotlin.String)/proxyPassword) to [TorServicePrefs](../-tor-service-prefs/index.md). If the value is the same as what is
declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../-tor-service-prefs/index.md) is queried to remove the setting if
it exists.

### Parameters

`proxyPassword` -

**See Also**

[TorSettings.proxyPassword](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/proxy-password.md)

