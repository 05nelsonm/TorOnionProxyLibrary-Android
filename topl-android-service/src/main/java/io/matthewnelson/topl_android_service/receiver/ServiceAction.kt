package io.matthewnelson.topl_android_service.receiver

import androidx.annotation.StringDef

abstract class ServiceAction {
    @Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY_GETTER)
    @StringDef(
        ACTION_STOP,
        ACTION_RESTART,
        ACTION_RENEW
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Action

    @get:Action
    abstract var serviceAction: String

    companion object {
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_RESTART = "ACTION_RESTART"
        const val ACTION_RENEW = "ACTION_RENEW"
    }
}