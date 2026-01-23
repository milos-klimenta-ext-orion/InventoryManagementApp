# Inventory Management App

  This is a Kotlin Android application for managing inventory, products, suppliers, and transactions. It uses Jetpack Compose for the UI, Room for local database storage, and Hilt for dependency injection.

# Features
1. Product Management

    Add, edit, and delete products.

    Track current stock and minimum stock levels.

    Search products by name or barcode.

    Barcode scanning support with camera integration.

2. Supplier Management

    Add, edit, and delete suppliers.

    View supplier list and details.

    Search suppliers by name.

3. Stock and Transaction Management

    Record inventory transactions: Restock and Sale.

    Update product stock automatically when transactions occur.

    View transaction history filtered by type and date range.

    Export transactions or inventory reports as CSV files.

    Data persistence using Room.

4. Dashboard

    Shows products that are low in stock.

    Displays recent transactions.

5. Authentication

    Simple login with username/password.

    Persisted login state using DataStore.

    Navigate to dashboard after successful login.

# Architecture

  MVVM Pattern with ViewModels for UI logic.

  Repository Pattern for data access.

  Room as local database.

  Jetpack Compose for UI.

  Hilt for dependency injection.

# Testing

  Unit tests for all ViewModels and Repositories.

  Fake repositories and DAOs used to isolate tests.

  Compose UI tests for screens like Product List.

  Hilt test setup with HiltAndroidRule and custom test modules.

# Data Models

  Product: id, name, description, price, category, barcode, supplier, current stock, minimum stock.

  Supplier: id, name, contact, phone, email, address.

  InventoryTransaction: id, product, quantity, type (RESTOCK / SALE), notes, timestamp.

# How to Run

  Clone the repository.

  Open in Android Studio.

  Build and run on an emulator or device.

  Log in with admin/admin to test login features.

  Navigate to Products, Suppliers, or Transactions screens.

# Notes

  Transaction types are stored in Room using TypeConverters.

  All UI state is reactive using StateFlow.

  Screens are fully Compose-based and responsive.

# Planned / Future Tasks

  1. Add UI Tests for all screens to ensure correctness.

  2. Add Charts in Stock Management screen for better visualization.

  3. Implement Notifications for low stock items.

  4. Improve UI design: current UI is basic and intended for testing only.

# Known Issues
  
   1. Issue with dialog for runtime permission access for camera!
