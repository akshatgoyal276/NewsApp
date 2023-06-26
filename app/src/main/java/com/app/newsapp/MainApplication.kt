package com.app.newsapp

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application() {

    companion object {
        var instance: MainApplication? = null
        val context: Context?
            get() = instance
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
    }
}