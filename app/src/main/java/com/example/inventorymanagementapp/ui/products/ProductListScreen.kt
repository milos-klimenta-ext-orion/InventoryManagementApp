package com.example.inventorymanagementapp.ui.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.inventorymanagementapp.domain.model.Product
import com.example.inventorymanagementapp.ui.StockScannerPermission
import com.example.inventorymanagementapp.ui.camera.BarcodeScannerDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onProductClicked: (productId: Long) -> Unit,
    onAddProductClicked: () -> Unit,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showScanner by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Barcode scanner dialog
    if (showScanner) {
        StockScannerPermission {
            BarcodeScannerDialog(
                onDismiss = { showScanner = false },
                onBarcodeScanned = { barcode ->
                    viewModel.setBarcodeQuery(barcode)
                    showScanner = false
                }
            )
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = { Text("Products") }, actions = {
                    IconButton(onClick = {viewModel.exportInventory(context)}) {
                        Icon(Icons.Default.FileDownload, contentDescription = "Export CSV")
                    }
                })
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = viewModel::setSearchQuery,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    label = { Text("Search") },
                    trailingIcon = {
                        IconButton(onClick = { showScanner = true }) {
                            Icon(Icons.Default.QrCodeScanner, contentDescription = "Scan Barcode")
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProductClicked) {
                Icon(Icons.Default.Add, contentDescription = "Add Product")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.products) { product ->
                ProductItem(
                    product = product,
                    onClick = { onProductClicked(product.id)})
            }
        }
    }
}

@Composable
private fun ProductItem(
    product: Product,
    onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(product.name, style = MaterialTheme.typography.titleMedium)
            Text("Stock ${product.currentStock}")
        }
    }
}

@Preview
@Composable
private fun PreviewProductItem() {
    ProductItem(Product(
        name = "Milk",
        description = "1L Milk",
        price = 1.2,
        category = "Dairy",
        barcode = "123456",
        supplierId = 1,
        currentStock = 3,
        minimumStock = 5
    )) { }
}