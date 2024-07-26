package com.parth.crawlappsdemo.dialogs.sort

class SortModel(
    var id: Int = 0,
    var name: String = "",
    var isSelect: Boolean = false
)

fun getSortModel(): ArrayList<SortModel> {
    val data = ArrayList<SortModel>()

    data.add(SortModel(0, "Ascending CreatedAt", true))
    data.add(SortModel(1, "Descending CreatedAt", false))
    data.add(SortModel(2, "Ascending DueDate", false))
    data.add(SortModel(3, "Descending DueDate", false))
    data.add(SortModel(4, "Alphabetically, Task Title A-Z", false))
    data.add(SortModel(5, "Alphabetically, Task Title Z-A", false))

    return data
}