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
 * Example usage:
 *
 * ```Kt
 * val result = kompanionWithIO {
 *     // Perform a database query or file read operation
 *     fetchUserFromDatabase()
 * }
 * ```
 *
 * When to use:
 * Use this method when you are performing IO-bound operations, such as reading from or writing to a file, making network calls, or querying a database.
 * It ensures that the operation is performed on the IO dispatcher, which is optimized for IO tasks.
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
 * Example usage:
 *
 * ```Kt
 * kompanionCoIO {
 *     // Perform background logging or analytics reporting
 *     performLoggingTask()
 * }
 * ```
 *
 * When to use:
 * Use this method to perform fire-and-forget tasks such as logging, network calls, or background tasks that don't require a result.
 * It runs the coroutine on the IO dispatcher, which is designed for IO-bound operations.
 *
 * @param runner The suspending block to be executed.
 */
fun kompanionCoIO(runner: suspend CoroutineScope.() -> Unit) =
    CoroutineScope(Dispatchers.IO).launch { runner.invoke(this) }

/**
 * Launches a new coroutine on the Main dispatcher without returning any result.
 *
 * Example usage:
 *
 * ```Kt
 * kompanionCoMain {
 *     // Update a UI element
 *     updateTextViewOnMainThread()
 * }
 * ```
 *
 * When to use:
 * This method is useful for performing UI-related tasks, such as updating views, animations, or user interactions.
 * It runs on the Main dispatcher, ensuring that your UI is updated on the correct thread.
 *
 * @param runner The suspending block to be executed.
 */
fun kompanionCoMain(runner: suspend CoroutineScope.() -> Unit) =
    CoroutineScope(Dispatchers.Main).launch { runner.invoke(this) }

/**
 * Maps over an iterable and applies the given function concurrently, using coroutines.
 *
 * Example usage:
 *
 * ```Kt
 * val results = listOf(1, 2, 3).pmap { number ->
 *     // Perform asynchronous operation for each item
 *     processNumberAsync(number)
 * }
 * println(results) // Prints processed results
 * ```
 *
 * When to use:
 * Use this method when you need to apply a suspending function to each item in a collection concurrently.
 * It is useful for parallelizing tasks, such as making multiple API calls at once or processing a list of files concurrently.
 *
 * @param f The suspending function to apply to each element.
 * @return A list of results from the applied function.
 */
suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> =
    coroutineScope {
        map { async { f(it) } }.awaitAll()
    }

/**
 * Executes a suspending block asynchronously and returns its result.
 *
 * Example usage:
 *
 * ```Kt
 * val result = coAsync {
 *     // Perform an async operation, e.g., network call
 *     fetchDataFromApi()
 * }
 * ```
 *
 * When to use:
 * Use this method when you need to run a task asynchronously and wait for the result, such as making network calls or reading from a database in parallel.
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
 * Example usage:
 *
 * ```Kt
 * val result = onBackgroundReturnable {
 *     // Perform a CPU-intensive task that returns a nullable integer
 *     performCalculationOrReturnNull()
 * }
 * ```
 *
 * When to use:
 * This method is useful when performing a CPU-bound task (e.g., calculations) that might return a nullable integer result.
 * It runs the operation on the Default dispatcher, which is optimized for CPU-bound tasks.
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
 * Example usage:
 *
 * ```Kt
 * val result = onBackground {
 *     // Perform a CPU-intensive background task
 *     performHeavyComputation()
 * }
 * ```
 *
 * When to use:
 * Use this method for CPU-bound operations like data processing, parsing, or heavy computation.
 * It runs the task on the Default dispatcher, which is optimized for CPU-intensive tasks.
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
 * Example usage:
 *
 * ```Kt
 * toBackground {
 *     // Run a file read operation in the background
 *     readFileInBackground()
 * }
 * ```
 *
 * When to use:
 * Use this method for fire-and-forget IO operations such as reading files, making network calls, or querying databases.
 * It ensures the block runs on the IO dispatcher.
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
 * Example usage:
 *
 * ```Kt
 * toMain {
 *     // Update UI elements on the main thread
 *     updateTextViewOnMainThread()
 * }
 * ```
 *
 * When to use:
 * Use this method when you need to switch to the **Main** dispatcher to update UI elements.
 * It ensures that the block runs on the main thread for proper UI handling.
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
 * Example usage:
 *
 * ```Kt
 * toDefault {
 *     // Perform a CPU-bound task in the background
 *     performHeavyComputation()
 * }
 * ```
 *
 * When to use:
 * Use this method for background CPU-bound tasks like parsing, sorting, or computations. It runs on the Default dispatcher, optimized for CPU tasks.
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
 * Example usage:
 *
 * ```Kt
 * val result: Int? = someDeferredTask.kompanionAwaitOrNull()
 * println(result) // Output could be null if an exception occurred
 * ```
 *
 * When to use:
 * Use this method to safely await the result of a Deferred task, ensuring that null is returned in case of exceptions.
 *
 * @return The result of the Deferred task or null in case of an exception.
 */
