package com.simple.ghostvpn.repository

import com.simple.ghostvpn.network.api.ApiService
import com.simple.ghostvpn.network.retrofit

object VpnRepository {

    suspend fun getVpnInfo(id: Long) = retrofit { ApiService.vpn.getVpnInfo(id) }

    suspend fun getVpnListInfo() = retrofit { ApiService.vpn.getVpnListInfo() }
}