package com.michael.kompanion.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */

/**
 * Switches the execution context to IO dispatcher and executes the provided block.
 *
 * @param block The suspending block of code to be executed on the IO dispatcher.
 * @return The result of the block.
 */
suspend fun <T> kompanionWithIO(block: suspend CoroutineScope.() -> T): T {
    return withContext(Dispatchers.IO, block)
}

/**
 * Launches a new coroutine on the IO dispatcher without returning any result.
 *
 * @param runner The suspending block to be executed.
 */
fun kompanionCoIO(runner: suspend CoroutineScope.() -> Unit) =
    CoroutineScope(Dispatchers.IO).launch { runner.invoke(this) }

/**
 * Launches a new coroutine on the Main dispatcher without returning any result.
 *
 * @param runner The suspending block to be executed.
 */
fun kompanionCoMain(runner: suspend CoroutineScope.() -> Unit) =
    CoroutineScope(Dispatchers.Main).launch { runner.invoke(this) }

/**
 * Maps over an iterable and applies the given function concurrently, using coroutines.
 *
 * @param f The suspending function to apply to each element.
 * @return A list of results of the function applied to the iterable elements.
 */
suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> =
    coroutineScope {
        map { async { f(it) } }.awaitAll()
    }

/**
 * Executes a suspending block asynchronously and returns its result.
 *
 * @param block The suspending block to be executed asynchronously.
 * @return The result of the block.
 */
suspend fun <T> coAsync(block: suspend () -> T): T = coroutineScope {
    val deferred = async { block.invoke() }
    deferred.await()
}

/**
 * Executes a suspending block in the background using the Default dispatcher and returns a nullable integer.
 *
 * @param block The suspending block to be executed.
 * @return The nullable integer result of the block.
 */
suspend fun onBackgroundReturnable(block: suspend () -> Int?): Int? {
    return withContext(Dispatchers.Default) {
        block()
    }
}

/**
 * Executes a suspending block in the background using the Default dispatcher and returns its result.
 *
 * @param block The suspending block to be executed.
 * @return The result of the block.
 */
suspend fun <T> onBackground(block: suspend () -> T): T {
    return withContext(Dispatchers.Default) {
        block()
    }
}

/**
 * Executes a suspending block on the IO dispatcher without returning a result.
 *
 * @param block The suspending block to be executed.
 */
suspend fun <T> toBackground(block: suspend () -> T) {
    coroutineScope {
        async(Dispatchers.IO) {
            block()
        }.await()
    }
}

/**
 * Executes a suspending block on the Main dispatcher without returning a result.
 *
 * @param block The suspending block to be executed.
 */
suspend fun <T> toMain(block: suspend () -> T) {
    coroutineScope {
        async(Dispatchers.Main) {
            block()
        }.await()
    }
}

/**
 * Executes a suspending block on the Default dispatcher without returning a result.
 *
 * @param block The suspending block to be executed.
 */
suspend fun <T> toDefault(block: suspend () -> T) {
    coroutineScope {
        async(Dispatchers.Default) {
            block()
        }.await()
    }
}

/**
 * Awaits the result of a Deferred task, returning null in case of an exception.
 *
 * @return The result of the Deferred task or null in case of an exception.
 */
suspend fun <T> Deferred<T>.kompanionAwaitOrNull(): T? {
    return kompanionSafeReturnableSuspendOperation(
        operation = {
            await()
        },
    )
}

/**
 * Retries a suspending operation a given number of times with an exponential backoff strategy.
 *
 * @param times The number of retry attempts.
 * @param initialDelayMs The initial delay before retrying (in milliseconds).
 * @param maxDelayMs The maximum delay between retries (in milliseconds).
 * @param factor The factor by which the delay increases after each attempt.
 * @param block The suspending block to execute.
 * @return The result of the block after retries.
 */
suspend fun <T> retry(
    times: Int,
    initialDelayMs: Long = 100,
    maxDelayMs: Long = 10000,
    factor: Double = 2.0,
    block: suspend () -> T
): T {
    var currentDelay = initialDelayMs
    repeat(times - 1) {
        try {
            return block()
        } catch (e: Exception) {
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelayMs)
        }
    }
    return block() // Last attempt without delay
}

