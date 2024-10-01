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

suspend inline fun <T : Any?> Flow<T>.collectBy(
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

suspend inline fun <T : Any?> Flow<T>.singleFlow(
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

suspend fun <T : Any?> Flow<T>.collectByWithScope(
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

suspend fun <T : Any?> Flow<T>.singleFlowOnItemReceivedInScope(
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



fun <T> Flow<T>.collectAsEffect(
    activity: AppCompatActivity,
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend (T) -> Unit,
) {
    activity.lifecycleScope.launch {
        onEach(block).flowOn(context).launchIn(this)
    }
}

fun <T> List<T>.asFlow(): Flow<List<T>> =
    flow { emit(this@asFlow) }
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
fun <T> List<T>.batchProcessFlow(batchSize: Int): Flow<List<T>> = flow {
    for (batch in this@batchProcessFlow.chunked(batchSize)) {
        emit(batch)
    }
}
