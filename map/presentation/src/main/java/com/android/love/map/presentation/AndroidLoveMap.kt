package com.android.love.map.presentation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.location.LocationServices
import com.google.maps.android.compose.GoogleMap
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AndroidLoveMap() {

    val context = LocalContext.current

    var locationPermissionsGranted by remember {
        mutableStateOf(
            areLocationPermissionsAlreadyGranted(context = context)
        )
    }

    var shouldShowPermissionRationale by remember {
        mutableStateOf(
            shouldShowRequestPermissionRationale(
                context.findActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    var shouldDirectUserToApplicationSettings by remember {
        mutableStateOf(false)
    }


    var currentPermissionsStatus by remember {
        mutableStateOf(
            decideCurrentPermissionStatus(
                locationPermissionsGranted,
                shouldShowPermissionRationale
            )
        )
    }

    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )


    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            locationPermissionsGranted =
                permissions.values.reduce { acc, isPermissionGranted ->
                    acc && isPermissionGranted
                }

            if (!locationPermissionsGranted) {
                shouldShowPermissionRationale =
                    shouldShowRequestPermissionRationale(
                        context.findActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
            }
            shouldDirectUserToApplicationSettings =
                !shouldShowPermissionRationale && !locationPermissionsGranted
            currentPermissionsStatus = decideCurrentPermissionStatus(
                locationPermissionsGranted,
                shouldShowPermissionRationale
            )
        })


    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START &&
                !locationPermissionsGranted &&
                !shouldShowPermissionRationale
            ) {
                locationPermissionLauncher.launch(locationPermissions)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })




    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch {

            val locationService : ILocationService = LocationService(
                context,
                LocationServices.getFusedLocationProviderClient(context)
            )

            locationService.requestLocationUpdates().collectLatest {
                println("Location: ${it?.latitude} ${it?.longitude}")
            }
        }
    }



    GoogleMap()

    /*val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxWidth(),
                text = "Location Permissions",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Text(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxWidth(),
                text = "Current Permission Status: $currentPermissionsStatus",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
        if (shouldShowPermissionRationale) {
            LaunchedEffect(Unit) {
                scope.launch {
                    val userAction = snackbarHostState.showSnackbar(
                        message = "Please authorize location permissions",
                        actionLabel = "Approve",
                        duration = SnackbarDuration.Indefinite,
                        withDismissAction = true
                    )
                    when (userAction) {
                        SnackbarResult.ActionPerformed -> {
                            shouldShowPermissionRationale = false
                            locationPermissionLauncher.launch(locationPermissions)
                        }

                        SnackbarResult.Dismissed -> {
                            shouldShowPermissionRationale = false
                        }
                    }
                }
            }
        }
        if (shouldDirectUserToApplicationSettings) {
            openApplicationSettings(context)
        }
    }*/
}


fun areLocationPermissionsAlreadyGranted(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

fun openApplicationSettings(context: Context) {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    ).also {
        context.startActivity(it)
    }
}

fun decideCurrentPermissionStatus(
    locationPermissionsGranted: Boolean,
    shouldShowPermissionRationale: Boolean
): String {
    return if (locationPermissionsGranted) "Granted"
    else if (shouldShowPermissionRationale) "Rejected"
    else "Denied"
}


fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}



