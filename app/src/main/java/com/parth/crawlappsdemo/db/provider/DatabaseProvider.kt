package com.parth.crawlappsdemo.db.provider

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.parth.crawlappsdemo.db.AppDatabase

class DatabaseProvider(private val activity: Application) {

    private var database: AppDatabase? = null

    fun getInstance(): AppDatabase =
        database ?: synchronized(this) {
            database ?: buildDatabase().also { database = it }
        }

    private fun buildDatabase(): AppDatabase =
        Room.databaseBuilder(
            activity.applicationContext,
            AppDatabase::class.java, "APP_DB"
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {})
            .build()
}