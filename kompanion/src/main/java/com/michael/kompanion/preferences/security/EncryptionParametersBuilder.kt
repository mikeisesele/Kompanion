package com.michael.kompanion.preferences.security


import com.michael.kompanion.preferences.security.enums.EncryptionAlgorithm
import com.michael.kompanion.preferences.security.enums.EncryptionTransformation
import com.michael.kompanion.preferences.security.enums.PBKDF2Algorithm


/**
 * A builder class for creating encryption parameters used in cryptographic operations.
 * Allows customization of parameters such as salt size, IV size, iteration count, and key length.
 */

class EncryptionParametersBuilder {

    private var saltSize: Int = 16
    private var ivSize: Int = 16
    private var iterationCount: Int = 65536
    private var keyLength: Int = 256
    private var transformation: EncryptionTransformation = EncryptionTransformation.AES_CBC_PKCS5Padding
    private var algorithm: EncryptionAlgorithm = EncryptionAlgorithm.AES
    private var keyGenerationAlgorithm: PBKDF2Algorithm = PBKDF2Algorithm.HMAC_SHA1

    fun setSaltSize(size: Int): EncryptionParametersBuilder {
        this.saltSize = size
        return this
    }

    fun setIVSize(size: Int): EncryptionParametersBuilder {
        this.ivSize = size
        return this
    }

    fun setIterationCount(count: Int): EncryptionParametersBuilder {
        this.iterationCount = count
        return this
    }

    fun setKeyLength(length: Int): EncryptionParametersBuilder {
        this.keyLength = length
        return this
    }

    fun setTransformation(transformation: EncryptionTransformation): EncryptionParametersBuilder {
        this.transformation = transformation
        return this
    }

    fun setAlgorithm(algorithm: EncryptionAlgorithm): EncryptionParametersBuilder {
        this.algorithm = algorithm
        return this
    }

    fun setKeyGenerationAlgorithm(keyGenerationAlgorithm: PBKDF2Algorithm): EncryptionParametersBuilder {
        this.keyGenerationAlgorithm = keyGenerationAlgorithm
        return this
    }

    fun build(): EncryptionParameters {
        return EncryptionParameters(
            saltSize,
            ivSize,
            iterationCount,
            keyLength,
            transformation,
            algorithm,
            keyGenerationAlgorithm
        )
    }
}


/**
 * Provides utilities for password-based key derivation using the PBKDF2 algorithm.
 *
 * To use this class, you need to provide an instance of `EncryptionParameters`.
 * You can create an instance of `EncryptionParameters` using the `EncryptionParametersBuilder` class.
 */
data class EncryptionParameters(
    val saltSize: Int,
    val ivSize: Int,
    val iterationCount: Int,
    val keyLength: Int,
    val transformationType: EncryptionTransformation,
    val algorithm: EncryptionAlgorithm,
    val keyGenerationAlgorithm: PBKDF2Algorithm
) {
    companion object {
        val defaultEncryptionParameters = EncryptionParametersBuilder().build()
    }
}

