package com.michael.kompanion.preferences.security


import android.os.Build
import androidx.annotation.RequiresApi
import com.michael.kompanion.preferences.security.enums.CharacterEncoding
import com.michael.kompanion.preferences.security.enums.EncryptionAlgorithm
import com.michael.kompanion.preferences.security.enums.EncryptionTransformation
import com.michael.kompanion.utils.safeNullableReturnableOperation
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


object RigelAdvancedEncryption {

    @RequiresApi(Build.VERSION_CODES.O)
    fun advancedEncryptionEncrypt(
        input: String,
        secretKey: String,
        algorithm: EncryptionAlgorithm = EncryptionAlgorithm.AES,
        transformation: EncryptionTransformation = EncryptionTransformation.AES_CBC_PKCS5Padding,
        charsetName: CharacterEncoding = CharacterEncoding.UTF_8
    ): String? {
        return safeNullableReturnableOperation(
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
            exceptionMessage = "advancedEncryptionEncrypt failed"
        )

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun advancedEncryptionDecrypt(
        input: String,
        secretKey: String,
        algorithm: EncryptionAlgorithm = EncryptionAlgorithm.AES,
        transformation: EncryptionTransformation = EncryptionTransformation.AES_CBC_PKCS5Padding,
        charsetName: CharacterEncoding = CharacterEncoding.UTF_8
    ): String? {
        return safeNullableReturnableOperation(
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
            exceptionMessage = "advancedEncryptionDecrypt failed"
        )
    }
}

