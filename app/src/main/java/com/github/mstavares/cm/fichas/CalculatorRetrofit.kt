package com.github.mstavares.cm.fichas

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*

class CalculatorRetrofit(retrofit: Retrofit) : Calculator() {

    private val TAG = CalculatorRetrofit::class.java.simpleName
    private val service = retrofit.create(CalculatorService::class.java)

    override fun performOperation(onFinished: () -> Unit) {
        val currentExpression = expression
        super.performOperation {
            CoroutineScope(Dispatchers.IO).launch {
                val request = PostOperationRequest(currentExpression, expression.toDouble(), Date().time)
                service.insert(request).enqueue(object: Callback<String> {

                    override fun onResponse(call: Call<String>, response: Response<String>) {

                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.e(TAG, t.toString())
                    }

                })
            }
            onFinished()
        }
    }

    override fun getLastOperation(onFinished: (String) -> Unit) {
        //TODO("Not yet implemented")
    }

    override fun deleteOperation(uuid: String, onSuccess: () -> Unit) {
        //TODO("Not yet implemented")
    }

    override fun getHistory(onFinished: (List<OperationUi>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            service.getAll().enqueue(object : Callback<List<GetOperationsResponse>> {

                override fun onResponse(call: Call<List<GetOperationsResponse>>, response: Response<List<GetOperationsResponse>>) {
                    response.body()?.let { operations ->
                        onFinished(operations.map { OperationUi(it.uuid, it.expression, it.result, it.timestamp) })
                    }
                }

                override fun onFailure(call: Call<List<GetOperationsResponse>>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }

            })
        }
    }
}