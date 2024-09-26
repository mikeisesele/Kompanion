package com.michael.kompanion.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map


/**
 * A suspend function that handles different states of a Resource using a Flow if using a resource from the repository.
 *
 * @param resource A Flow emitting Resource states (Success, Loading, Failure, ServerError).
 * @param onSuccess A lambda function called on Resource.Success with the success data.
 * @param onFailure A lambda function called on Resource.Failure.
 * @param onLoading A lambda function called on Resource.Loading.
 * @param onError A lambda function called on Resource.ServerError.
 */
suspend inline fun <reified T> handleFlowResourceStates(
    resource: Flow<Resource<T>>?,
    crossinline onSuccess: (T) -> Unit = {},
    crossinline onFailure: (Exception) -> Unit = {},
    crossinline onLoading: () -> Unit = {},
    crossinline onError: (String) -> Unit = {}
) {
    resource?.distinctUntilChanged()?.collect {
        when (it) {
            is Resource.Success -> onSuccess(it.data)
            is Resource.Loading -> onLoading()
            is Resource.Failure -> {
                val exception = it.exception
                if (exception is Exception) {
                    onFailure(exception)
                } else {
                    onFailure(Exception())
                }
            }
            is Resource.Error -> onError(it.message)
        }
    }
}



suspend inline fun <reified T> handleResourceStates(
    resource: Resource<T>?,
    crossinline onSuccess: (T) -> Unit = {},
    crossinline onFailure: (Exception) -> Unit = {},
    crossinline onLoading: () -> Unit = {},
    crossinline onError: (String) -> Unit = {}
) {
    when (resource) {
        is Resource.Success -> onSuccess(resource.data)
        is Resource.Loading -> onLoading()
        is Resource.Failure -> onFailure(resource.exception as Exception)
        is Resource.Error -> onError(resource.message)
        null -> {}
    }
}

/*
    call this on any flow to wrap the result into a resource success
    see usage in any repositoryImpl
 */
fun <T> Flow<T>.asSuccess(): Flow<Resource<T>> =
    map { Resource.Success(it) }.distinctUntilChanged()

fun <T> Flow<Resource<T>>.unwrap(): Flow<T> =
    filterIsInstance<Resource.Success<T>>().map { it.data }


fun <T> T.asSuccessFlow(): Flow<Resource<T>> =
    flow { emit(Resource.Success(this@asSuccessFlow)) }
        .distinctUntilChanged()

fun <T> T.asFlow(): Flow<T> = flowOf(this)
    .distinctUntilChanged()

fun <T> T.asErrorFlow(message: String): Flow<Resource<T>> =
    flow { emit(Resource.Error(message)) }
        .distinctUntilChanged()

fun <T> T.asSuccess(): Resource<T> = Resource.Success(this)

fun <T> T.asError(message: String): Resource<T> = Resource.Error(message)

fun <T> Flow<T>.asError(message: String): Flow<Resource<T>> =
    map { Resource.Error(message) }
        .distinctUntilChanged()




sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    data class Failure(val exception: Any) : Resource<Nothing>()
}