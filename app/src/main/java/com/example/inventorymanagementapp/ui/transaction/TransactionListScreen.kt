package com.example.inventorymanagementapp.ui.transaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.inventorymanagementapp.domain.model.InventoryTransaction
import com.example.inventorymanagementapp.domain.model.TransactionType
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    onTransactionClicked: (transactionId: Long) -> Unit,
    viewModel: TransactionListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var selectedType by remember { mutableStateOf<TransactionType?>(null) }
    var startDate by remember { mutableStateOf<Long?>(null) }
    var endDate by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = { TopAppBar({ Text("Transactions") }, actions = {
            IconButton(onClick = {viewModel.exportTransactionCSV(context)}) {
                Icon(Icons.Default.FileDownload, contentDescription = "Export CSV")
            }
        }) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            FilterSection(
                startDate = startDate,
                endDate = endDate,
                selectedType = selectedType,
                onStartDateSelected = {
                    startDate = it
                    viewModel.setStartDate(it)
                },
                onEndDateSelected = {
                    endDate = it
                    viewModel.setEndDate(it)
                },
                onTypeSelected = {
                    selectedType = it
                    viewModel.setTransactionType(it)
                }
            )

            // --- Content ---
            when {
                uiState.isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                uiState.transactions.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No transactions found")
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.transactions) { transaction ->
                            TransactionItem(
                                transaction,
                                onClick = { onTransactionClicked(transaction.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionItem(transaction: InventoryTransaction, onClick: () -> Unit) {
    Card(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.Companion.padding(16.dp)) {
            Text("Type: ${transaction.type.name}", style = MaterialTheme.typography.bodyLarge)
            Text("Quantity: ${transaction.quantity}", style = MaterialTheme.typography.bodyMedium)
            Text(text = SimpleDateFormat("dd MM yyyy").format(transaction.date))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(
    selectedDate: Long?,
    onDateSelected: (Long) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Button(onClick = { showDialog = true }) {
        Text(
            text = selectedDate?.let {
                SimpleDateFormat("dd MMM yyyy").format(it)
            } ?: "Pick a date"
        )
    }

    if (showDialog) {

        val state = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
        )
        // This is the Compose Material3 dialog
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    // Get selected date from state below
                    state.selectedDateMillis?.let { onDateSelected(it) }
                    showDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            },
            content = {
                DatePicker(
                    state = state,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }
}

@Composable
private fun FilterSection(
    startDate: Long?,
    endDate: Long?,
    selectedType: TransactionType?,
    onStartDateSelected: (Long?) -> Unit,
    onEndDateSelected: (Long?) -> Unit,
    onTypeSelected: (TransactionType?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Date filters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MyDatePicker(
                selectedDate = startDate,
                onDateSelected = onStartDateSelected
            )
            MyDatePicker(
                selectedDate = endDate,
                onDateSelected = onEndDateSelected
            )
        }

        // Type filters
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TransactionTypeChip(
                label = "All",
                selected = selectedType == null,
                onClick = { onTypeSelected(null) }
            )
            TransactionTypeChip(
                label = "Sale",
                selected = selectedType == TransactionType.SALE,
                onClick = { onTypeSelected(TransactionType.SALE) }
            )
            TransactionTypeChip(
                label = "Restock",
                selected = selectedType == TransactionType.RESTOCK,
                onClick = { onTypeSelected(TransactionType.RESTOCK) }
            )
        }
    }
}

@Composable
private fun TransactionTypeChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) }
    )
}




