package com.github.mstavares.cm.fichas

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mstavares.cm.fichas.databinding.FragmentCalculatorBinding
import com.github.mstavares.cm.fichas.databinding.FragmentHistoryBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryFragment : Fragment() {

    private lateinit var model: Calculator
    private var adapter = HistoryAdapter(onClick = ::onOperationClick, onLongClick = ::onOperationLongClick)
    private lateinit var binding: FragmentHistoryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        model = Calculator(CalculatorDatabase.getInstance(requireContext()).operationDao())
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.history)
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        binding = FragmentHistoryBinding.bind(view)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = adapter
        model.getHistory { updateHistory(it) }
    }

    private fun onOperationClick(operation: OperationUi) {
        NavigationManager.goToOperationDetail(parentFragmentManager, operation)
    }

    private fun onOperationLongClick(operation: OperationUi): Boolean {
        Toast.makeText(requireContext(), getString(R.string.deleting), Toast.LENGTH_SHORT).show()
        model.deleteOperation(operation.uuid) { model.getHistory { updateHistory(it) } }
        return false
    }

    private fun updateHistory(operations: List<Operation>) {
        val history = operations.map { OperationUi(it.uuid, it.expression, it.result, it.timestamp) }
        CoroutineScope(Dispatchers.Main).launch {
            showHistory(history.isNotEmpty())
            adapter.updateItems(history)
        }
    }

    private fun showHistory(show: Boolean) {
        if (show) {
            binding.rvHistory.visibility = View.VISIBLE
            binding.textNoHistoryAvailable.visibility = View.GONE
        } else {
            binding.rvHistory.visibility = View.GONE
            binding.textNoHistoryAvailable.visibility = View.VISIBLE
        }
    }

}