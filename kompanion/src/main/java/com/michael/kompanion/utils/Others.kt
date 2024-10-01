package com.michael.kompanion.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext


/**
 * Safely executes an operation by wrapping it in a try-catch block. Operations to be invoked
 * should not have a return type.
 *
 * @param operation The operation to be executed, taking no parameters and having no return type.
 * @param exceptionMessage The log message associated with the exception.
 * @param actionOnException An optional action to be executed in case of an exception.
 *                          It takes no parameters and is typically used for cleanup or logging.
 */
fun kompanionSafeOperation(
    operation: () -> Unit,
    actionOnException: ((e:Exception) -> Unit)? = null,
) {
    try {
        operation.invoke()
    } catch (e: Exception) {
        //print stack trace
        e.printStackTrace()
        // Invoke the optional actionOnException (cleanup or additional logging)
        actionOnException?.invoke(e)
    }
}

/**
 * Safely executes an operation by wrapping it in a try-catch block. The operation to be invoked
 * must have a nullable return type.
 *
 * @param operation The operation to be executed, returning a nullable result.
 * @param actionOnException An optional action to be executed in case of an exception.
 *                          It takes an Exception parameter and is typically used for cleanup or logging.
 * @param exceptionMessage The log message associated with the exception.
 * @return The result of the operation or null in case of an exception.
 */
fun <T> kompanionSafeNullableReturnableOperation(
    operation: () -> T?,
    actionOnException: ((e: Exception?) -> Unit)? = null,
): T? {
    return try {
        operation.invoke()
    } catch (e: Exception) {
        // Print stack trace
        e.printStackTrace()

        // Invoke the optional actionOnException with the error message
        actionOnException?.invoke(e)

        // Return null in case of an exception
        null
    }
}

/**
 * Safely executes a suspend operation by wrapping it in a try-catch block.
 *
 * @param operation The suspend operation to be executed.
 * @param actionOnException An optional action to be executed in case of an exception.
 *                          It takes a nullable string parameter representing the error message.
 * @param exceptionMessage The log message associated with the exception.
 */
suspend fun <T> kompanionSafeSuspendOperation(
    operation: suspend () -> T,
    actionOnException: (suspend (exception: Exception?) -> Unit)? = null,
): T? {
    return try {
        operation.invoke()
    } catch (e: Exception) {
        //print stack trace
        e.printStackTrace()

        // Invoke the optional actionOnException with the error message
        actionOnException?.invoke(e)
        null
    }
}

/**
 * Safely executes an operation by wrapping it in a try-catch block. The operation to be invoked
 * must have a nullable return type.
 *
 * @param operation The operation to be executed, returning a nullable result.
 * @param exceptionMessage The log message associated with the exception.
 * @param actionOnException An optional action to be executed in case of an exception.
 *                          It takes a nullable string parameter representing the error message.
 * @return The result of the operation or null in case of an exception.
 */
suspend fun <T> kompanionSafeReturnableSuspendOperation(
    operation: suspend () -> T?,
    actionOnException: ((message: Exception?) -> Unit)? = null,
): T? {
    return try {
        operation.invoke()
    } catch (e: Exception) {
        //print stack trace
        e.printStackTrace()

        // Invoke the optional actionOnException with the error message
        actionOnException?.invoke(e)

        // Return null in case of an exception
        null
    }
}

suspend fun <T> kompanionSafeFlowReturnableOperation(
    operation: suspend () -> T?,
    actionOnException: ((message: Exception?) -> Unit)? = null,
    dispatcher: CoroutineDispatcher
): Flow<T?> = flow {
    try {
        emit(operation.invoke())
    } catch (e: Exception) {
        coroutineContext.ensureActive()

//        if (e is CancellationException) throw e <- this will cancel the flow or use this

        // Print stack trace
        e.printStackTrace()

        // Invoke the optional actionOnException with the error message
        actionOnException?.invoke(e)

        // Emit null in case of an exception
        emit(null)
    }
}.flowOn(dispatcher)


suspend fun <T : Any?> Flow<T>.kompanionSingleFlowOnItemReceivedInScope(
    onStart: () -> Unit = {},
    onItemReceived: suspend (T) -> Unit = { _ -> },
    onError: (Throwable) -> Unit = { _ -> },
    coroutineScope: CoroutineScope,
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
