package com.parth.crawlappsdemo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.parth.crawlappsdemo.db.dao.TaskTableDao
import com.parth.crawlappsdemo.db.entity.TableTask
import com.parth.crawlappsdemo.db.typeConverter.AppDbConverter

@Database(
    entities = [
        TableTask::class
    ],
    version = 1, exportSchema = false
)
@TypeConverters(AppDbConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTaskTableDao(): TaskTableDao
}