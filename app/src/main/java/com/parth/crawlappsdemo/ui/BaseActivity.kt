package com.parth.crawlappsdemo.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.parth.crawlappsdemo.R
import com.parth.crawlappsdemo.databinding.DailogProgrssBarBinding
import com.parth.crawlappsdemo.utils.extensions.beGone
import com.parth.crawlappsdemo.utils.extensions.beVisible
import javax.inject.Inject

abstract class BaseActivity<T : ViewDataBinding>(
    @LayoutRes private val contentLayoutId: Int,
) : AppCompatActivity() {

    companion object {
        private const val TAG = "BaseActivity"
    }

    private var progressBarDialog: DailogProgrssBarBinding? = null

    @Inject
    lateinit var preferences: SharedPreferences

    protected val binding: T by lazy(LazyThreadSafetyMode.NONE) {
        DataBindingUtil.setContentView(this, contentLayoutId)
    }

    protected val activity: BaseActivity<*> by lazy(LazyThreadSafetyMode.NONE) {
        this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addProgressBar()
    }

    private fun addProgressBar() {
        if (binding.root is ViewGroup) {
            progressBarDialog = DataBindingUtil.inflate(layoutInflater, R.layout.dailog_progrss_bar, null, false)
            progressBarDialog?.root?.beGone()

            val param = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            (binding.root as ViewGroup).addView(progressBarDialog?.root, param)
        }
    }


    open fun isShowPg(visible: Boolean = true) {
        progressBarDialog?.let {
            if (visible && !it.root.isVisible) {
                runOnUiThread { it.root.beVisible() }
            } else {
                runOnUiThread { it.root.beGone() }
            }
        }
    }

    private var onActivityResult: ((ActivityResult) -> Unit)? = null
    private val startIntent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            onActivityResult?.invoke(it)
            onActivityResult = null
        }

    fun startActivityForResult(intent: Intent, callback: (ActivityResult) -> Unit = {}) {
        onActivityResult = callback
        startIntent.launch(intent)
    }

    private var onPermissionResult: ((Map<String, Boolean>) -> Unit)? = null
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            onPermissionResult?.invoke(it)
            onPermissionResult = null
        }

    fun requestPermissionLauncher(
        permissions: Array<String>,
        callback: (Map<String, Boolean>) -> Unit = {},
    ) {
        onPermissionResult = callback
        requestPermissionLauncher.launch(permissions)
    }
}