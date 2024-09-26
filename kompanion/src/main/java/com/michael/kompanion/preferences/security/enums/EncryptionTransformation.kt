package com.michael.kompanion.preferences.security.enums

enum class EncryptionTransformation(val transformation: String) {
    AES_CBC_PKCS5Padding("AES/CBC/PKCS5Padding"),
    AES_ECB_PKCS5Padding("AES/ECB/PKCS5Padding"),
    DES_CBC_PKCS5Padding("DES/CBC/PKCS5Padding"),
    RSA_ECB_OAEPPadding("RSA/ECB/OAEPPadding")
}