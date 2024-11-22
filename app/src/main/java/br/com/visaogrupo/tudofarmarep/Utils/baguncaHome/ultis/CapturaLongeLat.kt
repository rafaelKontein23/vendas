package br.com.visaogrupo.tudofarmarep.Carga.ultis

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class CapturaLongeLat {
      suspend  fun capturaLogeLat(context: Context): Pair<String, String> {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return Pair("","")
            }
          val fusedLocationClient: FusedLocationProviderClient =
              LocationServices.getFusedLocationProviderClient(context)

          return suspendCancellableCoroutine { continuation ->
              fusedLocationClient.lastLocation.addOnCompleteListener { task: Task<Location> ->
                  if (task.isSuccessful && task.result != null) {
                      val location: Location = task.result
                      val longitude: Double = location.longitude
                      val latitude: Double = location.latitude

                      Log.d("lon", longitude.toString())
                      Log.d("lat", latitude.toString())

                      continuation.resume(Pair(longitude.toString(), latitude.toString()))
                  } else {
                      Log.d("Location", "Location not found")
                      continuation.resume(Pair("", ""))
                  }
              }.addOnFailureListener { exception ->
                  continuation.resumeWithException(exception)
              }
          }

        }


}