package com.parth.crawlappsdemo.dialogs.common

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import androidx.databinding.DataBindingUtil
import com.parth.crawlappsdemo.R
import com.parth.crawlappsdemo.databinding.DialogCommonMsgBinding

class CommonMsgDialog(
    private val activity: Activity,
    private val title: String,
    private val msg: String,
    private val btn: String = "Cancel",
    private val btn1: String = "Ok",
    private val click: (Int) -> Unit = {}
) : AppCompatDialog(
    activity, R.style.AppTheme_ToastDialog
) {

    companion object {
        const val NEGATIVE_BTN_CLICK = 0
        const val POSITIVE_BTN_CLICK = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)

        val binding: DialogCommonMsgBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity), R.layout.dialog_common_msg, null, false
        )
        setContentView(binding.root)
        binding.title = title
        binding.msg = msg
        binding.btn = btn
        binding.btn1 = btn1

        binding.btnCancel.setOnClickListener {
            click(NEGATIVE_BTN_CLICK)
            finishDialog()
        }

        binding.btnOk.setOnClickListener {
            click(POSITIVE_BTN_CLICK)
            finishDialog()
        }
    }

    private fun finishDialog() {
        if (isShowing) {
            dismiss()
        }
    }

    fun openDialog() {
        if (!activity.isFinishing && !isShowing && window != null) {
            show()
        }
    }
}