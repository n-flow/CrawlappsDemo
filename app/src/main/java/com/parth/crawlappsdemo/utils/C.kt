package com.parth.crawlappsdemo.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager

fun openUrl(activity: Activity, url: String) {
    val i = Intent(Intent.ACTION_VIEW)
    i.data = Uri.parse(url)
    activity.startActivity(i)
}

fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager?
    imm?.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
}

fun showKeyboard(v: View, activity: Activity) {
    v.requestFocus()
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
}

fun logger(tag: String, message: String) {
    val maxLogSize = 4000
    for (i in 0..message.length / maxLogSize) {
        val start = i * maxLogSize
        var end = (i + 1) * maxLogSize
        end = if (end > message.length) message.length else end
        Log.e(tag, message.substring(start, end))
    }
}
