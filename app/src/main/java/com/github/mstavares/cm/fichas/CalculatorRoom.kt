package com.github.mstavares.cm.fichas

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class CalculatorRoom(private val dao: OperationDao) : Calculator() {

    override fun refreshOperations(operations: List<OperationUi>) {
        CoroutineScope(Dispatchers.IO).launch {
            val history = operations.map { OperationRoom(it.uuid, it.expression, it.result, it.timestamp) }
            dao.deleteAll()
            dao.insertAll(history)
        }
    }

    override fun performOperation(onFinished: () -> Unit) {
        val currentExpression = expression
        super.performOperation {
            val operation = OperationRoom(expression = currentExpression, result = expression.toDouble(), timestamp = Date().time)
            CoroutineScope(Dispatchers.IO).launch { dao.insert(operation) }
            onFinished()
        }
    }

    override fun getLastOperation(onFinished: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            // Também pode ser realizado com uma query com o Room, ver getLastOperation do OperationDao
            // o método do dao é o mais correto, este serve apenas para apresentar outra solução :)
            getHistory { onFinished(it.last().expression) }
        }
    }

    override fun deleteOperation(uuid: String, onSuccess: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = dao.delete(uuid)
            // se == 1 eliminou o registo, se for 0 não encontrou o registo a eliminar
            if(result == 1) onSuccess()
        }
    }

    override fun getHistory(onFinished: (List<OperationUi>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val operations = dao.getAll()
            onFinished(operations.map { OperationUi(it.uuid, it.expression, it.result, it.timestamp) })
        }
    }

}