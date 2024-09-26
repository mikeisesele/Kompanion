package com.michael.kompanion.model

data class RepositoryResponseModel<T>(
    val data: T? = null,
    val apiErrorMessage: String? = null
)