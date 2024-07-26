package com.parth.crawlappsdemo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T : ViewDataBinding>(
    private val k: Int
) : RecyclerView.Adapter<BaseAdapter.ViewHolder<T>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), k, parent, false
            )
        )
    }

    class ViewHolder<T : ViewDataBinding>(val binding: T) :
        RecyclerView.ViewHolder(binding.root)
}