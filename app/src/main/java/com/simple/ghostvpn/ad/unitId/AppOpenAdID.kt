package com.simple.ghostvpn.ad.unitId

import com.simple.ghostvpn.BuildConfig

class AppOpenAdID : AdID {
    override fun debug(): String {
        return "ca-app-pub-3940256099942544/3419835294"
    }

    override fun release(): String {
        return "ca-app-pub-4684374725464850/3874546658"
    }

    companion object {
        val value = if (BuildConfig.DEBUG) {
            AppOpenAdID().debug()
        } else {
            AppOpenAdID().release()
        }
    }
}