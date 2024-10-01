package com.michael.kompanion.utils

import android.content.Context
import android.widget.Toast

fun kompanionDisplayToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
