package com.parth.crawlappsdemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<T : ViewDataBinding>(
    @LayoutRes private val contentLayoutId: Int
) : Fragment() {

    lateinit var activityBase: BaseActivity<*>
    lateinit var binding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activityBase = requireActivity() as BaseActivity<*>
        binding = DataBindingUtil.inflate(inflater, contentLayoutId, container, false)
        return binding.root
    }
}