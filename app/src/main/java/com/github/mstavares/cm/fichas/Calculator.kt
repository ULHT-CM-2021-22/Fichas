package com.github.mstavares.cm.fichas

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.objecthunter.exp4j.ExpressionBuilder
import java.util.*

class Calculator(private val dao: OperationDao) {

    var expression: String = "0"
        private set

    fun insertSymbol(symbol: String): String {
        expression = if(expression == "0") symbol else "$expression$symbol"
        return expression
    }

    fun clear(): String {
        expression = "0"
        return expression
    }

    fun deleteLastSymbol(): String {
        expression = if(expression.length > 1) expression.dropLast(1) else "0"
        return expression
    }

    fun getLastOperation(onFinished: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            getHistory { history ->
                expression = if (history.isNotEmpty()) history[history.size - 1].expression else expression
                onFinished(expression)
            }
        }
    }

    fun deleteOperation(uuid: String, onSuccess: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = dao.delete(uuid)
            // se == 1 eliminou o registo, se for 0 não encontrou o registo a eliminar
            if(result == 1) onSuccess()
        }
    }

    fun performOperation(onFinished: () -> Unit) {
        val expressionBuilder = ExpressionBuilder(expression).build()
        val result = expressionBuilder.evaluate()
        val operation = Operation(expression = expression, result = result, timestamp = Date().time)
        expression = result.toString()
        CoroutineScope(Dispatchers.IO).launch {
            dao.insert(operation)
            onFinished()
        }
    }

    fun getHistory(onFinished: (List<Operation>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            onFinished(dao.getAll())
        }
    }

}
