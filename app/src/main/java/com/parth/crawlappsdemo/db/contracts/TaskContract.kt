package com.parth.crawlappsdemo.db.contracts

import androidx.lifecycle.asLiveData
import com.parth.crawlappsdemo.db.entity.TableTask
import com.parth.crawlappsdemo.db.provider.DaoProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskContract(provider: DaoProvider) {

    private val dao = provider.getTaskTableDao()

    fun insertAll(data: List<TableTask>) {
        CoroutineScope(Dispatchers.Default).launch { dao.insertAll(data) }
    }

    fun insert(data: TableTask) {
        CoroutineScope(Dispatchers.Default).launch { dao.insert(data) }
    }

    fun update(data: TableTask) {
        CoroutineScope(Dispatchers.Default).launch { dao.update(data) }
    }

    fun delete(data: TableTask) {
        CoroutineScope(Dispatchers.Default).launch { dao.delete(data) }
    }

    fun deleteAllData() {
        CoroutineScope(Dispatchers.Default).launch { dao.deleteAllTasks() }
    }

    fun getData() = dao.getLiveTasks().asLiveData(Dispatchers.IO)
}