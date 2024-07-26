package com.parth.crawlappsdemo.utils.extensions

import android.content.SharedPreferences
import androidx.core.content.edit

internal var SharedPreferences.email: String
    set(value) {
        edit { putString("email", value) }
    }
    get() {
        return getString("email", "") ?: ""
    }

fun SharedPreferences.clear() {
    val editor = this.edit()
    editor.clear()
    editor.apply()
}