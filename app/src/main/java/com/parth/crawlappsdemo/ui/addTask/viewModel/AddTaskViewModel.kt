package com.parth.crawlappsdemo.ui.addTask.viewModel

import androidx.lifecycle.ViewModel
import com.parth.crawlappsdemo.core.data.models.taskStatus.getTaskStatusList
import com.parth.crawlappsdemo.db.contracts.TaskContract
import com.parth.crawlappsdemo.db.entity.TableTask
import com.parth.crawlappsdemo.db.entity.TaskEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    val taskContract: TaskContract
) : ViewModel() {

    var taskList = ArrayList<TableTask>()

    val taskStatusList = getTaskStatusList()

    var selectedTask = TableTask(Date().time, TaskEntity(Date().time.toString()))

    fun addTask(success: () -> Unit = {}) {
        taskList.filter { selectedTask.task.subTaskOf.contains(it.id.toString()) }.forEach {
            it.task.parentTaskOf.add(selectedTask.id.toString())
        }
        taskContract.insertAll(taskList.filter { selectedTask.task.subTaskOf.contains(it.id.toString()) })

        if (selectedTask.task.createdAt == 0L) {
            selectedTask.task.createdAt = Date().time
        }
        selectedTask.task.updatedAt = Date().time
        taskContract.insert(selectedTask)
        success.invoke()
    }

    fun deleteSubTask(task: TableTask) {
        task.task.parentTaskOf.remove(selectedTask.id.toString())
        taskContract.update(task)
    }
}