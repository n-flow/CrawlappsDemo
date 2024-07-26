package com.parth.crawlappsdemo.ui.filter.data.util

import com.parth.crawlappsdemo.ui.filter.data.model.FilterId
import com.parth.crawlappsdemo.ui.filter.data.model.FilterModel
import com.parth.crawlappsdemo.ui.filter.data.model.FilterType

fun getTaskFilters(): ArrayList<FilterModel> {
    val data = ArrayList<FilterModel>()

    data.add(getStatus())

    return data
}

fun getStatus(): FilterModel {
    return FilterModel(
        id = FilterId.STATUS,
        type = FilterType.CHECK_BOX,
        name = "Task Status",
        filterValue = ArrayList(),
        isChecked = false
    )
}