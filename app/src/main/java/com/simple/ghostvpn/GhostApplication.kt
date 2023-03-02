package com.simple.ghostvpn

import android.app.Application

class GhostApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        current = this
    }

    companion object {
        lateinit var current: Application
    }
}