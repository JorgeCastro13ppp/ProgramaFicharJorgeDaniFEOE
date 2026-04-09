package com.example.programaficharfeoe.data.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationService(private val context: Context) {

    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): Triple<Double, Double, Double>? {
        return suspendCancellableCoroutine { cont ->

            val fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(context)

            // 1. Intentar lastLocation
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->

                    if (location != null) {
                        cont.resume(
                            Triple(
                                location.latitude,
                                location.longitude,
                                location.accuracy.toDouble()
                            )
                        )
                    } else {
                        // 2. Si es null → pedir ubicación actual
                        fusedLocationClient.getCurrentLocation(
                            com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                            null
                        ).addOnSuccessListener { currentLocation ->

                            if (currentLocation != null) {
                                cont.resume(
                                    Triple(
                                        currentLocation.latitude,
                                        currentLocation.longitude,
                                        currentLocation.accuracy.toDouble()
                                    )
                                )
                            } else {
                                cont.resume(null)
                            }
                        }.addOnFailureListener {
                            cont.resume(null)
                        }
                    }
                }
                .addOnFailureListener {
                    cont.resume(null)
                }
        }
    }
}