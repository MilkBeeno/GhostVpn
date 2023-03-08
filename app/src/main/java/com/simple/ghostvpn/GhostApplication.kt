package com.simple.ghostvpn

import android.app.Application
import com.google.android.gms.ads.AdActivity
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.simple.ghostvpn.activity.LauncherActivity
import com.simple.ghostvpn.activity.LottieActivity
import com.simple.ghostvpn.util.BackgroundMonitor
import com.simple.ghostvpn.util.KvManger
import com.simple.ghostvpn.util.log.Logger

class GhostApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        current = this
        initAdmob()
        initLibrary()
        initMonitor()
    }

    private fun initAdmob() {
        MobileAds.initialize(current) {
            if (BuildConfig.DEBUG) {
                val testDeviceNumbers =
                    listOf("c1aadd83-3bcd-474d-aac2-cd1e2e83ef22")
                MobileAds.setRequestConfiguration(
                    RequestConfiguration
                        .Builder()
                        .setTestDeviceIds(testDeviceNumbers)
                        .build()
                )
            }
        }
    }

    private fun initLibrary() {
        KvManger.initialize(current)
        Logger.initialize(BuildConfig.DEBUG)
    }

    private fun initMonitor() {
        BackgroundMonitor.registered(current) {
            if (it !is LauncherActivity && it !is LottieActivity && it !is AdActivity) {
                LottieActivity.start(current, false)
            }
        }
    }

    companion object {
        lateinit var current: Application
    }
}