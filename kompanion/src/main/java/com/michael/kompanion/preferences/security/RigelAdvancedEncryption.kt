package com.michael.kompanion.preferences.security


import android.os.Build
import androidx.annotation.RequiresApi
import com.michael.kompanion.preferences.security.enums.CharacterEncoding
import com.michael.kompanion.preferences.security.enums.EncryptionAlgorithm
import com.michael.kompanion.preferences.security.enums.EncryptionTransformation
import com.michael.kompanion.utils.kompanionSafeNullableReturnableOperation
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


object RigelAdvancedEncryption {

    /**
  * Provides advanced encryption and decryption methods using the specified algorithm and transformation.
  *
  * @param input The string to be encrypted.
  * @param secretKey The secret key used for encryption.
  * @param algorithm The encryption algorithm to be used, defaulting to AES.
  * @param transformation The transformation to be applied, defaulting to AES_CBC_PKCS5Padding.
  * @param charsetName The character encoding to be used, defaulting to UTF_8.
  * @return The encrypted string, or null if the operation fails.
  *
  * @throws RuntimeException If encryption fails.
  */
    @RequiresApi(Build.VERSION_CODES.O)
    fun advancedEncryptionEncrypt(
        input: String,
        secretKey: String,
        algorithm: EncryptionAlgorithm = EncryptionAlgorithm.AES,
        transformation: EncryptionTransformation = EncryptionTransformation.AES_CBC_PKCS5Padding,
        charsetName: CharacterEncoding = CharacterEncoding.UTF_8
    ): String? {
        return kompanionSafeNullableReturnableOperation(
            operation = {
                val cipher = Cipher.getInstance(transformation.transformation)
                val keySpec = SecretKeySpec(
                    secretKey.toByteArray(charset(charsetName.charsetName)),
                    algorithm.algorithm
                )
                cipher.init(Cipher.ENCRYPT_MODE, keySpec)
                val encryptedBytes = cipher.doFinal(input.toByteArray(charset(charsetName.charsetName)))
                Base64.getEncoder().encodeToString(encryptedBytes)
            },
            actionOnException = {
                throw RuntimeException("Encryption failed", it)
            },
        )
    }

    /**
     * Decrypts an encrypted string using the specified algorithm and transformation.
     *
     * @param input The string to be decrypted.
     * @param secretKey The secret key used for decryption.
     * @param algorithm The encryption algorithm to be used, defaulting to AES.
     * @param transformation The transformation to be applied, defaulting to AES_CBC_PKCS5Padding.
     * @param charsetName The character encoding to be used, defaulting to UTF_8.
     * @return The decrypted string, or null if the operation fails.
     *
     * @throws RuntimeException If decryption fails.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun advancedEncryptionDecrypt(
        input: String,
        secretKey: String,
        algorithm: EncryptionAlgorithm = EncryptionAlgorithm.AES,
        transformation: EncryptionTransformation = EncryptionTransformation.AES_CBC_PKCS5Padding,
        charsetName: CharacterEncoding = CharacterEncoding.UTF_8
    ): String? {
        return kompanionSafeNullableReturnableOperation(
            operation = {
                val cipher = Cipher.getInstance(transformation.transformation)
                val keySpec = SecretKeySpec(
                    secretKey.toByteArray(charset(charsetName.charsetName)),
                    algorithm.algorithm
                )
                cipher.init(Cipher.DECRYPT_MODE, keySpec)
                val decodedBytes = Base64.getDecoder().decode(input)
                val decryptedBytes = cipher.doFinal(decodedBytes)
                String(decryptedBytes, charset(charsetName.charsetName))
            },
            actionOnException = {
                throw RuntimeException("Decryption failed", it)
            },
        )
    }

}

