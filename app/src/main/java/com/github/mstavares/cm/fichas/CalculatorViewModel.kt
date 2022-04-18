package com.github.mstavares.cm.fichas

import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    private val model = Calculator

    fun getDisplayValue(): String {
        return model.display
    }

    fun onClickSymbol(symbol: String): String {
        return model.insertSymbol(symbol)
    }

    fun onClickClear(): String {
        return model.clear()
    }

    fun onClickBackspace(): String {
        return model.deleteLastSymbol()
    }

    fun onClickGetLastOperation(onFinished: (String) -> Unit) {
        model.getLastOperation(onFinished)
    }

    fun onClickEquals(onSaved: () -> Unit): String {
        val result = model.performOperation(onSaved)
        return if(result % 1 == 0.0) result.toLong().toString() else result.toString()
    }

}