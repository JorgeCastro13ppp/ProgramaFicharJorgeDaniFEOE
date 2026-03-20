package com.example.programaficharfeoe.ui.screens.qr

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.example.programaficharfeoe.data.repository.FichajeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import androidx.compose.ui.unit.dp

@androidx.camera.core.ExperimentalGetImage
@Composable
fun QRScreen() {

    val context = LocalContext.current
    var qrResult by remember { mutableStateOf("Escanea un QR") }
    var isScanned by remember { mutableStateOf(false) }

    // 🔐 Permiso cámara
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    if (!hasPermission) {
        Text("Solicitando permiso de cámara...")
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = qrResult,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // 🔥 Botón para pruebas sin cámara
        Button(
            onClick = {
                val fakeQR = "USER_123"
                qrResult = "Enviando..."

                CoroutineScope(Dispatchers.IO).launch {
                    val repo = FichajeRepository()
                    val result = repo.enviarQR(fakeQR)

                    qrResult = result
                    isScanned = true
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Simular QR")
        }

        if (!isScanned) {

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->

                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                    cameraProviderFuture.addListener({

                        val cameraProvider = cameraProviderFuture.get()

                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        val scanner = BarcodeScanning.getClient()

                        val analysis = ImageAnalysis.Builder().build()

                        analysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->

                            if (isScanned) {
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

                                            val qrValue = barcode.rawValue ?: "QR vacío"

                                            qrResult = "Enviando..."

                                            CoroutineScope(Dispatchers.IO).launch {
                                                val repo = FichajeRepository()
                                                val result = repo.enviarQR(qrValue)

                                                qrResult = result
                                                isScanned = true
                                            }
                                        }
                                    }
                                    .addOnFailureListener {
                                        qrResult = "Error al escanear"
                                    }
                                    .addOnCompleteListener {
                                        imageProxy.close()
                                    }

                            } else {
                                imageProxy.close()
                            }
                        }

                        cameraProvider.unbindAll()

                        cameraProvider.bindToLifecycle(
                            ctx as androidx.lifecycle.LifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            analysis
                        )

                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                }
            )

        } else {

            Column(modifier = Modifier.padding(16.dp)) {

                Text("Resultado recibido ✅")

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    isScanned = false
                    qrResult = "Escanea un QR"
                }) {
                    Text("Escanear otro")
                }
            }
        }
    }
}