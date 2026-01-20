package com.example.inventorymanagementapp.navigation

sealed class Screen(val route: String) {

    object Login: Screen("login")
    object Dashboard: Screen("dashboard")
    object ProductList: Screen("products")

    object ProductDetails : Screen("product_detail/{productId}") {
        fun createRoute(productId: Long) =
            "product_detail/$productId"
    }


    object ProductEdit: Screen("product_edit?productId={productId}") {
        fun createRoute(productId: Long? = null) =
            productId?.let { "product_edit?productId=$it" } ?: "product_edit?productId=-1"
    }

    object SupplierList: Screen("suppliers")

    object SupplierDetails: Screen("supplier_detail/{supplierId}") {
        fun createRoute(supplierId: Long) =
            "supplier_details/$supplierId"
    }

    object TransactionList: Screen("transactions")

    object TransactionDetails: Screen("transaction_details/{transactionId}") {
        fun createRoute(transactionId: Long) =
            "transaction_details/$transactionId"
    }
    object StockManagement: Screen("stock_management")
}