package com.michael.kompanion.preferences

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.michael.kompanion.preferences.security.EncryptionParameters
import com.michael.kompanion.preferences.security.RigelAdvancedEncryption
import com.michael.kompanion.preferences.security.enums.EncryptionType
import com.michael.kompanion.preferences.security.RigelPasswordBasedKeyDerivationFunction

@RequiresApi(Build.VERSION_CODES.O)
fun SharedPreferences.putEncryptedString(
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

@RequiresApi(Build.VERSION_CODES.O)
fun SharedPreferences.getDecryptedString(
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
