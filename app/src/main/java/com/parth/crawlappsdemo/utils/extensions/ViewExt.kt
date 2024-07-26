package com.parth.crawlappsdemo.utils.extensions

import android.app.Activity
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import com.parth.crawlappsdemo.utils.hideKeyboard

fun View.beGone() {
    this.visibility = View.GONE
}

fun View.beVisible() {
    this.visibility = View.VISIBLE
}

fun AppCompatEditText.setOnEditorActionListener(
    activity: Activity,
    etView: AppCompatEditText? = null
) {
    this.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            etView?.let {
                etView.requestFocus()
                etView.setSelection(etView.text?.length ?: 0)

            } ?: hideKeyboard(activity = activity)
            true
        } else false
    }
}