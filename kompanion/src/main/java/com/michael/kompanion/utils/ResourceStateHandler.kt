package com.michael.kompanion.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */


/**
 * A sealed class representing the resource state in a coroutine operation.
 * It can represent various states such as loading, success, error, or failure.
 *
 * @param T The type of data associated with the resource.
 */
sealed class KompanionResource<out T> {

    /**
     * Represents a loading state.
     */
    data object Loading : KompanionResource<Nothing>()

    /**
     * Represents a successful operation with associated data.
     *
     * @param data The data returned from the successful operation.
     */
    data class Success<T>(val data: T) : KompanionResource<T>()

    /**
     * Represents an error state with an error message.
     *
     * @param message The error message describing the issue.
     */
    data class Error(val message: String) : KompanionResource<Nothing>()

    /**
     * Represents a failure state with an associated exception.
     *
     * @param exception The exception that occurred during the operation.
     */
    data class Failure(val exception: Any) : KompanionResource<Nothing>()
}



/**
 * A suspend function that handles different states of a Resource using a Flow if using a resource from the repository.
 *
 * @param resource A Flow emitting Resource states (Success, Loading, Failure, ServerError).
 * @param onSuccess A lambda function called on Resource.Success with the success data.
 * @param onFailure A lambda function called on Resource.Failure.
 * @param onLoading A lambda function called on Resource.Loading.
 * @param onError A lambda function called on Resource.ServerError.
 */
suspend inline fun <reified T> kompanionHandleFlowResourceStates(
    resource: Flow<KompanionResource<T>>?,
    crossinline onSuccess: (T) -> Unit = {},
    crossinline onFailure: (Exception) -> Unit = {},
    crossinline onLoading: () -> Unit = {},
    crossinline onError: (String) -> Unit = {}
) {
    resource?.distinctUntilChanged()?.collect {
        when (it) {
            is KompanionResource.Success -> onSuccess(it.data)
            is KompanionResource.Loading -> onLoading()
            is KompanionResource.Failure -> {
                val exception = it.exception
                if (exception is Exception) {
                    onFailure(exception)
                } else {
                    onFailure(Exception("Something went wrong"))
                }
            }
            is KompanionResource.Error -> onError(it.message)
        }
    }
}

/**
 * A suspend function that handles different states of a Resource if using a resource from the repository.
 *
 * @param resource A Resource of states (Success, Loading, Failure, ServerError).
 * @param onSuccess A lambda function called on Resource.Success with the success data.
 * @param onFailure A lambda function called on Resource.Failure.
 * @param onLoading A lambda function called on Resource.Loading.
 * @param onError A lambda function called on Resource.ServerError.
 */

suspend inline fun <reified T> kompanionHandleResourceStates(
    resource: KompanionResource<T>,
    crossinline onSuccess: (T) -> Unit = {},
    crossinline onFailure: (Exception) -> Unit = {},
    crossinline onLoading: () -> Unit = {},
    crossinline onError: (String) -> Unit = {}
) {
    when (resource) {
        is KompanionResource.Success -> onSuccess(resource.data)
        is KompanionResource.Loading -> onLoading()
        is KompanionResource.Failure -> onFailure(resource.exception as Exception)
        is KompanionResource.Error -> onError(resource.message)
    }
}
/**
 * Extension function to wrap any Flow into a success state resource.
 * Converts each emitted value of the flow into a KompanionResource.Success object.
 *
 * @return Flow emitting success resources of type T.
 */
fun <T> Flow<T>.kompanionAsSuccessFlowResource(): Flow<KompanionResource<T>> =
    map { KompanionResource.Success(it) }.distinctUntilChanged()

/**
 * Extension function to unwrap a Flow of KompanionResource to extract the success data.
 * Filters out only the Success states and maps them to their data values.
 *
 * @return Flow emitting the data from KompanionResource.Success.
 */
fun <T> Flow<KompanionResource<T>>.kompanionUnwrap(): Flow<T> =
    filterIsInstance<KompanionResource.Success<T>>().map { it.data }

/**
 * Extension function to convert an object of type T into a Flow emitting a success state resource.
 *
 * @return Flow emitting a KompanionResource.Success object containing this instance of type T.
 */
fun <T> T.kompanionToSuccessFlowResource(): Flow<KompanionResource<T>> =
    flow { emit(KompanionResource.Success(this@kompanionToSuccessFlowResource)) }
        .distinctUntilChanged()

/**
 * Extension function to convert an object of type T into a Flow of that object.
 *
 * @return Flow emitting this instance of type T.
 */
fun <T> T.kompanionToFlow(): Flow<T> = flowOf(this)
    .distinctUntilChanged()

/**
 * Extension function to convert an object of type T into a Flow emitting an error state resource.
 * The resource will carry an error message.
 *
 * @param message The error message to emit with the error resource.
 * @return Flow emitting a KompanionResource.Error object containing the error message.
 */
fun <T> T.kompanionToErrorFlowResource(message: String): Flow<KompanionResource<T>> =
    flow { emit(KompanionResource.Error(message)) }
        .distinctUntilChanged()

/**
 * Extension function to convert an object of type T into a success state resource.
 *
 * @return KompanionResource.Success object containing this instance of type T.
 */
fun <T> T.kompanionAsSuccessResource(): KompanionResource<T> = KompanionResource.Success(this)

/**
 * Extension function to convert an object of type T into an error state resource.
 * The resource will carry an error message.
 *
 * @param message The error message to emit with the error resource.
 * @return KompanionResource.Error object containing the error message.
 */
fun <T> T.kompanionAsErrorResource(message: String): KompanionResource<T> = KompanionResource.Error(message)

/**
 * Extension function to convert a Flow of type T into a Flow emitting error state resources.
 * The resource will carry an error message.
 *
 * @param message The error message to emit with the error resource.
 * @return Flow emitting KompanionResource.Error objects containing the error message.
 */
fun <T> Flow<T>.kompanionAsErrorFlowResource(message: String): Flow<KompanionResource<T>> =
    map { KompanionResource.Error(message) }
        .distinctUntilChanged()