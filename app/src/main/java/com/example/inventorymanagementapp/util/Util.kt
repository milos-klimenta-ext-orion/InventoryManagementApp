package com.example.inventorymanagementapp.util

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import com.example.inventorymanagementapp.domain.model.InventoryTransaction
import com.example.inventorymanagementapp.domain.model.Product
import java.io.File
import java.text.SimpleDateFormat

object Util {

    fun generateInventoryCsv(products: List<Product>): String {
        val header = "ID,Name,Category,Stock,MinStock,Price,Barcode,Supplier\n"
        val body = products.joinToString("\n") { p ->
            "${p.id},${p.name},${p.category},${p.currentStock},${p.minimumStock},${p.price},${p.barcode},${p.supplierId}"
        }
        return header + body
    }

    fun generateTransactionCsv(transactions: List<InventoryTransaction>): String {
        // CSV header
        val header = "ID,Date,Type,ProductID,Quantity,Notes,CreatedAt\n"

        // CSV body
        val body = transactions.joinToString("\n") { t ->
            val dateStr = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(java.util.Date(t.date))
            val createdAtStr = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(java.util.Date(t.createdAt))
            "${t.id},$dateStr,${t.type},${t.productId},${t.quantity},${t.notes.orEmpty()},$createdAtStr"
        }

        return header + body
    }


    fun saveCsvToFile(context: Context, csvContent: String, fileName: String): File? {
        return try {
            // Save in app-specific external storage (no extra permissions needed)
            val directory = context.getExternalFilesDir(null)
            if (directory != null && !directory.exists()) {
                directory.mkdirs()
            }

            val file = File(directory, fileName)
            file.writeText(csvContent)


            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val uri = context.contentResolver.insert(MediaStore.Files.getContentUri("external"), values)
            uri?.let {
                context.contentResolver.openOutputStream(it)?.use { stream ->
                    stream.write(csvContent.toByteArray())
                }
            }


            file // return the saved file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}