package com.example.programaficharfeoe.ui.screens.fichaje

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.programaficharfeoe.viewmodel.FichajeViewModel
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.delay
import java.util.concurrent.Executors

@Composable
fun FichajeScreen(
    navController: NavController,
    viewModel: FichajeViewModel = viewModel()
) {
    val context = LocalContext.current
    val tipo = viewModel.tipoActual
    val isLoading = viewModel.isLoading

    var mostrarExito by remember { mutableStateOf(false) }
    var errorMensaje by remember { mutableStateOf<String?>(null) }
    var navegarHome by remember { mutableStateOf(false) }

    // 🔥 Navegación controlada
    LaunchedEffect(navegarHome) {
        if (navegarHome) {
            delay(1000)
            navController.navigate("home") {
                popUpTo("fichaje") { inclusive = true }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Text(
            text = if (tipo == "entrada") "Fichar entrada" else "Fichar salida",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {

            Box(modifier = Modifier.fillMaxSize()) {

                // 📷 CÁMARA
                QRScanner(
                    modifier = Modifier.fillMaxSize(),
                    onQrDetected = { qr ->

                        viewModel.fichar(qr) { ok ->

                            if (ok) {

                                // Vibración
                                val vibrator =
                                    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    vibrator.vibrate(
                                        VibrationEffect.createOneShot(
                                            200,
                                            VibrationEffect.DEFAULT_AMPLITUDE
                                        )
                                    )
                                } else {
                                    @Suppress("DEPRECATION")
                                    vibrator.vibrate(200)
                                }

                                mostrarExito = true
                                navegarHome = true

                            } else {

                                errorMensaje = "QR no válido"
                                navegarHome = true
                            }
                        }
                    }
                )

                // ÉXITO
                if (mostrarExito) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xAA4CAF50)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✔ Fichaje correcto",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }

                // ERROR
                if (errorMensaje != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xAAFF0000)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = errorMensaje!!,
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun QRScanner(
    modifier: Modifier = Modifier,
    onQrDetected: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    val previewView = remember { PreviewView(context) }
    var yaDetectado by remember { mutableStateOf(false) }

    AndroidView(
        factory = { previewView },
        modifier = modifier
    )

    LaunchedEffect(Unit) {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({

            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val scanner = BarcodeScanning.getClient()

            val analysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            analysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->

                if (yaDetectado) {
                    imageProxy.close()
                    return@setAnalyzer
                }

                val mediaImage = imageProxy.image
                if (mediaImage != null) {

                    val image = InputImage.fromMediaImage(
                        mediaImage,
                        imageProxy.imageInfo.rotationDegrees
                    )

                    scanner.process(image)
                        .addOnSuccessListener { barcodes ->
                            for (barcode in barcodes) {
                                barcode.rawValue?.let {

                                    if (!yaDetectado) {
                                        yaDetectado = true
                                        onQrDetected(it)
                                    }
                                }
                            }
                        }
                        .addOnFailureListener {
                            Log.e("QR", "Error escaneando", it)
                        }
                        .addOnCompleteListener {
                            imageProxy.close()
                        }
                } else {
                    imageProxy.close()
                }
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                analysis
            )

        }, ContextCompat.getMainExecutor(context))
    }
}