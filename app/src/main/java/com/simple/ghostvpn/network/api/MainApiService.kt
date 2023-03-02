package com.simple.ghostvpn.network.api

import com.simple.ghostvpn.data.ApiResponse
import com.simple.ghostvpn.data.AppConfigModel
import com.simple.ghostvpn.data.body.AppConfigRequestModel
import retrofit2.http.Body
import retrofit2.http.POST

interface MainApiService {

    @POST("/v1/app/mobile/conf")
    suspend fun getAppConfig(@Body appConfigBody: AppConfigRequestModel): ApiResponse<AppConfigModel>
}