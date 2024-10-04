package com.michael.kompanion.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */


/**
* Collects items from a Flow while providing hooks for handling events like onStart, onEach, and onError.
* - onStart: Invoked before collecting items from the flow.
* - onEach: Called for each emitted item in the flow.
* - onError: Invoked when an error is caught during collection.
* Uses 'distinctUntilChanged' to avoid emitting duplicate consecutive items.
*
* @param onStart Lambda function invoked before collecting items.
* @param onEach Lambda function called for each emitted item.
* @param onError Lambda function invoked when an error is caught during collection.
*/
suspend inline fun <T : Any?> Flow<T>.kompanionCollectBy(
    onStart: () -> Unit = {},
    crossinline onEach: (T) -> Unit = { _ -> },
    crossinline onError: (Throwable) -> Unit = { _ -> },
) {
    try {
        onStart()
        catch { error -> onError(error) }
            .distinctUntilChanged()
            .collect { item -> onEach(item) }
    } catch (e: Exception) {
        onError(e)
    }
}

/*
 * Collects only the first item from the Flow and wraps it into a single-item flow for further operations.
 * - onStart: Invoked before collecting the first item from the flow.
 * - onItemReceived: Called when the first item is received.
 * - onError: Invoked when an error is caught.
 * Uses 'distinctUntilChanged' to avoid duplicate emissions and handles exceptions gracefully.
 *
 * @param onStart Lambda function invoked before collecting the first item.
 * @param onItemReceived Lambda function called when the first item is received.
 * @param onError Lambda function invoked when an error is caught.
 * @return A Flow that emits the first item wrapped in a single-item flow.
 */
suspend inline fun <T : Any?> Flow<T>.kompanionSingleFlow(
    onStart: () -> Unit = {},
    crossinline onItemReceived: (T) -> Unit = { _ -> },
    crossinline onError: (Throwable) -> Unit = { _ -> },
) {
    try {
        onStart()
        flowOf(first())
            .catch { error -> onError(error) }
            .distinctUntilChanged()
            .collect { item -> onItemReceived(item) }
    } catch (e: Exception) {
        onError(e)
    }
}

/**
 * Collects items from a Flow with a provided CoroutineScope, allowing the 'onEach' handler to be launched within the scope.
 * - onStart: Invoked before the collection starts.
 * - onEach: Suspends and handles each item received, executed within the provided CoroutineScope.
 * - onError: Called when an error occurs.
 * - coroutineScope: Defines the scope in which 'onEach' will be launched.
 * Handles errors and ensures 'onEach' is executed asynchronously within the given scope.
 *
 * @param onStart Lambda function invoked before the collection starts.
 * @param onEach Lambda function that handles each item received.
 * @param onError Lambda function invoked when an error occurs.
 * @param coroutineScope The CoroutineScope where 'onEach' will be launched.
 */
suspend fun <T : Any?> Flow<T>.kompanionCollectByWithScope(
    onStart: () -> Unit = {},
    onEach: suspend (T) -> Unit = { _ -> },
    onError: (Throwable) -> Unit = { _ -> },
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main),
) {
    try {
        onStart()
        catch { error -> onError(error) }
            .distinctUntilChanged()
            .collect { item ->
                coroutineScope.launch {
                    try {
                        onEach(item)
                    } catch (e: Exception) {
                        onError(e)
                    }
                }
            }
    } catch (e: Exception) {
        onError(e)
    }
}

/**
 * Collects the first item from the Flow and processes it within the provided CoroutineScope.
 * - onStart: Invoked before attempting to receive the first item.
 * - onItemReceived: Handles the first item asynchronously within the given scope.
 * - onError: Invoked in case of an error.
 * - coroutineScope: Defines the CoroutineScope where 'onItemReceived' will be executed.
 * Ensures that the first item is handled in an asynchronous manner within the given CoroutineScope.
 *
 * @param onStart Lambda function invoked before attempting to receive the first item.
 * @param onItemReceived Lambda function that handles the first item.
 * @param onError Lambda function invoked in case of an error.
 * @param coroutineScope The CoroutineScope where 'onItemReceived' will be executed.
 */
suspend fun <T : Any?> Flow<T>.kompanionSingleFlowOnItemReceivedInScope(
    onStart: () -> Unit = {},
    onItemReceived: suspend (T) -> Unit = { _ -> },
    onError: (Throwable) -> Unit = { _ -> },
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main),
) {
    try {
        onStart()
        firstOrNull()?.let { item ->
            coroutineScope.launch {
                try {
                    onItemReceived(item)
                } catch (e: Exception) {
                    onError(e)
                }
            }
        }
    } catch (e: Exception) {
        onError(e)
    }
}

/**
 * Collects items from a Flow and performs the given action (block) within the lifecycle of the provided AppCompatActivity.
 * - activity: The AppCompatActivity whose lifecycle scope is used for launching the collection.
 * - context: Optional CoroutineContext to run the flow on a specific dispatcher.
 * - block: The action to perform for each item collected from the flow.
 * Uses 'flowOn' to switch the context and 'launchIn' to collect within the lifecycle scope.
 *
 * @param activity The AppCompatActivity whose lifecycle scope is used.
 * @param context Optional CoroutineContext to run the flow on a specific dispatcher.
 * @param block Lambda function to perform for each item collected from the flow.
 */
fun <T> Flow<T>.kompanionCollectAsEffect(
    activity: AppCompatActivity,
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend (T) -> Unit,
) {
    activity.lifecycleScope.launch {
        onEach(block).flowOn(context).launchIn(this)
    }
}

/**
 * Converts a List into a Flow that emits the entire list as a single item.
 * The flow ensures that the same list is not emitted twice consecutively by using 'distinctUntilChanged'.
 *
 * @return A Flow that emits the entire list as a single item.
 */
fun <T> List<T>.kompanionAsFlow(): Flow<List<T>> =
    flow { emit(this@kompanionAsFlow) }
        .distinctUntilChanged()



/**
 * Processes a list in batches using Kotlin Flow.
 *
 * runBlocking {
 *     val data = listOf(1, 2, 3, 4, 5, 6)
 *
 *     data.batchProcessFlow(2).collect { batch ->
 *         println("Processing batch: $batch")
 *     }
 * }
 *
 */
fun <T> List<T>.kompanionBatchProcessFlow(batchSize: Int): Flow<List<T>> = flow {
    for (batch in this@kompanionBatchProcessFlow.chunked(batchSize)) {
        emit(batch)
    }
}
