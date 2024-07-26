package com.parth.crawlappsdemo.utils.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar

inline fun View.snack(
    message: String,
    length: Int = Snackbar.LENGTH_LONG,
    f: Snackbar.() -> Unit = {}
) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun View.action(msg: String, btn: String, listener: (View) -> Unit) {
    Snackbar.make(this, msg, Snackbar.LENGTH_INDEFINITE)
        .setAction(btn) {
            listener(this)
        }
        .show()
}