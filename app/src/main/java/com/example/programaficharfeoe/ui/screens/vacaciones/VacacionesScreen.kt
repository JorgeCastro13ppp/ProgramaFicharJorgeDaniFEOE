package com.example.programaficharfeoe.ui.screens.vacaciones

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.programaficharfeoe.viewmodel.VacacionesViewModel

@Composable
fun VacacionesScreen(
    viewModel: VacacionesViewModel = viewModel()
) {

    val vacaciones = viewModel.vacaciones.value
    val isLoading = viewModel.isLoading.value
    val error = viewModel.error.value
    val solicitudOk = viewModel.solicitudOk.value
    val errorSolicitud = viewModel.errorSolicitud.value

    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }
    var errorFechas by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.cargarVacaciones()
    }

    // 🔹 VALIDACIÓN
    fun validarFechas(inicio: String, fin: String): String? {

        if (inicio.isBlank() || fin.isBlank()) {
            return "Debes seleccionar ambas fechas"
        }

        fun parseFecha(fecha: String): java.util.Calendar {
            val parts = fecha.split("-")
            val cal = java.util.Calendar.getInstance()
            cal.set(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt(), 0, 0, 0)
            cal.set(java.util.Calendar.MILLISECOND, 0)
            return cal
        }

        val inicioCal = parseFecha(inicio)
        val finCal = parseFecha(fin)

        // fin antes que inicio
        if (finCal.before(inicioCal)) {
            return "La fecha fin no puede ser anterior a la de inicio"
        }

        // fechas pasadas
        val hoy = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }

        if (inicioCal.before(hoy)) {
            return "No puedes seleccionar fechas pasadas"
        }

        return null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Vacaciones",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Solicitar vacaciones")

        Spacer(modifier = Modifier.height(8.dp))

        DatePickerField(
            label = "Fecha inicio",
            selectedDate = fechaInicio,
            onDateSelected = { fechaInicio = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        DatePickerField(
            label = "Fecha fin",
            selectedDate = fechaFin,
            onDateSelected = { fechaFin = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {

                val error = validarFechas(fechaInicio, fechaFin)

                if (error != null) {
                    errorFechas = error
                } else {
                    errorFechas = null
                    viewModel.solicitarVacaciones(fechaInicio, fechaFin)
                    fechaInicio = ""
                    fechaFin = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar solicitud")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 ERROR VALIDACIÓN
        errorFechas?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        // 🔹 RESPUESTA BACKEND
        if (solicitudOk) {
            Text("Solicitud enviada correctamente")
        }

        errorSolicitud?.let {
            Text("Error: $it")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 LISTADO
        when {
            isLoading -> {
                Text("Cargando...")
            }

            error != null -> {
                Text("Error: $error")
            }

            else -> {
                LazyColumn {
                    items(vacaciones) { v ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Text("Inicio: ${v.fechaInicio}")
                                Text("Fin: ${v.fechaFin}")
                                Text("Estado: ${v.estado}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Box(modifier = Modifier.fillMaxWidth()) {

        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { showDialog = true }
        )
    }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false

                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {

                        val calendar = java.util.Calendar.getInstance()
                        calendar.timeInMillis = millis

                        val year = calendar.get(java.util.Calendar.YEAR)
                        val month = calendar.get(java.util.Calendar.MONTH) + 1
                        val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)

                        val formattedDate = String.format(
                            "%04d-%02d-%02d",
                            year,
                            month,
                            day
                        )

                        onDateSelected(formattedDate)
                    }
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}