package com.parth.crawlappsdemo.db.typeConverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.parth.crawlappsdemo.core.data.models.taskStatus.TaskStatusModel
import com.parth.crawlappsdemo.db.entity.TaskEntity

class AppDbConverter {

    @TypeConverter
    fun stringToLevels(json: String): TaskEntity {
        val type = object : TypeToken<TaskEntity>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun levelsToString(list: TaskEntity): String {
        val type = object : TypeToken<TaskEntity>() {}.type
        return Gson().toJson(list, type)
    }

    @TypeConverter
    fun stringToTaskStatusModel(json: String): TaskStatusModel {
        val type = object : TypeToken<TaskStatusModel>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun taskStatusModelToString(data: TaskStatusModel): String {
        val type = object : TypeToken<TaskStatusModel>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun stringToList(json: String): ArrayList<String> {
        val type = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun listToString(list: ArrayList<String>): String {
        val type = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().toJson(list, type)
    }
}