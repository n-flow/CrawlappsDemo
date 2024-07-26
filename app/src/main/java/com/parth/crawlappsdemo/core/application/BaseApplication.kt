package com.parth.crawlappsdemo.core.application

import androidx.lifecycle.LifecycleObserver
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : MultiDexApplication(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
    }
}