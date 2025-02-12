package com.aura.data.api

import com.aura.data.model.account.AccountModelResponse
import com.aura.data.model.login.LoginModelRequest
import com.aura.data.model.login.LoginModelResponse
import com.aura.data.model.transfer.TransferModelRequest
import com.aura.data.model.transfer.TransferModelResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    /**
     * User connection
     */
    @POST("login")
    suspend fun login(@Body loginModelRequest: LoginModelRequest): LoginModelResponse

    /**
     * POST transfer endpoint
     */
    @POST("transfer")
    suspend fun transfer(@Body transferModelRequest: TransferModelRequest): TransferModelResponse

    /**
     * GET accounts by id endpoint
     */
    @GET("accounts/{id}")
    suspend fun getAccounts(@Path("id") accountId: String): List<AccountModelResponse>
}