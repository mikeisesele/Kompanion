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

    suspend fun <T> kompanionWithIO(block: suspend CoroutineScope.() -> T): T {
        return withContext(Dispatchers.IO, block)
    }

    fun kompanionCoIO(runner: suspend CoroutineScope.() -> Unit) = CoroutineScope(Dispatchers.IO).launch { runner.invoke((this)) }

    fun kompanionCoMain(runner: suspend CoroutineScope.() -> Unit) = CoroutineScope(Dispatchers.Main).launch { runner.invoke((this)) }

    suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> =
        coroutineScope {
            map { async { f(it) } }.awaitAll()
        }

    suspend fun <T> coAsync(block: suspend () -> T): T = coroutineScope {
        val deferred = async { block.invoke() }
        deferred.await()
    }

    suspend fun onBackgroundReturnable(block: suspend () -> Int?) : Int? {
        return withContext(Dispatchers.Default) {
            block()
        }
    }

    suspend fun <T> onBackground(block: suspend () -> T): T {
        return withContext(Dispatchers.Default) {
            block()
        }
    }
    
    
    suspend fun <T> toBackground(block: suspend () -> T) {
        coroutineScope {
            async(Dispatchers.IO) {
                block()
            }.await()
        }
    }

    suspend fun <T> toMain(block: suspend () -> T) {
        coroutineScope {
            async(Dispatchers.Main) {
                block()
            }.await()
        }
    }


    suspend fun <T> toDefault(block: suspend () -> T) {
        coroutineScope {
            async(Dispatchers.Default) {
                block()
            }.await()
        }
    }


suspend fun <T> Deferred<T>.awaitOrNull(): T? {
    return safeReturnableSuspendOperation(
        operation = {
            await()
        },
        exceptionMessage = "Deferred operation failed"
    )
}

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

// Flow extension function to combine two flows
fun <T1, T2> Flow<T1>.combine(other: Flow<T2>): Flow<Pair<T1, T2>> =
    combine(other) { t1, t2 -> Pair(t1, t2) }
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

fun CoroutineScope.launchIO(block: suspend CoroutineScope.() -> Unit) {
    launch(Dispatchers.IO) {
        block()
    }
}

fun CoroutineScope.launchMain(block: suspend CoroutineScope.() -> Unit) {
    launch(Dispatchers.Main) {
        block()
    }
}

/**
 * Runs a given suspending block of code asynchronously and ignores the result.
 */
fun kompanionLaunch(block: suspend CoroutineScope.() -> Unit): Job =
    CoroutineScope(Dispatchers.Default).launch { block() }

/**
 * Creates a coroutine that runs the given suspending block of code and repeats it periodically until the coroutine is cancelled.
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
 */
fun kompanionCancelJob(job: Job?) {
    job?.let {
        if (it.isActive) it.cancel()
    }
}

/**
 * Measures the execution time of the given suspending block of code and returns it in milliseconds.
 */
suspend fun kompanionMeasureTimeMillis(block: suspend () -> Unit): Long {
    val startTime = System.currentTimeMillis()
    block()
    return System.currentTimeMillis() - startTime
}

/**
 * Runs the given suspending block of code in a background thread, then switches to the main thread to execute the specified action with the result.
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
 * should be used when
 * a. making a network call
 * b. making a database query
 * c. making a file read operation
 */
suspend fun <T> kompanionWithContextIO(block: suspend () -> T): T = withContext(Dispatchers.IO) { block() }

/**
 * Executes the given suspending block of code asynchronously on the main thread dispatcher and returns its result.
 * should be used when
 * a. updating UI
 * b. making a database update like insert, update, delete
 * c. making a file write operation
 */
suspend fun <T> kompanionWithContextMain(block: suspend () -> T): T = withContext(Dispatchers.Main) { block() }

/**
 * Executes the given suspending block of code asynchronously on the default dispatcher and returns its result.
 * should be used when
 * a. operation is not tied to IO or UI thread
 * b.  operation is expensive to execute
 * c. a result is needed
 */
suspend fun <T> kompanionWthContextDefault(block: suspend () -> T): T = withContext(Dispatchers.Default) { block() }


/**
 * Executes the given suspending block of code asynchronously on the IO dispatcher and returns its result as a Deferred.
 */
fun <T> kompanionAsyncIO(block: suspend () -> T): Deferred<T> = CoroutineScope(Dispatchers.IO).async { block() }

/**
 * Executes the given suspending block of code asynchronously on the main thread dispatcher and returns its result as a Deferred.
 */
fun <T> kompanionAsyncMain(block: suspend () -> T): Deferred<T> = CoroutineScope(Dispatchers.Main).async { block() }

/**
 * Executes the given suspending block of code asynchronously on the default dispatcher and returns its result as a Deferred.
 */
fun <T> kompanionAsyncDefault(block: suspend () -> T): Deferred<T> = CoroutineScope(Dispatchers.Default).async { block() }

/**
 * Executes the given suspending block of code asynchronously and calls the specified actions when it completes or throws an exception.
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

/*
* Cancels a current Job before starting a new one.
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
 *
 */
suspend fun <T> List<suspend () -> T>.komPanionParallelExecute(): List<T> = coroutineScope {
    map { async { it() } }.awaitAll()
}



