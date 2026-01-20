package com.example.inventorymanagementapp.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.inventorymanagementapp.domain.model.InventoryTransaction
import com.example.inventorymanagementapp.domain.model.Product
import com.example.inventorymanagementapp.domain.model.TransactionType
import com.example.inventorymanagementapp.ui.login.LoginViewModel

@ExperimentalMaterial3Api
@Composable
fun DashboardScreen(
    onNavigateToProducts: () -> Unit,
    onNavigateToTransaction: () -> Unit,
    onLogoutClicked: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            DashboardLoading()
        }

        uiState.isEmpty -> {
            DashboardEmpty()
        }

        else -> {
            DashboardContent(
                onNavigateToProducts,
                onNavigateToTransaction,
                {
                    onLogoutClicked()
                    loginViewModel.logout()
                },
                uiState
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardContent(
    onNavigateToProducts: () -> Unit,
    onNavigateToTransaction: () -> Unit,
    onLogoutClicked: () -> Unit,
    uiState: DashboardUiState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                actions = {
                    IconButton(onClick = onLogoutClicked) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            item {
                DashboardCard(
                    title = "LowStockItem",
                    actionText = "View All",
                    badgeCount = uiState.lowStockProducts.size,
                    onActionClicked = onNavigateToProducts
                ) {
                    uiState.lowStockProducts
                        .take(3)
                        .forEach { product ->
                            LowStockItem(product)
                        }
                }
            }

            item {
                DashboardCard(
                    title = "Transactions",
                    actionText = "View All",
                    onActionClicked = onNavigateToTransaction
                ) {
                    uiState.recentTransactions
                        .take(3)
                        .forEach { transaction ->
                            TransactionItem(transaction)
                        }
                }
            }
        }
    }
}

@Composable
private fun DashboardCard(
    title: String,
    actionText: String,
    onActionClicked: () -> Unit,
    badgeCount: Int? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (badgeCount != null && badgeCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Badge(
                            containerColor = MaterialTheme.colorScheme.error
                        ) {
                            Text(
                                badgeCount.toString(),
                                color = MaterialTheme.colorScheme.onError
                            )
                        }
                    }
                }

                TextButton(onClick = onActionClicked) {
                    Text(actionText)
                }
            }

            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            content()
        }
    }
}



@Composable
private fun DashboardSection(
    title: String,
    actionText: String,
    onActionClicked: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        TextButton(onClick = onActionClicked) { Text(actionText) }
    }
}

@Composable
private fun LowStockItem(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(.16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(product.name, style = MaterialTheme.typography.bodyLarge)
                Text(
                    "Stock: ${product.currentStock}",
                    color = MaterialTheme.colorScheme.error
                )
            }
            Icon(
                modifier = Modifier.padding(16.dp),
                imageVector = Icons.Default.Warning,
                contentDescription = "Low stock",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun TransactionItem(transaction: InventoryTransaction) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = transaction.type.name,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Quantity : ${transaction.quantity}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun DashboardLoading() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(5) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {  }
        }
    }
}

@Composable
private fun DashboardEmpty() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("No data yet. Add products to get started.")
    }
}

@Preview
@Composable
private fun PreviewLowStockItem() {
    LowStockItem(Product(
        name = "Milk",
        description = "1L Milk",
        price = 1.2,
        category = "Dairy",
        barcode = "123456",
        supplierId = 1,
        currentStock = 3,
        minimumStock = 5
    ))
}

@Preview
@Composable
private fun PreviewTranslationItem() {
    TransactionItem(InventoryTransaction(
        type = TransactionType.RESTOCK,
        productId = 1,
        quantity = 20,
        notes = "Initial stock"
    ))
}

@Preview
@Composable
private fun PreviewForDashboardCard() {
    DashboardCard(
        title = "Low Stock Item",
        actionText = "View All",
        onActionClicked = {},
        badgeCount = 15
    ) { }
}
