package com.parth.crawlappsdemo.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.parth.crawlappsdemo.db.entity.TableTask
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskTableDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<TableTask>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: TableTask)

    @Update
    fun update(data: TableTask)

    @Delete
    fun delete(data: TableTask)

    @Query("DELETE FROM task_entity")
    fun deleteAllTasks()

    @Query("SELECT * FROM task_entity")
    fun getLiveTasks(): Flow<MutableList<TableTask>>
}