package com.example.inventorymanagementapp.ui.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.inventorymanagementapp.ui.StockScannerPermission
import com.example.inventorymanagementapp.ui.camera.BarcodeScannerDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductEditScreen(
    onFinished: () -> Unit,
    viewModel: ProductEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showScanner by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onFinished()
    }

    if (showScanner) {
        StockScannerPermission {
            BarcodeScannerDialog(
                onDismiss = { showScanner = false },
                onBarcodeScanned = { barcode ->
                    viewModel.onBarcodeChange(barcode)
                    showScanner = false
                }
            )
        }
    }
    Scaffold(
        topBar = { TopAppBar(title = { Text(if (uiState.isEditMode) "Update Product" else "Add Product") }) }
        ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(uiState.name, viewModel::onNameChange, label = { Text("Name") })
            OutlinedTextField(uiState.price, viewModel::onPriceChange, label = { Text("Price") })
            OutlinedTextField(
                uiState.stock,
                viewModel::onCurrentStockChange,
                label = { Text("Stock") })
            OutlinedTextField(
                uiState.category,
                viewModel::onCategoryChange,
                label = { Text("Category") })
            OutlinedTextField(
                uiState.minStock,
                viewModel::onMinStockChange,
                label = { Text("Minimum Stock") })
            OutlinedTextField(
                value = uiState.barcode,
                onValueChange = viewModel::onBarcodeChange,
                label = { Text("Barcode") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { showScanner = true }) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = "Scan Barcode"
                        )
                    }
                }
            )
            OutlinedTextField(
                uiState.description,
                viewModel::onDescriptionChange,
                label = { Text("Description") })

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = viewModel::saveProduct
            ) {
                Text(if (uiState.isEditMode) "Update Product" else "Add Product")
            }
        }
    }
}