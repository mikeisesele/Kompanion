package com.michael.kompanion.preferences.security

import android.os.Build
import androidx.annotation.RequiresApi
import com.michael.kompanion.utils.kompanionSafeNullableReturnableOperation
import java.security.SecureRandom
import java.security.spec.KeySpec
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


object RigelPasswordBasedKeyDerivationFunction {

    /**
     * Encrypts a string using a password and specified encryption parameters.
     * Generates a random salt and IV for the encryption process.
     *
     * @param input The string to be encrypted.
     * @param password The password used for key derivation.
     * @param params The encryption parameters, including salt size, IV size, and transformation type.
     * @return The encrypted string in Base64 format, or null if the operation fails.
     *
     * @throws RuntimeException If encryption fails.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun encrypt(input: String, password: String, params: EncryptionParameters): String? {
        return kompanionSafeNullableReturnableOperation(
            operation = {
                val saltBytes = generateSalt(params.saltSize)
                val ivBytes = generateIV(params.ivSize)
                val keySpec = generateKey(password, saltBytes, params)
                val cipher = Cipher.getInstance(params.transformationType.transformation)
                val secretKey = SecretKeySpec(keySpec.encoded, params.algorithm.algorithm)
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(ivBytes))
                val encryptedBytes = cipher.doFinal(input.toByteArray())
                val encryptedData = ByteArray(saltBytes.size + ivBytes.size + encryptedBytes.size)
                System.arraycopy(saltBytes, 0, encryptedData, 0, saltBytes.size)
                System.arraycopy(ivBytes, 0, encryptedData, saltBytes.size, ivBytes.size)
                System.arraycopy(
                    encryptedBytes,
                    0,
                    encryptedData,
                    saltBytes.size + ivBytes.size,
                    encryptedBytes.size
                )
                Base64.getEncoder().encodeToString(encryptedData)
            },
            actionOnException = {
                it?.printStackTrace()
                throw RuntimeException("Encryption failed", it)
            }
        )
    }

    /**
     * Decrypts a string using a password and specified encryption parameters.
     * Extracts the salt and IV from the encrypted data to derive the key.
     *
     * @param encryptedData The Base64-encoded string to be decrypted.
     * @param password The password used for key derivation.
     * @param params The encryption parameters, including salt size, IV size, and transformation type.
     * @return The decrypted string, or null if the operation fails.
     *
     * @throws RuntimeException If decryption fails.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun decrypt(encryptedData: String, password: String, params: EncryptionParameters): String? {
        return kompanionSafeNullableReturnableOperation(
            operation = {
                val encryptedBytes = Base64.getDecoder().decode(encryptedData)
                val saltBytes = encryptedBytes.copyOfRange(0, params.saltSize)
                val ivBytes =
                    encryptedBytes.copyOfRange(params.saltSize, params.saltSize + params.ivSize)
                val encryptedContent =
                    encryptedBytes.copyOfRange(params.saltSize + params.ivSize, encryptedBytes.size)
                val keySpec = generateKey(
                    password,
                    saltBytes,
                    params,
                )
                val cipher = Cipher.getInstance(params.transformationType.transformation)
                val secretKey = SecretKeySpec(keySpec.encoded, params.algorithm.algorithm)
                cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(ivBytes))
                val decryptedBytes = cipher.doFinal(encryptedContent)
                String(decryptedBytes)
            },
            actionOnException = {
                it?.printStackTrace()
                throw RuntimeException("Decryption failed", it)
            }
        )
    }

    /**
     * Generates a random salt of the specified size.
     *
     * @param saltSize The size of the salt in bytes.
     * @return A byte array containing the generated salt.
     */
    private fun generateSalt(saltSize: Int): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(saltSize)
        random.nextBytes(salt)
        return salt
    }


    /**
     * Generates a random initialization vector (IV) of the specified size.
     *
     * @param ivSize The size of the IV in bytes.
     * @return A byte array containing the generated IV.
     */
    private fun generateIV(ivSize: Int): ByteArray {
        val random = SecureRandom()
        val iv = ByteArray(ivSize)
        random.nextBytes(iv)
        return iv
    }

    /**
     * Generates a secret key based on the provided password, salt, and encryption parameters.
     *
     * @param password The password used for key derivation.
     * @param salt The salt used in the key generation process.
     * @param params The encryption parameters for key generation.
     * @return A SecretKey derived from the password and salt.
     */
    private fun generateKey(
        password: String,
        salt: ByteArray,
        params: EncryptionParameters
    ): SecretKey {
        val keySpec: KeySpec =
            PBEKeySpec(password.toCharArray(), salt, params.iterationCount, params.keyLength)
        val factory = SecretKeyFactory.getInstance(params.keyGenerationAlgorithm.algorithm)
        return SecretKeySpec(factory.generateSecret(keySpec).encoded, params.algorithm.algorithm)
    }

}