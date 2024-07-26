package com.parth.crawlappsdemo.core.data.models.taskStatus

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TaskStatusModel(

    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("status")
    var status: String? = null
) : Serializable

fun getTaskStatusList(): ArrayList<TaskStatusModel> {
    val data = ArrayList<TaskStatusModel>()

    data.add(TaskStatusModel(0, "To-Do"))
    data.add(TaskStatusModel(1, "In-Progress"))
    data.add(TaskStatusModel(2, "Done"))

    return data
}