package com.app.adstertimes.main

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

val applicationContext = MainApplication.context

@HiltAndroidApp
class MainApplication: Application() {

    companion object {
        lateinit var instance: MainApplication
        val context: Context
            get() = instance
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
    }
}