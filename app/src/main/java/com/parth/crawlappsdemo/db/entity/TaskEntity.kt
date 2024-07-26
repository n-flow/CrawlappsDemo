package com.parth.crawlappsdemo.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.parth.crawlappsdemo.core.data.models.taskStatus.TaskStatusModel
import java.io.Serializable

@Entity(tableName = "task_entity")
data class TableTask(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id: Long = 0,

    @SerializedName("task")
    @ColumnInfo(name = "task")
    var task: TaskEntity
) : Serializable

class TaskEntity(

    @SerializedName("id")
    var id: String = "",

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("createdAt")
    var createdAt: Long = 0,

    @SerializedName("updatedAt")
    var updatedAt: Long = 0,

    @SerializedName("status")
    var status: TaskStatusModel? = null,

    @SerializedName("dueDate")
    var dueDate: Long = 0,

    @SerializedName("subTaskOf")
    var subTaskOf: ArrayList<String> = ArrayList(),

    @SerializedName("parentTaskOf")
    var parentTaskOf: ArrayList<String> = ArrayList(),
) : Serializable