package com.parth.crawlappsdemo.ui.dashboard.view.adapter

import android.graphics.Typeface
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import com.parth.crawlappsdemo.R
import com.parth.crawlappsdemo.databinding.AdapterTasksBinding
import com.parth.crawlappsdemo.db.entity.TableTask
import com.parth.crawlappsdemo.ui.BaseAdapter
import com.parth.crawlappsdemo.utils.DateFormats
import com.parth.crawlappsdemo.utils.dateToString
import java.util.Date

class TasksAdapter(
    private val onEditClick: (TableTask) -> Unit = {},
    private val onDeleteClick: (TableTask) -> Unit = {},
    private val showEditButtons: Boolean = true,
    private val showDeleteButtons: Boolean = true,
) : BaseAdapter<AdapterTasksBinding>(R.layout.adapter_tasks) {

    val tasks = ArrayList<TableTask>()

    fun updateList(newItems: List<TableTask>) {
        val oldItems: ArrayList<TableTask> = ArrayList(tasks)
        tasks.clear()
        tasks.addAll(newItems)

        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldItems.size
            }

            override fun getNewListSize(): Int {
                return tasks.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition] == newItems[newItemPosition]
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition] == newItems[newItemPosition]
            }
        }).dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: ViewHolder<AdapterTasksBinding>, position: Int) {
        val task = tasks[position]
        holder.binding.data = task.task

        val today = dateToString(Date().time, DateFormats.FORMAT_30)
        val taskDueDate = dateToString(task.task.dueDate, DateFormats.FORMAT_30)
        holder.binding.txtTaskDueDate.text = taskDueDate

        val dateColor = when {
            !task.task.status?.status.equals("done", true) && (today.equals(taskDueDate, true) || Date(
                task.task.dueDate
            ).before(Date())) -> {
                R.color.red
            }

            else -> {
                R.color.black
            }
        }
        holder.binding.txtTaskDueDate.setTextColor(
            ContextCompat.getColor(
                holder.binding.root.context,
                dateColor
            )
        )
        holder.binding.txtTaskDueDate.setTypeface(
            holder.binding.txtTaskDueDate.typeface,
            if (dateColor == R.color.red) Typeface.BOLD else Typeface.NORMAL
        )

        val statusColor = when {
            task.task.status?.status.equals("done", true) -> {
                R.color.green
            }

            else -> {
                R.color.black
            }
        }
        holder.binding.txtTaskStatus.setTextColor(
            ContextCompat.getColor(
                holder.binding.root.context,
                statusColor
            )
        )

        holder.binding.ivEdit.isVisible = showEditButtons
        holder.binding.ivDelete.isVisible = showDeleteButtons

        holder.binding.ivEdit.setOnClickListener {
            onEditClick.invoke(task)
        }

        holder.binding.ivDelete.setOnClickListener {
            onDeleteClick.invoke(task)
        }
    }

    override fun getItemCount() = tasks.size
}