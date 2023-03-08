package com.simple.ghostvpn.data

import com.google.gson.annotations.SerializedName

data class AppConfigModel(
    @SerializedName("vvvdc321")
    var share_copy: String = "",
    @SerializedName("vvvdc322")
    var Shield_app_bundle: String = "",
    @SerializedName("vvvdc323")
    var mainNativeAd: String = "",
    @SerializedName("vvvdc324")
    var openAd: String = "",
    @SerializedName("vvvdc325")
    var connectedInsertAd: String = "",
    @SerializedName("vvvdc326")
    var connectedNativeAd: String = "",
    @SerializedName("vvvdc327")
    var disconnectInsertAd: String = "",
    @SerializedName("vvvdc328")
    var disconnectNativeAd: String = "",
    @SerializedName("vvvdc329")
    var vpnListNativeAd: String = "",
)