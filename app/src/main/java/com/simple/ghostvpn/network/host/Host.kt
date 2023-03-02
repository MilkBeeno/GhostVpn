package com.simple.ghostvpn.network.host

import com.simple.ghostvpn.BuildConfig

interface Host {
    val realUrl: String
        get() = if (BuildConfig.DEBUG) debugUrl() else releaseUrl()

    fun releaseUrl(): String
    fun debugUrl(): String
}