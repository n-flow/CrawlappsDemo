package com.parth.crawlappsdemo.utils.extensions

import android.graphics.Color
import android.os.Build

fun getDeviceManufacturer(): String? {
    return Build.MANUFACTURER
}

fun getDeviceModel(): String? {
    return Build.MODEL
}

fun getDeviceOS(): String? {
    return Build.VERSION.RELEASE
}

fun Int.getContrastColor(): Int {
    val y = (299 * Color.red(this) + 587 * Color.green(this) + 114 * Color.blue(this)) / 1000
    return if (y >= 149 && this != Color.BLACK) Color.WHITE else Color.WHITE
}
