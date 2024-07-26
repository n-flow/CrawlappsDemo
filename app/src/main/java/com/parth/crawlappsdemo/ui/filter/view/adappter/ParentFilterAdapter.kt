package com.parth.crawlappsdemo.ui.filter.view.adappter

import android.annotation.SuppressLint
import androidx.core.view.isVisible
import com.parth.crawlappsdemo.R
import com.parth.crawlappsdemo.databinding.AdapterParentFilterBinding
import com.parth.crawlappsdemo.ui.BaseAdapter
import com.parth.crawlappsdemo.ui.filter.data.model.FilterModel

class ParentFilterAdapter(
    private val onCLick: (FilterModel) -> Unit = {}
) : BaseAdapter<AdapterParentFilterBinding>(R.layout.adapter_parent_filter) {

    private val filters = ArrayList<FilterModel>()

    private var oldPosition = -1

    @SuppressLint("NotifyDataSetChanged")
    fun updateFilter(value: ArrayList<FilterModel>) {
        oldPosition = value.indexOfFirst { it.isChecked }
        filters.clear()
        filters.addAll(value)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder<AdapterParentFilterBinding>, position: Int) {
        val value = filters[position]

        holder.binding.txtFilterName.text = value.name

        holder.binding.root.setOnClickListener {
            updateValue(position)
        }

        holder.binding.ivCheck.isVisible = value.isChecked
    }

    private fun updateValue(i: Int) {
        if (oldPosition != i) {
            filters.forEach {
                it.isChecked = false
            }

            if (oldPosition != -1) {
                notifyItemChanged(oldPosition)
            }
            filters[i].isChecked = true
            notifyItemChanged(i)
            onCLick.invoke(filters[i])
        }
        oldPosition = i
    }

    override fun getItemCount() = filters.size
}