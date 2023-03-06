package com.simple.ghostvpn.ad.unitId

import com.simple.ghostvpn.BuildConfig

class InterstitialAdID : AdID {
    override fun debug(): String {
        return "ca-app-pub-3940256099942544/1033173712"
    }

    override fun release(): String {
        return "ca-app-pub-4684374725464850/3438883642"
    }

    companion object {
        val value = if (BuildConfig.DEBUG) {
            InterstitialAdID().debug()
        } else {
            InterstitialAdID().release()
        }
    }
}