package com.github.mstavares.cm.fichas

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

data class GetOperationsResponse(val uuid: String, val expression: String, val result: Double, val timestamp: Long)
data class PostOperationRequest(val expression: String, val result: Double, val timestamp: Long)

interface CalculatorService {

    @Headers("apikey: 8270435acfead39ccb03e8aafbf37c49359dfbbcac4ef4769ae82c9531da0e17")
    @POST("operations")
    fun insert(@Body body: PostOperationRequest): Call<String>

    @Headers("apikey: 8270435acfead39ccb03e8aafbf37c49359dfbbcac4ef4769ae82c9531da0e17")
    @GET("operations")
    fun getAll(): Call<List<GetOperationsResponse>>

}