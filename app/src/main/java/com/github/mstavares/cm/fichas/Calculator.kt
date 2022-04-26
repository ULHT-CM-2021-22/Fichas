package com.github.mstavares.cm.fichas

import net.objecthunter.exp4j.ExpressionBuilder

abstract class Calculator {

    var expression: String = "0"

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

    open fun performOperation(onFinished: () -> Unit) {
        val expressionBuilder = ExpressionBuilder(expression).build()
        val result = expressionBuilder.evaluate()
        expression = result.toString()
        onFinished()
    }

    abstract fun refreshOperations(operations: List<OperationUi>)
    abstract fun getLastOperation(onFinished: (String) -> Unit)
    abstract fun deleteOperation(uuid: String, onSuccess: () -> Unit)
    abstract fun getHistory(onFinished: (List<OperationUi>) -> Unit)

}
