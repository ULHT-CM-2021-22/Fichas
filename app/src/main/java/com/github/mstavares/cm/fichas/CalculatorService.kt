package com.github.mstavares.cm.fichas

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class GetOperationsResponse(val uuid: String, val expression: String, val result: Double, val timestamp: Long)
data class PostOperationRequest(val expression: String, val result: Double, val timestamp: Long)

interface CalculatorService {

    @POST("operations")
    suspend fun insert(@Body body: PostOperationRequest): Call<String>

    @GET("operations")
    suspend fun getAll(): Call<List<GetOperationsResponse>>

}