package com.simple.ghostvpn.network

import com.simple.ghostvpn.network.host.MainHost
import com.simple.ghostvpn.network.host.VpnHost
import com.simple.ghostvpn.network.reteceptor.ApiHeadInterceptor
import com.simple.ghostvpn.network.reteceptor.ApiLogerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object OkhttpClient {
    private var mainRetrofit: Retrofit? = null
    private val mainClient: OkHttpClient
        get() {
            return OkHttpClient.Builder()
                .callTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(8, TimeUnit.SECONDS)
                .writeTimeout(8, TimeUnit.SECONDS)
                .addInterceptor(ApiLogerInterceptor())
                .build()
        }
    private var vpnRetrofit: Retrofit? = null
    private val vpnClient: OkHttpClient
        get() {
            return OkHttpClient.Builder()
                .callTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(8, TimeUnit.SECONDS)
                .writeTimeout(8, TimeUnit.SECONDS)
                .addInterceptor(ApiHeadInterceptor())
                .addInterceptor(ApiLogerInterceptor())
                .build()
        }

    fun main(): Retrofit {
        if (mainRetrofit == null)
            mainRetrofit = Retrofit.Builder()
                .baseUrl(MainHost().realUrl)
                .client(mainClient)
                .addConverterFactory(
                    GsonConverterFactory.create(JsonFormat.gson)
                )
                .build()
        return checkNotNull(mainRetrofit)
    }

    fun vpn(): Retrofit {
        if (vpnRetrofit == null)
            vpnRetrofit = Retrofit.Builder()
                .baseUrl(VpnHost().realUrl)
                .client(vpnClient)
                .addConverterFactory(
                    GsonConverterFactory.create(JsonFormat.gson)
                )
                .build()
        return checkNotNull(vpnRetrofit)
    }
}