package com.example.inventorymanagementapp.ui.supplier

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun SupplierDetailsScreen(
    onBack: () -> Unit,
    viewModel: SupplierDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        uiState.supplier == null -> {
            Text("Supplier not found")
        }

        else -> {
            val supplier = uiState.supplier!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(supplier.name, style = MaterialTheme.typography.headlineSmall)
                Text("Contact: ${supplier.contactPerson ?: "-"}")
                Text("Phone: ${supplier.phone ?: "-"}")
                Text("Email: ${supplier.email ?: "-"}")
                Text("Address: ${supplier.address ?: "-"}")
            }
        }
    }
}
