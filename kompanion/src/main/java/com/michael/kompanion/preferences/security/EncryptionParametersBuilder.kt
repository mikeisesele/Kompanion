package com.michael.kompanion.preferences.security


import com.michael.kompanion.preferences.security.enums.EncryptionAlgorithm
import com.michael.kompanion.preferences.security.enums.EncryptionTransformation
import com.michael.kompanion.preferences.security.enums.PBKDF2Algorithm

/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */



/**
 * A builder class for creating encryption parameters used in cryptographic operations.
 * Allows customization of parameters such as salt size, IV size, iteration count, and key length.
 *
 * @constructor Initializes default values for the encryption parameters.
 */
class EncryptionParametersBuilder {

    private var saltSize: Int = 16
    private var ivSize: Int = 16
    private var iterationCount: Int = 65536
    private var keyLength: Int = 256
    private var transformation: EncryptionTransformation = EncryptionTransformation.AES_CBC_PKCS5Padding
    private var algorithm: EncryptionAlgorithm = EncryptionAlgorithm.AES
    private var keyGenerationAlgorithm: PBKDF2Algorithm = PBKDF2Algorithm.HMAC_SHA1

    /**
     * Sets the size of the salt.
     *
     * @param size The size of the salt in bytes.
     * @return The current instance of the builder.
     */
    fun setSaltSize(size: Int): EncryptionParametersBuilder {
        this.saltSize = size
        return this
    }

    /**
     * Sets the size of the IV (Initialization Vector).
     *
     * @param size The size of the IV in bytes.
     * @return The current instance of the builder.
     */
    fun setIVSize(size: Int): EncryptionParametersBuilder {
        this.ivSize = size
        return this
    }

    /**
     * Sets the iteration count for the PBKDF2 algorithm.
     *
     * @param count The number of iterations.
     * @return The current instance of the builder.
     */
    fun setIterationCount(count: Int): EncryptionParametersBuilder {
        this.iterationCount = count
        return this
    }

    /**
     * Sets the key length for the encryption.
     *
     * @param length The length of the key in bits.
     * @return The current instance of the builder.
     */
    fun setKeyLength(length: Int): EncryptionParametersBuilder {
        this.keyLength = length
        return this
    }

    /**
     * Sets the transformation type for the encryption.
     *
     * @param transformation The encryption transformation type.
     * @return The current instance of the builder.
     */
    fun setTransformation(transformation: EncryptionTransformation): EncryptionParametersBuilder {
        this.transformation = transformation
        return this
    }

    /**
     * Sets the encryption algorithm.
     *
     * @param algorithm The encryption algorithm to use.
     * @return The current instance of the builder.
     */
    fun setAlgorithm(algorithm: EncryptionAlgorithm): EncryptionParametersBuilder {
        this.algorithm = algorithm
        return this
    }

    /**
     * Sets the key generation algorithm for PBKDF2.
     *
     * @param keyGenerationAlgorithm The algorithm for key generation.
     * @return The current instance of the builder.
     */
    fun setKeyGenerationAlgorithm(keyGenerationAlgorithm: PBKDF2Algorithm): EncryptionParametersBuilder {
        this.keyGenerationAlgorithm = keyGenerationAlgorithm
        return this
    }

    /**
     * Builds and returns an instance of EncryptionParameters with the configured values.
     *
     * @return A new instance of EncryptionParameters.
     */
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
 *
 * @property saltSize The size of the salt in bytes.
 * @property ivSize The size of the IV in bytes.
 * @property iterationCount The number of iterations for PBKDF2.
 * @property keyLength The length of the encryption key in bits.
 * @property transformationType The transformation type for encryption.
 * @property algorithm The encryption algorithm to use.
 * @property keyGenerationAlgorithm The algorithm for key generation.
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
