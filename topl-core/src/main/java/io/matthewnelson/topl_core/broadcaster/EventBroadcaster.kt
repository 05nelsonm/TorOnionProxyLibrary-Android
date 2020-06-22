/*
Copyright (c) Microsoft Open Technologies, Inc.
All Rights Reserved
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0

THIS CODE IS PROVIDED ON AN *AS IS* BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED,
INCLUDING WITHOUT LIMITATION ANY IMPLIED WARRANTIES OR CONDITIONS OF TITLE, FITNESS FOR A PARTICULAR PURPOSE,
MERCHANTABLITY OR NON-INFRINGEMENT.

See the Apache 2 License for the specific language governing permissions and limitations under the License.
*/
package io.matthewnelson.topl_core.broadcaster

import io.matthewnelson.topl_core_base.TorStates

/**
 * Service for sending event logs to the system
 */
abstract class EventBroadcaster: TorStates() {

    private lateinit var stateMachine: TorStateMachine

    val torStateMachine: TorStateMachine
        get() = if (!::stateMachine.isInitialized) {
            stateMachine = TorStateMachine(this)
            stateMachine
        } else {
            stateMachine
        }

    /**
     * Used by the [io.matthewnelson.topl_core.listener.BaseEventListener] to pipe the
     * connection bandwidth here.
     *
     * @param [bytesRead] bytes downloaded
     * @param [bytesWritten] bytes uploaded
     * */
    abstract fun broadcastBandwidth(bytesRead: String, bytesWritten: String)

    abstract fun broadcastDebug(msg: String)

    abstract fun broadcastException(msg: String?, e: Exception)

    abstract fun broadcastLogMessage(logMessage: String?)

    abstract fun broadcastNotice(msg: String)

    abstract fun broadcastTorState(@TorState state: String)

}