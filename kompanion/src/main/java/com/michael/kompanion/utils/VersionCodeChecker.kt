package com.michael.kompanion.utils

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */


object KompanionSDKVersionChecker {

    /**
     * Checks if the device's SDK version is lower than Android O (API level 26).
     * @return `true` if the device's SDK version is lower than Android O, `false` otherwise.
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
    fun isOLess() = Build.VERSION.SDK_INT < Build.VERSION_CODES.O

    /**
     * Checks if the device's SDK version is lower than Android P (API level 28).
     * @return `true` if the device's SDK version is lower than Android P, `false` otherwise.
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
    fun isPLess() = Build.VERSION.SDK_INT < Build.VERSION_CODES.P


    /**
     * Checks if the device's SDK version is Android Q (API level 29) or higher.
     * @return `true` if the device's SDK version is Android Q or higher, `false` otherwise.
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    fun isQPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    /**
     * Checks if the device's SDK version is Android R (API level 30) or higher.
     * @return `true` if the device's SDK version is Android R or higher, `false` otherwise.
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
    fun isRPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

    /**
     * Checks if the device's SDK version is Android S (API level 31) or higher.
     * @return `true` if the device's SDK version is Android S or higher, `false` otherwise.
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
    fun isSPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    /**
     * Checks if the device's SDK version is Android S (Version 2) (API level 32) or higher.
     * @return `true` if the device's SDK version is Android S (Version 2) or higher, `false` otherwise.
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S_V2)
    fun isSV2Plus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2

    /**
     * Checks if the device's SDK version is Android Tiramisu (API level 33) or higher.
     * @return `true` if the device's SDK version is Android Tiramisu or higher, `false` otherwise.
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
    fun isTPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    /**
     * Checks if the device's SDK version is Android Upside Down Cake (API level 34) or higher.
     * @return `true` if the device's SDK version is Android Upside Down Cake or higher, `false` otherwise.
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun isUPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE
}
