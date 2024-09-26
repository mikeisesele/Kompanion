package com.michael.kompanion.utils

import android.app.Dialog
import android.view.Gravity
import android.view.WindowManager

fun Dialog.setFullScreen() {
    window?.apply {
        setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        attributes.gravity = Gravity.CENTER
    }
}
