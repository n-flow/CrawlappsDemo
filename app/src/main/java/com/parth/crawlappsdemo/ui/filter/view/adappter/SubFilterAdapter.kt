package com.parth.crawlappsdemo.ui.filter.view.adappter

import android.annotation.SuppressLint
import com.parth.crawlappsdemo.R
import com.parth.crawlappsdemo.databinding.AdapterSubFilterBinding
import com.parth.crawlappsdemo.ui.BaseAdapter
import com.parth.crawlappsdemo.ui.filter.data.model.FilterModel
import com.parth.crawlappsdemo.ui.filter.data.model.FilterType

class SubFilterAdapter(
    private val onCLick: (FilterModel) -> Unit = {}
) : BaseAdapter<AdapterSubFilterBinding>(R.layout.adapter_sub_filter) {

    private var filters = FilterModel()
    private var oldPosition = -1

    @SuppressLint("NotifyDataSetChanged")
    fun updateFilter(value: FilterModel) {
        filters = value
        oldPosition = -1
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder<AdapterSubFilterBinding>, position: Int) {
        val value = filters.filterValue[position]

        holder.binding.txtFilterName.text = value.name

        holder.binding.root.setOnClickListener {
            updateValue(position)
            onCLick.invoke(filters)
        }

        holder.binding.ivCheck.setImageResource(if (value.isChecked) R.drawable.ic_check else R.drawable.ic_uncheck)
    }

    override fun getItemCount() = filters.filterValue.size

    private fun updateValue(i: Int) {
        if (filters.type == FilterType.CHECK_BOX) {
            filters.filterValue[i].isChecked = !filters.filterValue[i].isChecked
        } else if (oldPosition != i) {
            if (oldPosition != -1) {
                filters.filterValue[oldPosition].isChecked = false
            }
            filters.filterValue[i].isChecked = true
        }

        if (oldPosition != -1) {
            notifyItemChanged(oldPosition)
        }
        notifyItemChanged(i)
        oldPosition = i
    }
}