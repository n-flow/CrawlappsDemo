package com.parth.crawlappsdemo.ui.dashboard.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.parth.crawlappsdemo.R
import com.parth.crawlappsdemo.databinding.ActivityDashboardBinding
import com.parth.crawlappsdemo.db.entity.TableTask
import com.parth.crawlappsdemo.dialogs.common.CommonMsgDialog
import com.parth.crawlappsdemo.ui.BaseActivity
import com.parth.crawlappsdemo.ui.addTask.view.AddTaskActivity
import com.parth.crawlappsdemo.ui.dashboard.view.adapter.TasksAdapter
import com.parth.crawlappsdemo.ui.dashboard.viewModel.DashboardViewModel
import com.parth.crawlappsdemo.ui.filter.data.model.FilterModel
import com.parth.crawlappsdemo.ui.filter.view.FilterActivity
import com.parth.crawlappsdemo.utils.extensions.parcelableArrayList
import com.parth.crawlappsdemo.utils.hideKeyboard
import com.parth.crawlappsdemo.utils.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : BaseActivity<ActivityDashboardBinding>(R.layout.activity_dashboard),
    View.OnClickListener {

    companion object {
        const val TAG = "DashboardActivity"
    }

    private val viewModel: DashboardViewModel by viewModels()

    private val tasksAdapter = TasksAdapter(::editTask, ::deleteTask)

    private var mBottomSheetDialogSorting: BottomSheetDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.onClick = this

        initView()
        setObserve()
    }

    private fun initView() {
        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = tasksAdapter
        }

        binding.etSearch.addTextChangedListener(afterTextChanged = {
            viewModel.searchText = it?.toString() ?: ""
            viewModel.searchTask()
            binding.ivClear.isVisible = !it?.toString().isNullOrEmpty()
        })

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                createSearchTextFocus()
            }
            true
        }
    }

    private fun setObserve() {
        viewModel.taskContract.getData().observe(this) {
            if (!it.isNullOrEmpty()) {
                viewModel.filterTask(it)
            }
        }

        viewModel.tasks.observe(this) {
            updateTasksList(it)
        }
    }

    private fun updateTasksList(list: ArrayList<TableTask>? = null) {
        binding.txtEmpty.isVisible = list.isNullOrEmpty()
        val recyclerViewState = binding.rvTasks.layoutManager?.onSaveInstanceState()
        tasksAdapter.updateList(list ?: ArrayList())
        binding.rvTasks.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

    private fun editTask(task: TableTask) {
        startActivity(Intent(activity, AddTaskActivity::class.java).putExtra("editableTask", task))
    }

    private fun deleteTask(task: TableTask) {
        CommonMsgDialog(activity, "Delete Task", "Are you sure you want to delete this task?") {
            if (it == CommonMsgDialog.POSITIVE_BTN_CLICK) {
                isShowPg()
                viewModel.deleteTask(task)
            }
        }.openDialog()
    }

    private fun createSearchTextFocus() {
        hideKeyboard(activity)
        binding.etSearch.clearFocus()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.fabAddTask -> {
                startActivity(Intent(this, AddTaskActivity::class.java))
            }

            binding.ivSearch -> {
                showKeyboard(binding.etSearch, activity)
            }

            binding.ivClear -> {
                binding.etSearch.setText("")
                showKeyboard(binding.etSearch, activity)
            }

            binding.ivSort -> {
                createSearchTextFocus()
                openSortDialog()
            }

            binding.ivFilter -> {
                createSearchTextFocus()
                startActivityForResult(
                    Intent(
                        this,
                        FilterActivity::class.java
                    ).putParcelableArrayListExtra("FilterValue", viewModel.getFilter())
                ) {
                    it.data?.let { intent ->
                        if (intent.hasExtra("FilterValue")) {
                            intent.parcelableArrayList<FilterModel>("FilterValue")
                                ?.let { filteredValue ->
                                    viewModel.updateFilter(filteredValue)
                                }
                        }
                    }
                    createSearchTextFocus()
                }
            }
        }
    }

    private fun openSortDialog() {
        if (mBottomSheetDialogSorting == null) {
            mBottomSheetDialogSorting =
                viewModel.sortClass.createSortDialog(activity, viewModel.sortList) {
                    onSortClick(it)
                }
        }
        mBottomSheetDialogSorting?.show()
    }

    private fun onSortClick(i: Int) {
        createSearchTextFocus()
        viewModel.sortIndex = i
        viewModel.sortList.forEach {
            it.isSelect = false
        }
        viewModel.sortList[i].isSelect = true
        viewModel.sortClass.updateList(viewModel.sortList)
        mBottomSheetDialogSorting?.hide()
        viewModel.sortTask()
    }
}