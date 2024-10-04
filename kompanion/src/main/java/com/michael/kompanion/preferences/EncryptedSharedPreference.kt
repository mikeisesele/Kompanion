package com.michael.kompanion.preferences

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.michael.kompanion.preferences.security.EncryptionParameters
import com.michael.kompanion.preferences.security.RigelAdvancedEncryption
import com.michael.kompanion.preferences.security.RigelPasswordBasedKeyDerivationFunction
import com.michael.kompanion.preferences.security.enums.EncryptionType


/**
 * Encrypts and stores a string value in SharedPreferences using the specified encryption type.
 * - key: The key under which the encrypted string will be stored.
 * - value: The string value to encrypt and store.
 * - secretKey: The secret key used for encryption.
 * - encryptionType: The type of encryption to be applied.
 * - encryptionParameters: Optional parameters for encryption, defaulting to standard parameters.
 *
 * @param key The key for storing the encrypted string.
 * @param value The string value to encrypt.
 * @param secretKey The secret key for encryption.
 * @param encryptionType The encryption method to use.
 * @param encryptionParameters Optional encryption parameters.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun SharedPreferences.kompanionPutEncryptedString(
    key: String,
    value: String,
    secretKey: String,
    encryptionType: EncryptionType,
    encryptionParameters: EncryptionParameters = EncryptionParameters.defaultEncryptionParameters
) {
    val encryptedValue = when(encryptionType) {
        EncryptionType.ADVANCED_ENCRYPTION -> RigelAdvancedEncryption.advancedEncryptionEncrypt(
            value,
            secretKey
        )
        EncryptionType.PASSWORD_BASED_KEY_DERIVATION_ENCRYPTION -> RigelPasswordBasedKeyDerivationFunction.encrypt(
            value,
            secretKey,
            encryptionParameters
        )
    }

    edit().putString(key, encryptedValue).apply()
}

/**
 * Retrieves and decrypts a string value from SharedPreferences using the specified decryption method.
 * - key: The key under which the encrypted string is stored.
 * - secretKey: The secret key used for decryption.
 * - encryptionType: The type of encryption used for the stored string.
 * - encryptionParameters: Optional parameters for decryption, defaulting to standard parameters.
 *
 * @param key The key for the encrypted string.
 * @param secretKey The secret key for decryption.
 * @param encryptionType The encryption method used for the stored string.
 * @param encryptionParameters Optional decryption parameters.
 * @return The decrypted string, or null if the key does not exist or decryption fails.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun SharedPreferences.kompanionGetDecryptedString(
    key: String,
    secretKey: String,
    encryptionType: EncryptionType,
    encryptionParameters: EncryptionParameters = EncryptionParameters.defaultEncryptionParameters
): String? {
    val encryptedValue = getString(key, null)
    return encryptedValue?.let {
        val decryptedValue = when(encryptionType) {
            EncryptionType.ADVANCED_ENCRYPTION -> RigelAdvancedEncryption.advancedEncryptionDecrypt(
                it,
                secretKey
            )
            EncryptionType.PASSWORD_BASED_KEY_DERIVATION_ENCRYPTION -> RigelPasswordBasedKeyDerivationFunction.decrypt(
                it,
                secretKey,
                encryptionParameters
            )
        }
        decryptedValue
    }
}
