package com.github.mstavares.cm.fichas

import net.objecthunter.exp4j.ExpressionBuilder

class Calculator {

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

    fun getLastOperation(): String {
        display = if(history.size > 0) history[history.size - 1].expression else display
        return display
    }

    // TODO
    fun deleteOperation() {

    }

    fun performOperation(): Double {
        val expressionBuilder = ExpressionBuilder(display).build()
        val result = expressionBuilder.evaluate()
        history.add(Operation(display, result))
        display = result.toString()
        return result
    }

}
