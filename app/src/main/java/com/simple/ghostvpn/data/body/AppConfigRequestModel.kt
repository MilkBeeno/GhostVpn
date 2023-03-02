package com.simple.ghostvpn.data.body

import com.simple.ghostvpn.BuildConfig

data class AppConfigRequestModel(
    var appId: String = BuildConfig.APP_ID,
    var channel: String = BuildConfig.APP_CHANNEL,
    var pkgVersion: String = BuildConfig.APP_VERSION
)