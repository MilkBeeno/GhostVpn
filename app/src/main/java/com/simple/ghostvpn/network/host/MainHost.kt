package com.simple.ghostvpn.network.host

class MainHost : Host {
    override fun releaseUrl(): String {
        return "https://api.ghostproxyghost.com"
    }

    override fun debugUrl(): String {
        return "http://api.ghostproxyghostt.click"
    }
}