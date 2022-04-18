package com.github.mstavares.cm.fichas

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.objecthunter.exp4j.ExpressionBuilder

object Calculator {

    var display: String = "0"
        private set
    private val history = mutableListOf<Operation>()

    fun insertSymbol(symbol: String): String {
        display = if(display == "0") symbol else "$display$symbol"
        return display
    }

    fun clear(): String {
        display = "0"
        return display
    }

    fun deleteLastSymbol(): String {
        display = if(display.length > 1) display.dropLast(1) else "0"
        return display
    }

    fun getLastOperation(onFinished: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            Thread.sleep(10 * 1000)
            display = if (history.size > 0) history[history.size - 1].expression else display
            onFinished(display)
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

    fun performOperation(onSaved: () -> Unit): Double {
        val expressionBuilder = ExpressionBuilder(display).build()
        val result = expressionBuilder.evaluate()
        val operation = Operation(expression = display, result = result)
        CoroutineScope(Dispatchers.IO).launch {
            addToHistory(operation)
            onSaved()
        }
        display = result.toString()
        return result
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
