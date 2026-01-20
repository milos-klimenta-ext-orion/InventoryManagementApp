package com.example.inventorymanagementapp.ui.transaction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailsScreen(
    viewModel: TransactionDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val transaction = uiState.transaction
    val dateString = transaction?.createdAt?.let {
        SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(it))
    } ?: "Unknown"

    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (uiState.transaction == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Transaction not found")
        }
    } else {
        val transaction = uiState.transaction
        Scaffold(
            topBar = { TopAppBar({ Text("Transaction Details") }) }
        ) { padding ->
            Column(modifier = Modifier
                .padding(16.dp)
                .padding(padding)) {
                Text("Type: ${transaction?.type}", style = MaterialTheme.typography.headlineSmall)
                Text("Quantity: ${transaction?.quantity}")
                Text("Notes: ${transaction?.notes ?: "None"}")
                Text("Date: $dateString")
            }
        }
    }
}
