package com.parth.crawlappsdemo.ui.addTask.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.parth.crawlappsdemo.R
import com.parth.crawlappsdemo.core.data.models.taskStatus.getTaskStatusList
import com.parth.crawlappsdemo.databinding.ActivityAddTaskBinding
import com.parth.crawlappsdemo.db.entity.TableTask
import com.parth.crawlappsdemo.dialogs.common.CommonMsgDialog
import com.parth.crawlappsdemo.ui.BaseActivity
import com.parth.crawlappsdemo.ui.addTask.view.adapter.TaskStatusArrayAdapter
import com.parth.crawlappsdemo.ui.addTask.view.adapter.TasksArrayAdapter
import com.parth.crawlappsdemo.ui.addTask.viewModel.AddTaskViewModel
import com.parth.crawlappsdemo.ui.dashboard.view.adapter.TasksAdapter
import com.parth.crawlappsdemo.utils.DateFormats
import com.parth.crawlappsdemo.utils.dateToString
import com.parth.crawlappsdemo.utils.extensions.serializable
import com.parth.crawlappsdemo.utils.extensions.showToast
import com.parth.crawlappsdemo.utils.extensions.stringRes
import com.parth.crawlappsdemo.utils.hideKeyboard
import com.parth.crawlappsdemo.utils.openDatePickerDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class AddTaskActivity : BaseActivity<ActivityAddTaskBinding>(R.layout.activity_add_task),
    View.OnClickListener {


    val viewModel: AddTaskViewModel by viewModels()

    private val subTasksAdapter = TasksAdapter(onDeleteClick = ::onDeleteClick, showEditButtons = false)
    private val parentTasksAdapter = TasksAdapter(onDeleteClick = ::onDeleteClick, showEditButtons = false, showDeleteButtons = false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.onClick = this

        initView()
        updateDueDate(Date().time)
        checkIntent()
        setObserve()
    }

    private fun initView() {
        binding.rvParentTasks.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = parentTasksAdapter
        }

        binding.rvSubasks.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = subTasksAdapter
        }
    }

    private fun checkIntent() {
        if (intent.hasExtra("editableTask")) {
            intent.serializable<TableTask>("editableTask")?.let {
                viewModel.selectedTask = it
                binding.etTitle.setText(it.task.title)
                binding.etDescription.setText(it.task.description)
                binding.etDueDates.setText(dateToString(it.task.dueDate, DateFormats.FORMAT_30))

                binding.etDropdownTaskStatus.setText(it.task.status?.status ?: "")
                binding.etDropdownSubTaskOf.setText("")

                binding.btnSubmit.text = stringRes(R.string.update)
            }
        }

        initTaskStatusAdapter()
        updateSubTasksList()
        updateParentTasksList()
        initSubTaskListAdapter()
    }

    private fun initTaskStatusAdapter() {
        val taskStatusAdapter = TaskStatusArrayAdapter(this) {
            viewModel.selectedTask.task.status = it
            binding.etDropdownTaskStatus.setText(viewModel.selectedTask.task.status?.status ?: "")
            hideKeyboard(activity)
            binding.etDropdownTaskStatus.clearFocus()
        }
        binding.etDropdownTaskStatus.setAdapter(taskStatusAdapter)
        taskStatusAdapter.updateValues(viewModel.taskStatusList)
        binding.etDropdownTaskStatus.setText(viewModel.selectedTask.task.status?.status ?: "")
    }

    private fun initSubTaskListAdapter() {
        binding.boxSubTaskOf.isVisible = viewModel.taskList.isNotEmpty()
        val subList = viewModel.taskList.filter { viewModel.selectedTask.id != it.id && !viewModel.selectedTask.task.parentTaskOf.contains(it.id.toString()) && !viewModel.selectedTask.task.subTaskOf.contains(it.id.toString()) }
        val taskAdapter = TasksArrayAdapter(this) {
            viewModel.selectedTask.task.subTaskOf.add(it.id.toString())
            binding.etDropdownSubTaskOf.setText("")
            initSubTaskListAdapter()
            updateSubTasksList()
        }
        binding.etDropdownSubTaskOf.setAdapter(taskAdapter)
        taskAdapter.updateValues(subList)
    }

    private fun updateDueDate(date: Long) {
        viewModel.selectedTask.task.dueDate = date
        binding.etDueDates.setText(dateToString(date, DateFormats.FORMAT_30))
    }

    private fun setObserve() {
        viewModel.taskContract.getData().observe(this) {
            if (!it.isNullOrEmpty()) {
                viewModel.taskList.clear()
                viewModel.taskList.addAll(it)
                updateSubTasksList()
                updateParentTasksList()
                initSubTaskListAdapter()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivBack -> {
                finish()
            }

            binding.boxDueDates, binding.etDueDates -> {
                openDatePickerDialog {
                    updateDueDate(it)
                }
            }

            binding.btnSubmit -> {
                val title = binding.etTitle.text?.toString()
                viewModel.selectedTask.task.title = title

                val isAnyTaskPending = viewModel.taskList.filter { viewModel.selectedTask.task.parentTaskOf.contains(it.id.toString()) }.filter { it.task.status?.id != 2}

                if (viewModel.selectedTask.task.title.isNullOrEmpty()) {
                    "Please enter task title".showToast(this)
                } else if (viewModel.selectedTask.task.status == null) {
                    "Please select task status".showToast(this)
                } else if (viewModel.selectedTask.task.dueDate == 0L) {
                    "Please select task due date".showToast(this)
                } else if (viewModel.selectedTask.task.status?.id == 2 && isAnyTaskPending.isNotEmpty()) {
                    "There is total ${isAnyTaskPending.size} pending task. Please complete it first".showToast(this)
                } else {
                    isShowPg()
                    viewModel.selectedTask.task.description = binding.etDescription.text?.toString()
                    viewModel.addTask {
                        finish()
                    }
                }
            }
        }
    }

    private fun onDeleteClick(task: TableTask) {
        CommonMsgDialog(activity, "Delete Task", "Are you sure you want to delete this sub task?") {
            if (it == CommonMsgDialog.POSITIVE_BTN_CLICK) {
                viewModel.deleteSubTask(task)
                viewModel.selectedTask.task.subTaskOf.remove(task.id.toString())
                updateSubTasksList()
            }
        }.openDialog()
    }

    private fun updateSubTasksList() {
        val subList = viewModel.taskList.filter { viewModel.selectedTask.task.subTaskOf.any { task -> task.equals(it.id.toString(), true) } }
        binding.txtSubTask.isVisible = viewModel.selectedTask.task.subTaskOf.isNotEmpty()
        binding.rvSubasks.isVisible = viewModel.selectedTask.task.subTaskOf.isNotEmpty()
        val recyclerViewState = binding.rvParentTasks.layoutManager?.onSaveInstanceState()
        subTasksAdapter.updateList(subList)
        binding.rvSubasks.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

    private fun updateParentTasksList() {
        val parentList = viewModel.taskList.filter { viewModel.selectedTask.task.parentTaskOf.any { task -> task.equals(it.id.toString(), true) } }
        binding.txtParentTask.isVisible = viewModel.selectedTask.task.parentTaskOf.isNotEmpty()
        binding.rvParentTasks.isVisible = viewModel.selectedTask.task.parentTaskOf.isNotEmpty()
        val recyclerViewState = binding.rvParentTasks.layoutManager?.onSaveInstanceState()
        parentTasksAdapter.updateList(parentList)
        binding.rvParentTasks.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }
}