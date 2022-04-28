package com.github.mstavares.cm.fichas

import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel()  {

    private val model = CalculatorRepository.getInstance()

    fun getDisplayValue(): String {
        return model.getExpression()
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

    fun onGetHistory(onFinished: (List<OperationUi>) -> Unit) {
        model.getHistory(onFinished)
    }

    fun onDeleteOperation(uuid: String, onSuccess: () -> Unit) {
        model.deleteOperation(uuid, onSuccess)
    }

    fun onClickEquals(onFinished: () -> Unit) {
        model.performOperation(onFinished)
    }

}