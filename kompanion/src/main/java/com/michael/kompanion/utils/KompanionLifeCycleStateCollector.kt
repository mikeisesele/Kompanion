package com.michael.kompanion.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


/**
LifeCycleStateCollector
-----------------------
This class provides a convenient way to observe and collect states from a given Flow<T>
within a specified Lifecycle. It ensures that the observation only occurs when the Lifecycle
is at least in the specified minimum active state (defaulted to STARTED).

Usage:
------
1. Create an instance of LifeCycleStateCollector by providing the following parameters:
- stateFlow: The Flow<T> to observe.
- lifecycle: The Lifecycle object associated with the lifecycle owner (e.g., Activity or Fragment).
- minActiveState: (Optional) The minimum active state of the lifecycle at which observation should occur.
Defaults to STARTED state.

2. Call the observe function on the LifeCycleStateCollector instance and provide a callback
to receive state updates.

Example:
--------
// Create an instance of LifeCycleStateCollector
val stateCollector = LifeCycleStateCollector(stateFlow, lifecycle)

// Observe state updates
stateCollector.observe { state ->
// Handle state updates here
}
 */
class KompanionLifeCycleStateCollector<T>(
    private val stateFlow: Flow<T>,
    private val lifecycle: Lifecycle,
    private val minActiveState: Lifecycle.State = Lifecycle.State.STARTED
) : LifecycleObserver {

    var currentStateValue: T? = null

    fun observe(callback: (T) -> Unit) {
        with(lifecycle) {
            coroutineScope.launch {
                repeatOnLifecycle(
                    state = minActiveState
                ) {
                    stateFlow.collect {
                        currentStateValue = it
                        callback(it)
                    }
                }
            }
        }
    }
}



/**
rememberStateWithLifecycle
---------------------------
This function is a convenient way to create an instance of LifeCycleStateCollector.
It takes the necessary parameters and returns a LifeCycleStateCollector instance configured
with the provided parameters.

Usage:
------
Call this function to create an instance of LifeCycleStateCollector with the specified parameters.

Parameters:
-----------
- stateFlow: The Flow<T> to observe.
- lifecycle: The Lifecycle object associated with the lifecycle owner (e.g., Activity or Fragment).
- minActiveState: (Optional) The minimum active state of the lifecycle at which observation should occur.
Defaults to STARTED state.

Returns:
--------
Returns a LifeCycleStateCollector instance configured with the provided parameters.

Example:
--------
// Create an instance of LifeCycleStateCollector using rememberStateWithLifecycle
val stateCollector = rememberStateWithLifecycle(stateFlow, lifecycle)
 */

fun <T> kompanionRememberStateWithLifecycle(
    stateFlow: Flow<T>,
    lifecycle: Lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
): KompanionLifeCycleStateCollector<T> {
    return KompanionLifeCycleStateCollector(stateFlow, lifecycle, minActiveState)
}