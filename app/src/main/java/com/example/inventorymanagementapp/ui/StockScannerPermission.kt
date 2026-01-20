package com.example.inventorymanagementapp.ui

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.inventorymanagementapp.util.findActivity

@Composable
fun StockScannerPermission(
    onPermissionGranted: @Composable () -> Unit
) {
    val context = LocalContext.current
    val permission = android.Manifest.permission.CAMERA
    var hasPermission by remember { mutableStateOf(
        ContextCompat.checkSelfPermission(context, permission) == android.content.pm.PackageManager.PERMISSION_GRANTED
    )}

    if (!hasPermission) {
        val activity = context.findActivity()
        LaunchedEffect(Unit) {
            activity.let {
                ActivityCompat.requestPermissions(it, arrayOf(permission), 123)
            }
        }

        // Show fallback UI while requesting
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Camera permission required to scan barcode")
        }
    } else {
        // ✅ Permission granted → render the scanner UI
        onPermissionGranted()
    }
}