/**
 * Combines two Flows into a Flow of Pairs, emitting elements from both flows together.
 *
 * @param other The other Flow to combine with.
 * @return A Flow of Pairs, where each Pair contains one element from each Flow.
 */
fun <T1, T2> Flow<T1>.combine(other: Flow<T2>): Flow<Pair<T1, T2>> =
    combine(other) { t1, t2 -> Pair(t1, t2) }

/**
 * Combines two Flows and transforms their elements using the provided transform function.
 *
 * @param other The other Flow to combine with.
 * @param transform The transformation function to apply to the elements from both Flows.
 * @return A Flow emitting the transformed results.
 */
fun <T1, T2, R> Flow<T1>.combine(other: Flow<T2>, transform: (T1, T2) -> R): Flow<R> = flow {
    val outerFlow = this@combine
    outerFlow.map { outer ->
        Pair(outer, other)
    }.collect { (outer, innerFlow) ->
        innerFlow.map { inner ->
            transform(outer, inner)
        }.collect { result ->
            emit(result)
        }
    }
}

/**
 * Launches a coroutine on the IO dispatcher.
 *
 * @param block The suspending block to be executed.
 */
fun CoroutineScope.launchIO(block: suspend CoroutineScope.() -> Unit) {
    launch(Dispatchers.IO) {
        block()
    }
}

/**
 * Launches a coroutine on the Main dispatcher.
 *
 * @param block The suspending block to be executed.
 */
fun CoroutineScope.launchMain(block: suspend CoroutineScope.() -> Unit) {
    launch(Dispatchers.Main) {
        block()
    }
}

/**
 * Runs a given suspending block of code asynchronously and ignores the result.
 *
 * @param block The suspending block of code to run.
 * @return A Job representing the coroutine.
 */
fun kompanionLaunch(block: suspend CoroutineScope.() -> Unit): Job =
    CoroutineScope(Dispatchers.Default).launch { block() }

/**
 * Creates a coroutine that runs the given suspending block of code and repeats it periodically until the coroutine is cancelled.
 *
 * @param intervalMillis The interval in milliseconds between executions.
 * @param block The suspending block of code to repeat.
 * @return A Job representing the coroutine.
 */
fun kompanionRepeatPeriodically(intervalMillis: Long, block: suspend () -> Unit): Job =
    CoroutineScope(Dispatchers.Default).launch {
        while (isActive) {
            block()
            delay(intervalMillis)
        }
    }

/**
 * Creates a coroutine that runs the given suspending block of code with exponential backoff delay and retries it until it succeeds or the max retry count is reached.
 *
 * @param maxRetries The maximum number of retry attempts.
 * @param initialDelayMillis The initial delay in milliseconds before the first retry.
 * @param factor The multiplier for the delay after each retry.
 * @param block The suspending block of code to execute.
 * @return The result of the block if successful.
 * @throws Exception If the maximum retries are exceeded.
 */
suspend fun <T> kompanionRetryWithExponentialBackoff(
    maxRetries: Int = 3,
    initialDelayMillis: Long = 1000,
    factor: Long = 2L,
    block: suspend () -> T
): T {
    var currentDelay = initialDelayMillis
    repeat(maxRetries) {
        try {
            return block()
        } catch (e: Exception) {
            delay(currentDelay)
            currentDelay *= factor
        }
    }
    return block() // Last attempt without delay if max retries exceeded
}

/**
 * Cancels the given job if it's not null and is active.
 *
 * @param job The job to cancel.
 */
fun kompanionCancelJob(job: Job?) {
    job?.let {
        if (it.isActive) it.cancel()
    }
}

/**
 * Measures the execution time of the given suspending block of code and returns it in milliseconds.
 *
 * @param block The suspending block of code to measure.
 * @return The execution time in milliseconds.
 */
suspend fun kompanionMeasureTimeMillis(block: suspend () -> Unit): Long {
    val startTime = System.currentTimeMillis()
    block()
    return System.currentTimeMillis() - startTime
}

/**
 * Runs the given suspending block of code in a background thread, then switches to the main thread to execute the specified action with the result.
 *
 * @param backgroundBlock The suspending block of code to run in the background.
 * @param mainThreadAction The action to execute on the main thread with the result.
 */
fun <T> kompanionRunInBackgroundAndThen(
    backgroundBlock: suspend () -> T,
    mainThreadAction: (T) -> Unit
) {
    CoroutineScope(Dispatchers.Main).launch {
        val result = withContext(Dispatchers.Default) { backgroundBlock() }
        mainThreadAction(result)
    }
}

