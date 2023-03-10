package com.simple.ghostvpn.repository

import com.freetech.vpn.utils.VpnWhiteList
import com.simple.ghostvpn.data.body.AppConfigRequestModel
import com.simple.ghostvpn.network.OkhttpClient
import com.simple.ghostvpn.network.api.MainApiService
import com.simple.ghostvpn.network.retrofit
import com.simple.ghostvpn.util.KvManger
import com.simple.ghostvpn.util.ktx.ioScope

object AppRepository {
    private val main: MainApiService =
        OkhttpClient.main().create(MainApiService::class.java)

    var shareAppUrl = ""
    var showOpenAd: Boolean = true
        set(value) {
            KvManger.put("showOpenAd", value)
            field = value
        }
        get() {
            field = KvManger.getBoolean("showOpenAd")
            return field
        }
    var showMainNativeAd: Boolean = true
        set(value) {
            KvManger.put("showMainNativeAd", value)
            field = value
        }
        get() {
            field = KvManger.getBoolean("showMainNativeAd")
            return field
        }
    var showConnectedInsertAd: Boolean = true
        set(value) {
            KvManger.put("showConnectedInsertAd", value)
            field = value
        }
        get() {
            field = KvManger.getBoolean("showConnectedInsertAd")
            return field
        }
    var showConnectedNativeAd: Boolean = true
        set(value) {
            KvManger.put("showConnectedNativeAd", value)
            field = value
        }
        get() {
            field = KvManger.getBoolean("showConnectedNativeAd")
            return field
        }
    var showDisconnectInsertAd: Boolean = true
        set(value) {
            KvManger.put("showDisconnectInsertAd", value)
            field = value
        }
        get() {
            field = KvManger.getBoolean("showDisconnectInsertAd")
            return field
        }
    var showDisconnectNativeAd: Boolean = true
        set(value) {
            KvManger.put("showDisconnectNativeAd", value)
            field = value
        }
        get() {
            field = KvManger.getBoolean("showDisconnectNativeAd")
            return field
        }
    var showVpnListNativeAd: Boolean = true
        set(value) {
            KvManger.put("showSwitchNativeAd", value)
            field = value
        }
        get() {
            field = KvManger.getBoolean("showSwitchNativeAd")
            return field
        }

    fun getConfig() {
        ioScope {
            val body = AppConfigRequestModel()
            val apiResponse = retrofit { main.getAppConfig(body) }
            val apiResult = apiResponse.data
            if (apiResponse.successful && apiResult != null) {
                shareAppUrl = apiResult.share_copy
                if (apiResult.Shield_app_bundle != "0") {
                    VpnWhiteList.vpnList.clear()
                    val parts = apiResult.Shield_app_bundle.split("&")
                    parts.forEach { VpnWhiteList.vpnList.add(it) }
                }
                try {
                    showOpenAd = apiResult.openAd.toInt() == 1
                    showMainNativeAd = apiResult.mainNativeAd.toInt() == 1
                    showConnectedInsertAd = apiResult.connectedInsertAd.toInt() == 1
                    showConnectedNativeAd = apiResult.connectedNativeAd.toInt() == 1
                    showDisconnectInsertAd = apiResult.disconnectInsertAd.toInt() == 1
                    showDisconnectNativeAd = apiResult.disconnectNativeAd.toInt() == 1
                    showVpnListNativeAd = apiResult.vpnListNativeAd.toInt() == 1
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}