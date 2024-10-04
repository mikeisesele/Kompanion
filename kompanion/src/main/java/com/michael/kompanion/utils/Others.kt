package com.michael.kompanion.utils

import com.michael.easylog.logE
import com.michael.easylog.logInline
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
        e.printStackTrace().logE("kompanionSafeOperation")
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
        e.printStackTrace().logE("kompanionSafeNullableReturnableOperation")

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
 */
suspend fun <T> kompanionSafeSuspendOperation(
    operation: suspend () -> T,
    actionOnException: (suspend (exception: Exception?) -> Unit)? = null,
): T? {
    return try {
        operation.invoke()
    } catch (e: Exception) {
        //print stack trace
        e.printStackTrace().logE("kompanionSafeSuspendOperation")

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
        e.printStackTrace().logE("kompanionSafeReturnableSuspendOperation")

        // Invoke the optional actionOnException with the error message
        actionOnException?.invoke(e)

        // Return null in case of an exception
        null
    }
}/**
 * Safely executes a suspend operation within a Flow by wrapping it in a try-catch block.
 *
 * @param operation The suspend operation to be executed, returning a nullable result.
 * @param actionOnException An optional action to be executed in case of an exception.
 *                          It takes an Exception parameter and is typically used for cleanup or logging.
 * @param dispatcher The CoroutineDispatcher that the flow will operate on.
 * @return A Flow emitting the result of the operation or null in case of an exception.
 */
suspend fun <T> kompanionSafeFlowReturnableOperation(
    operation: suspend () -> T?,
    actionOnException: ((message: Exception?) -> Unit)? = null,
    dispatcher: CoroutineDispatcher
): Flow<T?> = flow {
        try {
            emit(operation.invoke())
        } catch (e: Exception) {
            coroutineContext.ensureActive()

            // Print stack trace
            e.printStackTrace().logE("kompanionSafeFlowReturnableOperation")

            // Invoke the optional actionOnException with the error message
            actionOnException?.invoke(e)

            // Emit null in case of an exception
            emit(null)
        }
    }.flowOn(dispatcher)

/**
 * Handles a Flow operation, collecting the first emitted item and processing it in the provided CoroutineScope.
 *
 * @param onStart Lambda function invoked before starting the Flow collection.
 * @param onItemReceived Lambda function invoked when the first item is received from the Flow.
 *                       The item is provided as an argument to the lambda.
 * @param onError Lambda function invoked when an error occurs during Flow collection.
 *                The Throwable is provided as an argument to the lambda.
 * @param coroutineScope The CoroutineScope where the onItemReceived lambda is launched.
 */
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
