package com.example.weatherapp.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import androidx.core.app.ActivityCompat


fun getUserLocation(activity: Activity, locationService: String, action: (Double, Double) -> Unit) {
    val locationManager = activity.getSystemService(locationService) as LocationManager


    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        showPopupEnableGPS(activity, locationManager)
    } else {

        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE
        criteria.powerRequirement = Criteria.POWER_HIGH
        criteria.isAltitudeRequired = false
        criteria.isBearingRequired = false
        criteria.isSpeedRequired = false
        criteria.isCostAllowed = true


        val provider = locationManager.getBestProvider(criteria, true)

        val location = provider?.let {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            locationManager.getLastKnownLocation(it)
        }

        if (location != null) {
            val latitude = location.latitude
            val longitude = location.longitude
            action.invoke(latitude, longitude)
        }
    }

}

fun showPopupEnableGPS(activity: Activity, locationManager: LocationManager) {
    val dialogBuilder = AlertDialog.Builder(activity)

    dialogBuilder.setMessage("You should enable location before open app")
        .setCancelable(false)
        .setPositiveButton("Ok") { _, _ ->
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showPopupEnableGPS(activity, locationManager)
            }
        }
        .setNegativeButton("Cancel") { _, _ ->
            activity.finish()
        }

    val alert = dialogBuilder.create()
    alert.setTitle("Enable location")
    alert.show()
}