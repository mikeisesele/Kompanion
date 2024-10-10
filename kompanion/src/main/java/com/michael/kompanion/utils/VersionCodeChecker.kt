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
     * @return `true` if the device's SDK version is lower than Android O (26), `false` otherwise.
     *
     * Example usage:
     * ```Kt
     * if (KompanionSDKVersionChecker.isApi26OLess()) {
     *     // Code for SDK versions lower than 26
     * }
     * ```
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
    fun isApi26OLess() = Build.VERSION.SDK_INT < Build.VERSION_CODES.O

    /**
     * Checks if the device's SDK version is lower than Android P (API level 28).
     * @return `true` if the device's SDK version is lower than Android P (28), `false` otherwise.
     *
     * Example usage:
     * ```Kt
     * if (KompanionSDKVersionChecker.isApi28PLess()) {
     *     // Code for SDK versions lower than 28
     * }
     * ```
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
    fun isApi28PLess() = Build.VERSION.SDK_INT < Build.VERSION_CODES.P

    /**
     * Checks if the device's SDK version is Android Q (API level 29) or higher.
     * @return `true` if the device's SDK version is Android Q (29) or higher, `false` otherwise.
     *
     * Example usage:
     * ```Kt
     * if (KompanionSDKVersionChecker.isApi29QPlus()) {
     *     // Code for SDK versions 29 or higher
     * }
     * ```
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    fun isApi29QPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    /**
     * Checks if the device's SDK version is Android R (API level 30) or higher.
     * @return `true` if the device's SDK version is Android R (30) or higher, `false` otherwise.
     *
     * Example usage:
     * ```Kt
     * if (KompanionSDKVersionChecker.isApi30RPlus()) {
     *     // Code for SDK versions 30 or higher
     * }
     * ```
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
    fun isApi30RPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

    /**
     * Checks if the device's SDK version is Android S (API level 31) or higher.
     * @return `true` if the device's SDK version is Android S (31) or higher, `false` otherwise.
     *
     * Example usage:
     * ```Kt
     * if (KompanionSDKVersionChecker.isApi31SPlus()) {
     *     // Code for SDK versions 31 or higher
     * }
     * ```
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
    fun isApi31SPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    /**
     * Checks if the device's SDK version is Android S (Version 2) (API level 32) or higher.
     * @return `true` if the device's SDK version is Android S (Version 2) (32) or higher, `false` otherwise.
     *
     * Example usage:
     * ```Kt
     * if (KompanionSDKVersionChecker.isApi32S_V2Plus()) {
     *     // Code for SDK versions 32 or higher
     * }
     * ```
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S_V2)
    fun isApi32S_V2Plus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2

    /**
     * Checks if the device's SDK version is Android Tiramisu (API level 33) or higher.
     * @return `true` if the device's SDK version is Android Tiramisu (33) or higher, `false` otherwise.
     *
     * Example usage:
     * ```Kt
     * if (KompanionSDKVersionChecker.isApi33TPlus()) {
     *     // Code for SDK versions 33 or higher
     * }
     * ```
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
    fun isApi33TPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    /**
     * Checks if the device's SDK version is Android Upside Down Cake (API level 34) or higher.
     * @return `true` if the device's SDK version is Android Upside Down Cake (34) or higher, `false` otherwise.
     *
     * Example usage:
     * ```Kt
     * if (KompanionSDKVersionChecker.isApi34UPlus()) {
     *     // Code for SDK versions 34 or higher
     * }
     * ```
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun isApi34UPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE
}
