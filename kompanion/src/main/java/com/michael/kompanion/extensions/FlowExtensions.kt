package com.michael.kompanion.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.flow.transform


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

/**
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


/**
 * Performs the given action on each emitted value if the predicate is true.
 *
 * Example usage:
 *
 * ```Kt
 *
 * flowOf(1, 2, 3, 4, 5)
 *     .kompanionFlowOnEachIf({ it % 2 == 0 }) { println("Even number: $it") }
 *     .collect()
 * ```
 *
 * Output:
 * Even number: 2
 * Even number: 4
 *
 * When to use:
 * Use this method when you want to conditionally perform a side effect (e.g., logging or analytics) on each emission in a Flow.
 *
 * @param predicate A condition to check for each emitted value.
 * @param action A block to be executed if the condition is met.
 */
fun <T> Flow<T>.kompanionFlowOnEachIf(predicate: (T) -> Boolean, action: (T) -> Unit): Flow<T> =
    this.onEach { value ->
        if (predicate(value)) {
            action(value)
        }
    }



/**
 * Maps the emitted values of the flow to different values based on conditions.
 *
 * Example usage:
 *
 * ```Kt
 *
 * flowOf(1, 2, 3, 4, 5)
 *     .kompanionFlowConditionalMap {
 *         when {
 *             it % 2 == 0 -> "Even"
 *             it % 2 != 0 -> "Odd"
 *             else -> "Unknown"
 *         }
 *     }.collect { println(it) }
 * ```
 *
 * Output:
 * Odd
 * Even
 * Odd
 * Even
 * Odd
 *
 * When to use:
 * Use this method when you need to transform flow emissions based on specific conditions, similar to a switch-case structure for flow emissions.
 *
 * @param transform A block that transforms the emitted values based on conditions.
 * @return A flow of transformed values.
 */
fun <T, R> Flow<T>.kompanionFlowConditionalMap(transform: (T) -> R): Flow<R> =
    this.map { value ->
        transform(value)
    }



/**
 * Emits values only when they have changed compared to the previous emission.
 *
 * Example usage:
 *
 * ```Kt
 *
 * flowOf(1, 1, 2, 2, 3)
 *     .kompanionFlowWhenChanged()
 *     .collect { println(it) }
 * ```
 *
 * Output:
 * 1
 * 2
 * 3
 *
 * When to use:
 * Use this method when you want to filter out consecutive duplicate emissions and only react to new changes in the flow.
 *
 * @return A flow that emits values only when they differ from the previous value.
 */
fun <T> Flow<T>.kompanionFlowWhenChanged(): Flow<T> = transform { value ->
    var lastValue: T? = null
    if (value != lastValue) {
        lastValue = value
        emit(value)
    }
}

/**
 * Dynamically switches between different flows based on the emitted values.
 *
 * Example usage:
 *
 * ```Kt
 *
 * flowOf(1, 2, 3)
 *     .kompanionFlowSwitchMap { number ->
 *         if (number % 2 == 0) flowOf("Even Flow") else flowOf("Odd Flow")
 *     }.collect { println(it) }
 * ```
 *
 * Output:
 * Odd Flow
 * Even Flow
 * Odd Flow
 *
 * When to use:
 * Use this method when you want to dynamically switch between different flows depending on the emitted value.
 * For example, based on certain conditions, you might want to switch between fetching data from different sources.
 *
 * @param transform A block that returns a new flow based on the emitted value.
 * @return A new flow that switches between other flows based on the values emitted by the source flow.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T, R> Flow<T>.kompanionFlowSwitchMap(transform: (T) -> Flow<R>): Flow<R> =
    this.flatMapLatest { value ->
        transform(value)
    }


/**
 * Takes values from the flow until the specified predicate returns true.
 *
 * Example usage:
 *
 * ```Kt
 *
 * flowOf(1, 2, 3, 4, 5)
 *     .kompanionFlowTakeUntil { it == 3 }
 *     .collect { println(it) }
 * ```
 *
 * Output:
 * 1
 * 2
 * 3
 *
 * When to use:
 * Use this method when you want to take values from a flow until a certain condition is met, after which no more values will be collected.
 *
 * @param predicate The condition that determines when the flow should stop emitting.
 * @return A flow that stops emitting when the predicate is met.
 */
fun <T> Flow<T>.kompanionFlowTakeUntil(predicate: (T) -> Boolean): Flow<T> =
    this.takeWhile { value ->
        !predicate(value)
    }

/**
 * Conditionally debounces the flow emissions if the predicate is met.
 *
 * Example usage:
 *
 * ```Kt
 *
 * flowOf(1, 2, 3, 4)
 *     .kompanionFlowDebounceIf({ it % 2 == 0 }, 500L)
 *     .collect { println(it) }
 * ```
 *
 * When to use:
 * Use this method when you want to debounce flow emissions based on a specific condition.
 * This can be useful in scenarios where some emissions need to be delayed while others should flow through without delay.
 *
 * @param predicate The condition that determines whether debouncing should occur.
 * @param debounceTimeMillis The debounce time in milliseconds.
 * @return A flow with conditionally debounced emissions.
 */
@OptIn(FlowPreview::class)
fun <T> Flow<T>.kompanionFlowDebounceIf(predicate: (T) -> Boolean, debounceTimeMillis: Long): Flow<T> =
    this.debounce { value ->
        if (predicate(value)) debounceTimeMillis else 0L
    }