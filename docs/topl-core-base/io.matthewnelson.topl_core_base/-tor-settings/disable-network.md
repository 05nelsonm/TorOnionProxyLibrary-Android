[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [TorSettings](index.md) / [disableNetwork](./disable-network.md)

# disableNetwork

`abstract val disableNetwork: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/TorSettings.kt#L177)

OnionProxyManager will enable this on startup using the TorControlConnection based off
of the device's network state. Setting this to `true` is highly recommended.

Adds to the torrc file "DisableNetwork &lt;1 or 0&gt;"

See [DEFAULT__DISABLE_NETWORK](-d-e-f-a-u-l-t__-d-i-s-a-b-l-e_-n-e-t-w-o-r-k.md)

