package com.example.weatherapp.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import android.provider.Settings


fun showPopupRequirePermission(activity: Activity) {
    val dialogBuilder = AlertDialog.Builder(activity)

    dialogBuilder.setMessage("You should enable permission")
        .setCancelable(false)
        .setPositiveButton("Ok") { _, _ ->
            goToAppPermission(activity)
        }
        .setNegativeButton("Cancel") { dialog, _ ->
            activity.finish()
        }

    val alert = dialogBuilder.create()
    alert.setTitle("Permission")
    alert.show()
}

fun goToAppPermission(activity: Activity) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", activity.packageName, null)
    intent.data = uri
    activity.startActivity(intent)
}

fun checkForPermission(activity: Activity, permission: String, requestMultiplePermissionLauncher: ()->Unit, action: () -> Unit) {
    when {
        ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED -> {
            action.invoke()
        }
        shouldShowRequestPermissionRationale(activity, permission) -> {

            showPopupRequirePermission(activity)
        }
        else -> {
            requestMultiplePermissionLauncher.invoke()
        }
    }
}