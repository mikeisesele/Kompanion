package com.michael.kompanion.preferences.security.enums

enum class PBKDF2Algorithm(val algorithm: String) {
    HMAC_SHA1("PBKDF2WithHmacSHA1"),
    HMAC_SHA256("PBKDF2WithHmacSHA256"),
    HMAC_SHA512("PBKDF2WithHmacSHA512")
}
