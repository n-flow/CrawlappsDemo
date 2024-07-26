package com.parth.crawlappsdemo.ui.dashboard.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parth.crawlappsdemo.core.data.models.taskStatus.getTaskStatusList
import com.parth.crawlappsdemo.db.contracts.TaskContract
import com.parth.crawlappsdemo.db.entity.TableTask
import com.parth.crawlappsdemo.dialogs.sort.SortClass
import com.parth.crawlappsdemo.dialogs.sort.getSortModel
import com.parth.crawlappsdemo.ui.filter.data.model.FilterId
import com.parth.crawlappsdemo.ui.filter.data.model.FilterModel
import com.parth.crawlappsdemo.ui.filter.data.model.FilterValue
import com.parth.crawlappsdemo.ui.filter.data.util.getTaskFilters
import com.parth.crawlappsdemo.utils.extensions.deleteIf
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    val taskContract: TaskContract
) : ViewModel() {

    companion object {
        const val TAG = "DashboardViewModel"
    }

    private var filters = getTaskFilters()
    private val taskStatus = getTaskStatusList()

    val sortClass = SortClass()
    val sortList = getSortModel()
    var searchText = ""
    var sortIndex = 0

    private var taskList = ArrayList<TableTask>()
    private var filterTasks = ArrayList<TableTask>()
    private var searchTasks = ArrayList<TableTask>()

    private var _filter = MutableLiveData<ArrayList<FilterModel>>()
    val filter: LiveData<ArrayList<FilterModel>>
        get() = _filter

    private var _tasks = MutableLiveData<ArrayList<TableTask>>()
    val tasks: LiveData<ArrayList<TableTask>>
        get() = _tasks

    init {
        updateFilters()
    }

    fun filterTask(tasks: MutableList<TableTask>? = null) {
        if (!tasks.isNullOrEmpty()) {
            taskList.clear()
            taskList.addAll(tasks)
        }

        val list = ArrayList(taskList)
        filters.first { it.id == FilterId.STATUS }.apply {
            if (filterValue.any { it.isChecked }) {
                val idMap = filterValue.map { if (it.isChecked) it.id else "-2" }
                list.deleteIf { !idMap.contains(it.task.status?.id?.toString() ?: "") }
            }
        }

        filterTasks = list
        searchTask()
    }

    fun searchTask() {
        val list = ArrayList<TableTask>()
        list.addAll(filterTasks)

        if (searchText.isNotEmpty()) {
            list.deleteIf {
                it.task.status?.status?.contains(searchText, true) != true &&
                it.task.description?.contains(searchText, true) != true &&
                        it.task.title?.contains(searchText, true) != true
            }
        }

        searchTasks = list
        sortTask()
    }

    fun sortTask() {
        val list = ArrayList<TableTask>()
        list.addAll(searchTasks)

        when (sortIndex) {
            1 -> {
                list.sortWith { o1, o2 -> o2.task.createdAt.compareTo(o1.task.createdAt) }
            }

            2 -> {
                list.sortWith { o1, o2 -> o1.task.dueDate.compareTo(o2.task.dueDate) }
            }

            3 -> {
                list.sortWith { o1, o2 -> o2.task.dueDate.compareTo(o1.task.dueDate) }
            }

            4 -> {
                list.sortWith { o1, o2 -> o1.task.title?.compareTo(o2.task.title ?: "") ?: 0 }
            }

            5 -> {
                list.sortWith { o1, o2 -> o2.task.title?.compareTo(o1.task.title ?: "") ?: 0 }
            }

            else -> {
                list.sortWith { o1, o2 -> o1.task.createdAt.compareTo(o2.task.createdAt) }
            }
        }

        _tasks.postValue(list)
    }

    fun updateFilter(filter: ArrayList<FilterModel>) {
        filters = filter
        filterTask()
    }

    fun getFilter() = filters

    private fun updateFilters() {
        val statusValues = ArrayList<FilterValue>()
        taskStatus.forEach {
            statusValues.add(FilterValue(it.id.toString(), it.status ?: ""))
        }
        filters.first { it.id == FilterId.STATUS }.apply {
            if (filters.isEmpty()) {
                filterValue = statusValues
            } else {
                statusValues.deleteIf { status -> filterValue.any { it.id == status.id } }
                filterValue.addAll(statusValues)
            }
        }
    }

    fun deleteTask(task: TableTask) {
        taskContract.delete(task)
    }
}