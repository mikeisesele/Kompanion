package com.michael.kompanion.extensions

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.michael.kompanion.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Suppress("TooGenericExceptionCaught")
@Composable
fun <T> rememberStateWithLifecycle(
    stateFlow: StateFlow<T>,
    lifecycle: Lifecycle = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
): State<T> {
    val initialValue = remember(stateFlow) {
        stateFlow.value
    }
    return produceState(
        key1 = stateFlow,
        key2 = lifecycle,
        key3 = minActiveState,
        initialValue = initialValue,
    ) {
        lifecycle.repeatOnLifecycle(minActiveState) {
            stateFlow.collect {
                this@produceState.value = it
            }
        }
    }
}

/**
 * Create an effect that matches the lifecycle of the call site.
 * If LandingScreen recomposes, the delay shouldn't start again.
 *
 * https://stackoverflow.com/a/71626121/6510726
 */
@SuppressLint("ComposableNaming")
@Composable
fun <T> Flow<T>.collectAsEffect(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend (T) -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        onEach(block).flowOn(context).launchIn(this)
    }
}

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
