package com.github.mstavares.cm.fichas

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.objecthunter.exp4j.ExpressionBuilder

object Calculator {

    var expression: String = "0"
        private set
    private val history = mutableListOf<Operation>()

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
            Thread.sleep(10 * 1000)
            expression = if (history.size > 0) history[history.size - 1].expression else expression
            onFinished(expression)
        }
    }

    fun deleteOperation(uuid: String, onSuccess: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            Thread.sleep(10 * 1000)
            val operation = history.find { it.uuid == uuid }
            history.remove(operation)
            onSuccess()
        }
    }

    fun performOperation(onSaved: () -> Unit) {
        val expressionBuilder = ExpressionBuilder(expression).build()
        val result = expressionBuilder.evaluate()
        val operation = Operation(expression = expression, result = result)
        expression = result.toString()
        CoroutineScope(Dispatchers.IO).launch {
            addToHistory(operation)
            onSaved()
        }
    }

    fun getHistory(onFinished: (List<Operation>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            Thread.sleep(10 * 1000)
            onFinished(history.toList())
        }
    }

    private fun addToHistory(operation: Operation) {
        Thread.sleep(10 * 1000)
        history.add(operation)
    }

}
