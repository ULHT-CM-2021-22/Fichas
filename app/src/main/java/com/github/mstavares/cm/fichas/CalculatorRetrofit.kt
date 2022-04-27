package com.github.mstavares.cm.fichas

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.*
import java.lang.Exception
import java.util.*

class CalculatorRetrofit(retrofit: Retrofit) : Calculator() {

    private val TAG = CalculatorRetrofit::class.java.simpleName
    private val service = retrofit.create(CalculatorService::class.java)

    override fun performOperation(onFinished: () -> Unit) {
        val currentExpression = expression
        super.performOperation {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val body = PostOperationRequest(currentExpression, expression.toDouble(), Date().time)
                    val result = service.insert(body)
                    Log.i(TAG, result.toString())
                } catch (ex: HttpException) {
                    Log.e(TAG, ex.message())
                }
            }
            onFinished()
        }
    }

    override fun insertOperations(operations: List<OperationUi>, onFinished: (List<OperationUi>) -> Unit) {
        throw Exception("Not implemented on web service")
    }

    override fun getLastOperation(onFinished: (String) -> Unit) {
        getHistory { history ->
            onFinished(history.sortedByDescending { it.timestamp }.first().expression)
        }
    }

    override fun deleteOperation(uuid: String, onFinished: () -> Unit) {
        //TODO("Not yet implemented")
    }

    override fun deleteAllOperations(onFinished: () -> Unit) {
        //TODO("Not yet implemented")
    }

    override fun getHistory(onFinished: (List<OperationUi>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val operations = service.getAll()
                onFinished(operations.map { OperationUi(it.uuid, it.expression, it.result, it.timestamp) })
            } catch (ex: HttpException) {
                Log.e(TAG, ex.message())
            }
        }
    }

}