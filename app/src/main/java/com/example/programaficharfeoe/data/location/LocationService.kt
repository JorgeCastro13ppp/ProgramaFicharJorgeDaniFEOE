package com.example.programaficharfeoe.data.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Double
)

class LocationService(context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(
            context.applicationContext
        )

    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): LocationData? {
        return suspendCancellableCoroutine { cont ->

            // 1. Intentar obtener la última ubicación conocida
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (cont.isActive) {
                        if (location != null) {
                            cont.resume(
                                LocationData(
                                    latitude = location.latitude,
                                    longitude = location.longitude,
                                    accuracy = location.accuracy.toDouble()
                                )
                            )
                        } else {
                            requestCurrentLocation(cont)
                        }
                    }
                }
                .addOnFailureListener {
                    if (cont.isActive) {
                        cont.resume(null)
                    }
                }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestCurrentLocation(
        cont: kotlinx.coroutines.CancellableContinuation<LocationData?>
    ) {
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { location ->
            if (cont.isActive) {
                cont.resume(
                    location?.let {
                        LocationData(
                            latitude = it.latitude,
                            longitude = it.longitude,
                            accuracy = it.accuracy.toDouble()
                        )
                    }
                )
            }
        }.addOnFailureListener {
            if (cont.isActive) {
                cont.resume(null)
            }
        }
    }
}