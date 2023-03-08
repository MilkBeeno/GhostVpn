package com.simple.ghostvpn.network.api

import com.simple.ghostvpn.data.ApiResponse
import com.simple.ghostvpn.data.VpnListModel
import com.simple.ghostvpn.data.VpnDetailModel
import com.simple.ghostvpn.network.reteceptor.ApiHeadInterceptor.Companion.PARAMS_IN_PATH
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface VpnApiService {

    @FormUrlEncoded
    @Headers("${PARAMS_IN_PATH}:true")
    @POST("/app/api/v1/c03/c0001")
    suspend fun getVpnInfo(@Field("id") id: Long): ApiResponse<VpnDetailModel>

    @Headers("${PARAMS_IN_PATH}:true")
    @POST("/app/api/v1/c03/c0001")
    suspend fun getVpnListInfo(): ApiResponse<MutableList<VpnListModel>>
}