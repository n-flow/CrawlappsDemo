package com.parth.crawlappsdemo.ui.addTask.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.parth.crawlappsdemo.R
import com.parth.crawlappsdemo.databinding.DropDownItemsBinding
import com.parth.crawlappsdemo.core.data.models.taskStatus.TaskStatusModel

class TaskStatusArrayAdapter(
    context: Context,
    private val onStatusClick: (TaskStatusModel) -> Unit = {}
) : ArrayAdapter<TaskStatusModel>(context, R.layout.drop_down_items) {

    private var originalList = ArrayList<TaskStatusModel>()
    private var filteredList = ArrayList<TaskStatusModel>()

    private var mFilter: CustomFilter? = null

    fun updateValues(data: ArrayList<TaskStatusModel>, isClearFilterValue: Boolean = true) {
        originalList.addAll(data)
        if (isClearFilterValue) {
            filteredList.clear()
            filteredList.addAll(data)
        }
        if (filteredList.size > 0) {
            notifyDataSetChanged()
        } else {
            notifyDataSetInvalidated()
        }
    }

    override fun getFilter(): Filter {
        if (mFilter == null)
            mFilter = CustomFilter()
        return mFilter!!
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val binding: DropDownItemsBinding
        if (view == null) {
            binding =
                DropDownItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            view = binding.root
        } else {
            binding = view.tag as DropDownItemsBinding
        }

        binding.txtStatus.text = filteredList[position].status ?: ""
        view.tag = binding

        binding.root.setOnClickListener {
            onStatusClick.invoke(filteredList[position])
        }

        return view
    }

    override fun getCount() = filteredList.size

    override fun getItem(position: Int): TaskStatusModel {
        return filteredList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private inner class CustomFilter : Filter() {
        override fun performFiltering(prefix: CharSequence?): FilterResults {
            val filteredItems = ArrayList<TaskStatusModel>()
            val filters = originalList.filter {
                it.status?.lowercase()?.contains(prefix.toString().lowercase()) == true
            }
            filteredItems.addAll(filters)

            val results = FilterResults()
            results.count = if (prefix.isNullOrEmpty()) originalList.size else filteredItems.size
            results.values = if (prefix.isNullOrEmpty()) originalList else filteredItems

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            @Suppress("UNCHECKED_CAST")
            if (results.values != null && results.values is ArrayList<*>) {
                filteredList.clear()
                filteredList.addAll(results.values as Collection<TaskStatusModel>)
            }

            if (filteredList.size > 0) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }
}