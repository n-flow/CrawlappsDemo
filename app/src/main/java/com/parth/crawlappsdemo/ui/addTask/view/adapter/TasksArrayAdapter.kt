package com.parth.crawlappsdemo.ui.addTask.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.parth.crawlappsdemo.R
import com.parth.crawlappsdemo.databinding.DropDownItemsBinding
import com.parth.crawlappsdemo.db.entity.TableTask

class TasksArrayAdapter(
    context: Context,
    private val onStatusClick: (TableTask) -> Unit = {}
) : ArrayAdapter<TableTask>(context, R.layout.drop_down_items) {

    private var originalList = ArrayList<TableTask>()
    private var filteredList = ArrayList<TableTask>()

    private var mFilter: CustomFilter? = null

    fun updateValues(data: List<TableTask>, isClearFilterValue: Boolean = true) {
        originalList.clear()
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

        binding.txtStatus.text = filteredList[position].id.toString()
        view.tag = binding

        binding.root.setOnClickListener {
            onStatusClick.invoke(filteredList[position])
        }

        return view
    }

    override fun getCount() = filteredList.size

    override fun getItem(position: Int): TableTask {
        return filteredList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private inner class CustomFilter : Filter() {
        override fun performFiltering(prefix: CharSequence?): FilterResults {
            val filteredItems = ArrayList<TableTask>()
            val filters = originalList.filter {
                it.id.toString().lowercase().contains(prefix.toString().lowercase()) ||
                it.task.id.lowercase().contains(prefix.toString().lowercase()) ||
                it.task.title.toString().lowercase().contains(prefix.toString().lowercase()) ||
                it.task.description.toString().lowercase().contains(prefix.toString().lowercase())
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
                filteredList.addAll(results.values as Collection<TableTask>)
            }

            if (filteredList.size > 0) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }
}