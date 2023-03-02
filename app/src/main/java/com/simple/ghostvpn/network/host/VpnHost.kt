package com.simple.ghostvpn.network.host

import com.simple.ghostvpn.network.host.Host

class VpnHost : Host {
    override fun releaseUrl(): String {
        return "https://apv.ghostproxyghost.com"
    }

    override fun debugUrl(): String {
        return "http://apv.ghostproxyghostt.click"
    }
}