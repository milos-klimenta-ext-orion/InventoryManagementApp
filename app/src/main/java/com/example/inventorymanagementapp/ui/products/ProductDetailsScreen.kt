package com.example.inventorymanagementapp.ui.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    onEditClicked: (productId: Long) -> Unit,
    viewModel: ProductDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        uiState.product == null -> {
            Text("Product not found")
        }
        else -> {
            val product = uiState.product

            Scaffold(
                topBar = { TopAppBar(title = { Text("Product Details") }) },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { onEditClicked(product!!.id) }
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Product")
                    }
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(product!!.name, style = MaterialTheme.typography.headlineSmall)
                    Text("Category: ${product.category}")
                    Text("Price: ${product.price}")
                    Text("Stock: ${product.currentStock}")
                    Text("Minimum stock: ${product.minimumStock}")
                    product.description?.let {
                        Text("Description: $it")
                    }
                }
            }
        }
    }
}