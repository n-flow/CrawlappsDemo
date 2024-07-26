package com.parth.crawlappsdemo.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.parth.crawlappsdemo.ui.dashboard.view.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initScreen()
    }

    private fun initScreen() {
        Handler(Looper.myLooper()!!).postDelayed({
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }, 500)
    }
}