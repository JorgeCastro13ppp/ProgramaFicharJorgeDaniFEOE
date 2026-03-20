package com.example.programaficharfeoe.ui.screens.vacaciones

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacacionesScreen() {

    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    var error by remember { mutableStateOf("") }

    var showInicioPicker by remember { mutableStateOf(false) }
    var showFinPicker by remember { mutableStateOf(false) }

    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Solicitud de Vacaciones",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Fecha inicio
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showInicioPicker = true }
        ) {
            OutlinedTextField(
                value = fechaInicio,
                onValueChange = {},
                label = { Text("Fecha inicio") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 Fecha fin
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showFinPicker = true }
        ) {
            OutlinedTextField(
                value = fechaFin,
                onValueChange = {},
                label = { Text("Fecha fin") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 Motivo
        OutlinedTextField(
            value = motivo,
            onValueChange = { motivo = it },
            label = { Text("Motivo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                // 🔥 VALIDACIONES
                when {
                    fechaInicio.isEmpty() || fechaFin.isEmpty() || motivo.isEmpty() -> {
                        error = "Todos los campos son obligatorios"
                        mensaje = ""
                    }

                    !validarFechas(fechaInicio, fechaFin, formatter) -> {
                        error = "La fecha fin debe ser posterior a la fecha inicio"
                        mensaje = ""
                    }

                    else -> {
                        error = ""
                        mensaje = "Solicitud enviada correctamente ✅"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar solicitud")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔴 ERROR
        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }

        // 🟢 OK
        if (mensaje.isNotEmpty()) {
            Text(mensaje)
        }
    }

    // 🔥 DatePicker INICIO
    if (showInicioPicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showInicioPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        fechaInicio = formatter.format(Date(millis))
                    }
                    showInicioPicker = false
                }) {
                    Text("Aceptar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // 🔥 DatePicker FIN
    if (showFinPicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showFinPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        fechaFin = formatter.format(Date(millis))
                    }
                    showFinPicker = false
                }) {
                    Text("Aceptar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

// 🔥 Función validación fechas
fun validarFechas(
    inicio: String,
    fin: String,
    formatter: SimpleDateFormat
): Boolean {
    return try {
        val fechaInicio = formatter.parse(inicio)
        val fechaFin = formatter.parse(fin)

        fechaFin.after(fechaInicio)
    } catch (e: Exception) {
        false
    }
}