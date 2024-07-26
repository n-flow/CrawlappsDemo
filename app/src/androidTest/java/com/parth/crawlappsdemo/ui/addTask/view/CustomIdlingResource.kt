package com.parth.crawlappsdemo.ui.addTask.view

import androidx.test.espresso.IdlingResource

class CustomIdlingResource : IdlingResource {
    @Volatile
    private var callback: IdlingResource.ResourceCallback? = null

    override fun getName(): String = CustomIdlingResource::class.java.name

    override fun isIdleNow(): Boolean {
        // Logic to determine if the resource is idle
        return true
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        this.callback = callback
    }
}