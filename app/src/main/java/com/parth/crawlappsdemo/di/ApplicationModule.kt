package com.parth.crawlappsdemo.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.parth.crawlappsdemo.db.contracts.TaskContract
import com.parth.crawlappsdemo.db.provider.DaoProvider
import com.parth.crawlappsdemo.db.provider.DatabaseProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences(
            "App_Shared_Pref",
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun databaseProvider(application: Application) = DatabaseProvider(application)

    @Provides
    @Singleton
    fun daoProvider(database: DatabaseProvider) = DaoProvider(database)

    @Provides
    @Singleton
    fun taskContract(daoProvider: DaoProvider) = TaskContract(daoProvider)
}