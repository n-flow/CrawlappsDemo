package com.parth.crawlappsdemo.db.provider

class DaoProvider(private val database: DatabaseProvider) {

    fun getTaskTableDao() = database.getInstance().getTaskTableDao()
}