suspend fun <T> Deferred<T>.kompanionAwaitOrNull(): T? {
    return kompanionSafeNullableSuspendCall(
        operation = {
            await()
        },
    )
}

/**
 * Retries a suspending operation a given number of times with an exponential backoff strategy.
 *
 * Example usage:
 *
 * ```Kt
 * val result = retry(3, 100, 1000, 2.0) {
 *     // Perform a retryable operation, like a network request
 *     fetchDataFromApi()
 * }
 * ```
 *
 * When to use:
 * This method is useful for retrying a suspending operation that might fail intermittently, such as network requests.
 * It uses an exponential backoff strategy for retries.
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
 * Example usage:
 *
 * ```Kt
 * val combinedFlow = flow1.combine(flow2)
 * combinedFlow.collect { (item1, item2) ->
 *     println("Flow1: $item1, Flow2: $item2")
 * }
 * ```
 *
 * When to use:
 * Use this method when you want to combine two Flows into a single Flow that emits pairs of elements.
 * This is useful when you need to work with data from two sources simultaneously.
 *
 * @param other The other Flow to combine with.
 * @return A Flow of Pairs, where each Pair contains one element from each Flow.
 */
fun <T1, T2> Flow<T1>.combine(other: Flow<T2>): Flow<Pair<T1, T2>> =
    combine(other) { t1, t2 -> Pair(t1, t2) }

/**
 * Combines two Flows and transforms their elements using the provided transform function.
 *
 * Example usage:
 *
 * ```Kt
 * val transformedFlow = flow1.combine(flow2) { item1, item2 ->
 *     // Transform elements
 *     item1 + item2
 * }
 * ```
 *
 * When to use:
 * Use this method when you need to combine two flows and apply a transformation to their emitted elements.
 * It's useful for merging and processing data from two streams.
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
 * Example usage:
 *
 * ```Kt
 * launchIO {
 *     // Perform file or network operations
 *     performFileOperation()
 * }
 * ```
 *
 * When to use:
 * Use this method to launch a coroutine on the IO dispatcher for IO-bound tasks, such as file reading/writing, network requests, or database queries.
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
 * Example usage:
 *
 * ```Kt
 * launchMain {
 *     // Update UI elements
 *     updateTextViewOnMainThread()
 * }
 * ```
 *
 * When to use:
 * Use this method to run tasks that need to be executed on the Main thread, such as updating the UI or handling user input.
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
 * Example usage:
 *
 * ```Kt
 * kompanionLaunch {
 *     // Perform a task without waiting for the result
 *     backgroundTask()
 * }
 * ```
 *
 * When to use:
 * Use this method for running fire-and-forget tasks where you do not need to handle the result.
 *
 * @param block The suspending block of code to run.
 * @return A Job representing the coroutine.
 */
fun kompanionLaunch(block: suspend CoroutineScope.() -> Unit): Job =
    CoroutineScope(Dispatchers.Default).launch { block() }

/**
 * Creates a coroutine that runs the given suspending block of code and repeats it periodically until the coroutine is cancelled.
 *
 * Example usage:
 *
 * ```Kt
 * val job = kompanionRepeatPeriodically(1000L) {
 *     // Perform a task every second
 *     checkForUpdates()
 * }
 * ```
 *
 * When to use:
 * Use this method when you need to run a task periodically at a fixed interval, such as polling for data or refreshing UI content.
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
 * Example usage:
 *
 * ```Kt
 * val result = kompanionRetryWithExponentialBackoff(5, 1000L) {
 *     // Retry a network call or other operation
 *     performNetworkOperation()
 * }
 * ```
 *
 * When to use:
 * Use this method when you want to retry a suspending operation with increasing delays between retries (exponential backoff), which is useful for unstable network requests or API calls.
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
 * Example usage:
 *
 * ```Kt
 * kompanionCancelJob(job)
 * ```
 *
 * When to use:
 * Use this method to safely cancel a Job if it is not null and currently active.
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
 * Example usage:
 *
 * ```Kt
 * val timeTaken = kompanionMeasureTimeMillis {
 *     // Perform a task to measure execution time
 *     performHeavyOperation()
 * }
 * println("Time taken: $timeTaken ms")
 * ```
 *
 * When to use:
 * Use this method when you need to measure how long a suspending block of code takes to execute.
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
 * Example usage:
 *
 * ```Kt
 * kompanionRunInBackgroundAndThen(
 *     backgroundBlock = {
 *         // Perform a background task, such as a network request
 *         fetchDataFromApi()
 *     },
 *     mainThreadAction = { result ->
 *         // Update UI with the result on the main thread
 *         updateUIWithData(result)
 *     }
 * )
 * ```
 *
 * When to use:
 * Use this method when you need to run a task in the background and then perform an action on the main thread once the task is complete.
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
 *
 * Example usage:
 *
 * ```Kt
 * val result = kompanionWithContextIO {
 *     // Perform a network request
 *     fetchDataFromServer()
 * }
 * ```
 *
 * When to use:
 * Use this method when you need to run a suspending IO-bound task asynchronously, such as network calls, file reading/writing, or database operations.
 *
 * @param block The suspending block of code to execute.
 * @return The result of the block.
 */
