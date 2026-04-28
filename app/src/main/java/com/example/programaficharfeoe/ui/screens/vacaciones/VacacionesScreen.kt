package com.example.programaficharfeoe.ui.screens.vacaciones

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.ui.screens.home.VacacionesCard
import com.example.programaficharfeoe.viewmodel.DashboardViewModel
import com.example.programaficharfeoe.viewmodel.VacacionesViewModel
import java.util.Calendar

@Composable
fun VacacionesScreen(
    viewModel: VacacionesViewModel = viewModel()
) {

    val dashboardViewModel: DashboardViewModel = viewModel()
    val vacaciones = viewModel.vacaciones
    val isLoading = viewModel.isLoading
    val error = viewModel.error
    val solicitudOk = viewModel.solicitudOk
    val errorSolicitud = viewModel.errorSolicitud

    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }
    var errorFechas by remember { mutableStateOf<String?>(null) }

    val diasRestantes = dashboardViewModel.diasVacacionesRestantes
    val diasLibres = dashboardViewModel.diasLibresRestantes
    val diasNavidad = dashboardViewModel.diasNavidadRestantes

    LaunchedEffect(Unit) {
        viewModel.cargarVacaciones()

        SessionManager.getUserId()?.let {
            dashboardViewModel.cargarDashboard(it)
        }
    }

    fun validarFechas(
        inicio: String,
        fin: String
    ): String? {

        if (inicio.isBlank() || fin.isBlank()) {
            return "Debes seleccionar ambas fechas"
        }

        fun parse(fecha: String): Calendar {
            val p = fecha.split("-")
            return Calendar.getInstance().apply {
                set(p[0].toInt(), p[1].toInt() - 1, p[2].toInt(), 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }

        val inicioCal = parse(inicio)
        val finCal = parse(fin)

        if (finCal.before(inicioCal)) {
            return "La fecha fin no puede ser anterior"
        }

        return null
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        item {

            // HEADER
            Card(
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF1E3A8A),
                                    Color(0xFF2563EB)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            Icons.Default.BeachAccess,
                            null,
                            tint = Color.White
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {

                            Text(
                                "Vacaciones",
                                color = Color.White,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                "Solicita y revisa tus vacaciones",
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }
        }

        // RESUMEN
        item {
            VacacionesCard(
                restantes = diasRestantes,
                libresRestantes = diasLibres,
                navidadRestantes = diasNavidad
            )
        }

        // SOLICITUD
        item {

            Text(
                text = "Nueva solicitud",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            DatePickerField(
                label = "Fecha inicio",
                selectedDate = fechaInicio,
                onDateSelected = {
                    fechaInicio = it
                }
            )
        }

        item {
            DatePickerField(
                label = "Fecha fin",
                selectedDate = fechaFin,
                onDateSelected = {
                    fechaFin = it
                }
            )
        }

        item {

            Button(
                onClick = {

                    val e = validarFechas(
                        fechaInicio,
                        fechaFin
                    )

                    if (e != null) {
                        errorFechas = e
                    } else {
                        errorFechas = null
                        viewModel.solicitarVacaciones(
                            fechaInicio,
                            fechaFin
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp)
            ) {

                Icon(Icons.Default.Send, null)

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    "Enviar solicitud",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        errorFechas?.let {
            item {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        if (solicitudOk) {
            item {
                Text(
                    "Solicitud enviada correctamente",
                    color = Color(0xFF22C55E)
                )
            }
        }

        errorSolicitud?.let {
            item {
                Text(
                    "Error: $it",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Historial",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        if (isLoading) {

            item {
                CircularProgressIndicator()
            }

        } else if (error != null) {

            item {
                Text(
                    "Error: $error",
                    color = MaterialTheme.colorScheme.error
                )
            }

        } else {

            items(vacaciones) { v ->

                val estado = v.estado.trim().uppercase()

                val colorEstado = when (estado) {
                    "APROBADA",
                    "APROBADO" -> Color(0xFF22C55E)

                    "PENDIENTE" -> Color(0xFFF59E0B)

                    "RECHAZADA",
                    "RECHAZADO" -> Color(0xFFEF4444)

                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }

                val icono = when (estado) {
                    "APROBADA",
                    "APROBADO" -> Icons.Default.CheckCircle

                    "PENDIENTE" -> Icons.Default.Pending

                    "RECHAZADA",
                    "RECHAZADO" -> Icons.Default.Close

                    else -> Icons.Default.CalendarMonth
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {

                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            icono,
                            null,
                            tint = colorEstado
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                "${v.fechaInicio} → ${v.fechaFin}",
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                estado,
                                color = colorEstado
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun ResumenBox(
    titulo: String,
    valor: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            titulo,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            valor,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {

    var showDialog by remember {
        mutableStateOf(false)
    }

    val datePickerState =
        rememberDatePickerState()

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {

        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    Icons.Default.CalendarMonth,
                    null
                )
            }
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable {
                    showDialog = true
                }
        )
    }

    if (showDialog) {

        DatePickerDialog(
            onDismissRequest = {
                showDialog = false
            },

            confirmButton = {
                TextButton(
                    onClick = {

                        val millis =
                            datePickerState.selectedDateMillis

                        if (millis != null) {

                            val c =
                                Calendar.getInstance()

                            c.timeInMillis = millis

                            val fecha =
                                "%04d-%02d-%02d".format(
                                    c.get(Calendar.YEAR),
                                    c.get(Calendar.MONTH) + 1,
                                    c.get(Calendar.DAY_OF_MONTH)
                                )

                            onDateSelected(fecha)
                        }

                        showDialog = false
                    }
                ) {
                    Text("OK")
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}