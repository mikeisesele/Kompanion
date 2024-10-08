package com.michael.kompanion.model


/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */


/*
    Useful for when api calls are successful but a failure message is returned from the server which is not
    form a 400+ error or an exception. with this class its possible to return the data as null and still get the
    message to the UI.
 */
data class KompanionRepositoryResponseModel<T>(
    val data: T? = null,
    val apiErrorMessage: String? = null
)