suspend fun <T> kompanionWithContextIO(block: suspend () -> T): T =
    withContext(Dispatchers.IO) { block() }

/**
 * Executes the given suspending block of code asynchronously on the main thread dispatcher and returns its result.
 *
 * Example usage:
 *
 * ```Kt
 * val result = kompanionWithContextMain {
 *     // Update UI or handle user input
 *     performUIUpdate()
 * }
 * ```
 *
 * When to use:
 * Use this method for tasks that need to be executed on the main thread, such as updating UI elements or handling user input.
 *
 * @param block The suspending block of code to execute.
 * @return The result of the block.
 */
suspend fun <T> kompanionWithContextMain(block: suspend () -> T): T =
    withContext(Dispatchers.Main) { block() }

/**
 * Executes the given suspending block of code asynchronously on the default dispatcher and returns its result.
 *
 * Example usage:
 *
 * ```Kt
 * val result = kompanionWithContextDefault {
 *     // Perform CPU-intensive operations
 *     processData()
 * }
 * ```
 *
 * When to use:
 * Use this method when you need to run CPU-bound tasks asynchronously, such as data processing or computation.
 *
 * @param block The suspending block of code to execute.
 * @return The result of the block.
 */
suspend fun <T> kompanionWithContextDefault(block: suspend () -> T): T =
    withContext(Dispatchers.Default) { block() }

/**
 * Executes the given suspending block of code asynchronously on the IO dispatcher and returns its result as a Deferred.
 *
 * Example usage:
 *
 * ```Kt
 * val deferredResult = kompanionAsyncIO {
 *     // Perform a file operation asynchronously
 *     readFileAsync()
 * }
 * val result = deferredResult.await()
 * ```
 *
 * When to use:
 * Use this method to run IO-bound tasks asynchronously and return the result as a Deferred, allowing you to await the result later.
 *
 * @param block The suspending block of code to execute.
 * @return A Deferred representing the result of the block.
 */
fun <T> kompanionAsyncIO(block: suspend () -> T): Deferred<T> =
    CoroutineScope(Dispatchers.IO).async { block() }

/**
 * Executes the given suspending block of code asynchronously on the main thread dispatcher and returns its result as a Deferred.
 *
 * Example usage:
 *
 * ```Kt
 * val deferredResult = kompanionAsyncMain {
 *     // Perform UI-related task asynchronously
 *     fetchDataForUI()
 * }
 * val result = deferredResult.await()
 * ```
 *
 * When to use:
 * Use this method when you need to run tasks on the main thread and return the result asynchronously as a Deferred.
 *
 * @param block The suspending block of code to execute.
 * @return A Deferred representing the result of the block.
 */
fun <T> kompanionAsyncMain(block: suspend () -> T): Deferred<T> =
    CoroutineScope(Dispatchers.Main).async { block() }

/**
 * Executes the given suspending block of code asynchronously on the default dispatcher and returns its result as a Deferred.
 *
 * Example usage:
 *
 * ```Kt
 * val deferredResult = kompanionAsyncDefault {
 *     // Perform a CPU-bound task asynchronously
 *     performHeavyComputation()
 * }
 * val result = deferredResult.await()
 * ```
 *
 * When to use:
 * Use this method for CPU-bound tasks that can run asynchronously, returning a Deferred to allow awaiting the result.
 *
 * @param block The suspending block of code to execute.
 * @return A Deferred representing the result of the block.
 */
fun <T> kompanionAsyncDefault(block: suspend () -> T): Deferred<T> =
    CoroutineScope(Dispatchers.Default).async { block() }

/**
 * Executes the given suspending block of code asynchronously and calls the specified actions when it completes or throws an exception.
 *
 * Example usage:
 *
 * ```Kt
 * kompanionAsyncWithCallbacks(
 *     block = { performTask() },
 *     onComplete = { println("Task completed!") },
 *     onError = { error -> println("Error occurred: $error") }
 * )
 * ```
 *
 * When to use:
 * Use this method when you want to execute a block asynchronously and handle completion or errors with callbacks.
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
 * Example usage:
 *
 * ```Kt
 * val job = kompanionCancelAndStartNewJob(currentJob) {
 *     // Perform the new task
 *     performTask()
 * }
 * ```
 *
 * When to use:
 * Use this method when you want to cancel an existing job before starting a new one.
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