/**
 * Executes the given suspending block of code asynchronously on the IO dispatcher and returns its result.
 * Should be used when:
 * a. making a network call
 * b. making a database query
 * c. making a file read operation
 *
 * @param block The suspending block of code to execute.
 * @return The result of the block.
 */
suspend fun <T> kompanionWithContextIO(block: suspend () -> T): T =
    withContext(Dispatchers.IO) { block() }

/**
 * Executes the given suspending block of code asynchronously on the main thread dispatcher and returns its result.
 * Should be used when:
 * a. updating UI
 * b. making a database update like insert, update, delete
 * c. making a file write operation
 *
 * @param block The suspending block of code to execute.
 * @return The result of the block.
 */
suspend fun <T> kompanionWithContextMain(block: suspend () -> T): T =
    withContext(Dispatchers.Main) { block() }

/**
 * Executes the given suspending block of code asynchronously on the default dispatcher and returns its result.
 * Should be used when:
 * a. operation is not tied to IO or UI thread
 * b. operation is expensive to execute
 * c. a result is needed
 *
 * @param block The suspending block of code to execute.
 * @return The result of the block.
 */
suspend fun <T> kompanionWithContextDefault(block: suspend () -> T): T =
    withContext(Dispatchers.Default) { block() }

/**
 * Executes the given suspending block of code asynchronously on the IO dispatcher and returns its result as a Deferred.
 *
 * @param block The suspending block of code to execute.
 * @return A Deferred representing the result of the block.
 */
fun <T> kompanionAsyncIO(block: suspend () -> T): Deferred<T> =
    CoroutineScope(Dispatchers.IO).async { block() }

/**
 * Executes the given suspending block of code asynchronously on the main thread dispatcher and returns its result as a Deferred.
 *
 * @param block The suspending block of code to execute.
 * @return A Deferred representing the result of the block.
 */
fun <T> kompanionAsyncMain(block: suspend () -> T): Deferred<T> =
    CoroutineScope(Dispatchers.Main).async { block() }

/**
 * Executes the given suspending block of code asynchronously on the default dispatcher and returns its result as a Deferred.
 *
 * @param block The suspending block of code to execute.
 * @return A Deferred representing the result of the block.
 */
fun <T> kompanionAsyncDefault(block: suspend () -> T): Deferred<T> =
    CoroutineScope(Dispatchers.Default).async { block() }

/**
 * Executes the given suspending block of code asynchronously and calls the specified actions when it completes or throws an exception.
 *
 * @param block The suspending block of code to execute.
 * @param onComplete Action to execute upon successful completion.
 * @param onError Action to execute if an error occurs.
 * @return A Job representing the coroutine.
 */
fun kompanionAsyncWithCallbacks(
    block: suspend () -> Unit,
    onComplete: () -> Unit = {},
    onError: (Throwable) -> Unit = {}
): Job = CoroutineScope(Dispatchers.Default).launch {
    try {
        block()
        onComplete()
    } catch (e: Exception) {
        onError(e)
    }
}

/**
* Cancels a current Job before starting a new one.
*
* @param currentJob The current job to cancel.
* @param block The suspending block of code to run in the new job.
* @return A Job representing the new coroutine.
*/
fun kompanionCancelAndStartNewJob(
    currentJob: Job?,
    block: suspend () -> Unit
): Job {
    kompanionCancelJob(currentJob)
    return CoroutineScope(Dispatchers.Default).launch {
        block()
    }
}

/**
 * Executes a list of suspending functions in parallel and collects their results.
 *
 * ```Kt
 *
 * suspend fun task1(): String {
 *     delay(1000L)
 *     return "Task 1 result"
 * }
 *
 * suspend fun task2(): String {
 *     delay(500L)
 *     return "Task 2 result"
 * }
 *
 * runBlocking {
 *     val results = listOf(::task1, ::task2).parallelExecute()
 *     println(results) // Output: [Task 1 result, Task 2 result]
 * }
 *```
 * @return A list of results from the executed suspending functions.
 */
suspend fun <T> List<suspend () -> T>.kompanionParallelExecute(): List<T> = coroutineScope {
    map { async { it() } }.awaitAll()
}

