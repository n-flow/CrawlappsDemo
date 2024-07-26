package com.parth.crawlappsdemo.ui.filter.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.parth.crawlappsdemo.R
import com.parth.crawlappsdemo.databinding.ActivityFilterBinding
import com.parth.crawlappsdemo.ui.BaseActivity
import com.parth.crawlappsdemo.ui.filter.data.model.FilterModel
import com.parth.crawlappsdemo.ui.filter.view.adappter.ParentFilterAdapter
import com.parth.crawlappsdemo.ui.filter.view.adappter.SubFilterAdapter
import com.parth.crawlappsdemo.utils.extensions.parcelableArrayList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterActivity : BaseActivity<ActivityFilterBinding>(R.layout.activity_filter),
    View.OnClickListener {

    companion object {
        const val TAG = "FilterActivity"
    }

    private var filters = ArrayList<FilterModel>()

    private val parentFilterAdapter = ParentFilterAdapter {
        updateSubFilter(it)
    }

    private val subFilterAdapter = SubFilterAdapter { value ->
        if (filters.any { it.id == value.id }) {
            filters[filters.indexOfFirst { it.id == value.id }] = value
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.onClick = this

        checkIntentData()
    }

    private fun checkIntentData() {
        if (intent?.hasExtra("FilterValue") == true) {
            intent.parcelableArrayList<FilterModel>("FilterValue")
                ?.let { filteredValue ->
                    filters = filteredValue
                    initView()
                }
        }
    }

    private fun initView() {
        binding.rvParentFilter.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = parentFilterAdapter
        }

        binding.rvSubFilter.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = subFilterAdapter
        }

        resetParentFilter()
    }

    private fun resetParentFilter() {
        val selected = filters.firstOrNull { it.isChecked }
        if (selected == null) {
            filters[0].isChecked = true
        }
        parentFilterAdapter.updateFilter(filters)
        updateSubFilter(filters.first { it.isChecked })
    }

    private fun updateSubFilter(i: FilterModel) {
        subFilterAdapter.updateFilter(i)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivBack -> {
                finish()
            }

            binding.btnClear -> {
                filters.forEach {
                    it.isChecked = false
                    it.filterValue.forEach { value ->
                        value.isChecked = false
                    }
                }
                resetParentFilter()
            }

            binding.btnApply -> {
                val intent = Intent()
                intent.putParcelableArrayListExtra("FilterValue", filters)
                setResult(1, intent)
                finish()
            }
        }
    }

}