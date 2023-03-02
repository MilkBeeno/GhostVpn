package com.simple.ghostvpn

import android.app.Application
import com.milk.simple.log.Logger
import com.milk.simple.mdr.KvManger
import com.simple.ghostvpn.activity.LauncherActivity
import com.simple.ghostvpn.activity.LottieActivity
import com.simple.ghostvpn.util.BackgroundMonitor

class GhostApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        current = this
        initLibrary()
        backgroundMonitor()
    }

    private fun initLibrary() {
        KvManger.initialize(current)
        Logger.initialize(BuildConfig.DEBUG)
    }

    private fun backgroundMonitor() {
        BackgroundMonitor.registered(current) {
            if (it !is LauncherActivity && it !is LottieActivity)
                LottieActivity.start(current,false)
        }
    }

    companion object {
        lateinit var current: Application
    }
}