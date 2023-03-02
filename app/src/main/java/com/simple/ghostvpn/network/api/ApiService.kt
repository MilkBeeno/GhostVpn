package com.simple.ghostvpn.network.api

import com.simple.ghostvpn.network.OkhttpClient

object ApiService {
    val vpn: VpnApiService =
        OkhttpClient.vpn().create(VpnApiService::class.java)
}