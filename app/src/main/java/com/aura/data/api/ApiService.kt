package com.aura.data.api

import com.aura.data.model.login.LoginModelRequest
import com.aura.data.model.login.LoginModelResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    /**
     *User connection
     */
    @POST("login")
    suspend fun login(@Body loginModelRequest: LoginModelRequest): LoginModelResponse

    /**
     *
     */
}