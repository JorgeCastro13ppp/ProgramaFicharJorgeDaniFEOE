package com.example.programaficharfeoe.ui.screens.vacaciones

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pending
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
import com.example.programaficharfeoe.ui.components.AppCard
import com.example.programaficharfeoe.ui.screens.home.VacacionesCard
import com.example.programaficharfeoe.viewmodel.DashboardViewModel
import com.example.programaficharfeoe.viewmodel.VacacionesViewModel
import java.util.Calendar
import com.example.programaficharfeoe.ui.components.LoadingView
import com.example.programaficharfeoe.ui.components.ErrorView
import com.example.programaficharfeoe.ui.components.AppButton
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.draw.alpha

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VacacionesScreen(
    viewModel: VacacionesViewModel = viewModel()
) {

    val dashboardViewModel: DashboardViewModel = viewModel()
    val vacacionesState = viewModel.uiState

    var visible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {

        kotlinx.coroutines.delay(120)

        visible = true
    }

    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }
    var errorFechas by remember { mutableStateOf<String?>(null) }

    var enviandoSolicitud by remember {
        mutableStateOf(false)
    }

    val dashboardState = dashboardViewModel.uiState

    val diasRestantes =
        dashboardState.diasVacacionesRestantes

    val diasLibres =
        dashboardState.diasLibresRestantes

    val diasNavidad =
        dashboardState.diasNavidadRestantes

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
            AnimatedVisibility(

                visible = visible,

                enter = fadeIn(
                    animationSpec = tween(700)
                ) + slideInVertically(

                    initialOffsetY = { -80 },

                    animationSpec = tween(700)
                )
            ) {

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

                            Spacer(
                                modifier = Modifier.width(10.dp)
                            )

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
        }

        // RESUMEN
        item {

            AnimatedVisibility(

                visible = visible,

                enter = fadeIn(
                    animationSpec = tween(650)
                ) + slideInVertically(

                    initialOffsetY = { 60 },

                    animationSpec = tween(650)
                )
            ) {

                VacacionesCard(
                    restantes = diasRestantes,
                    libresRestantes = diasLibres,
                    navidadRestantes = diasNavidad
                )
            }
        }

        // SOLICITUD
        item {
            AnimatedVisibility(

                visible = visible,

                enter = fadeIn(
                    animationSpec = tween(750)
                ) + slideInVertically(

                    initialOffsetY = { 40 },

                    animationSpec = tween(750)
                )
            ) {

                Text(
                    text = "Nueva solicitud",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
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

            AppButton(

                text = if (enviandoSolicitud)
                    "Enviando..."
                else
                    "Enviar solicitud",

                isLoading = enviandoSolicitud,

                onClick = {

                    val e = validarFechas(
                        fechaInicio,
                        fechaFin
                    )

                    if (e != null) {

                        errorFechas = e

                    } else {

                        errorFechas = null

                        enviandoSolicitud = true

                        viewModel.solicitarVacaciones(
                            fechaInicio,
                            fechaFin
                        )
                    }
                }
            )
        }

        item {

            AnimatedVisibility(

                visible = errorFechas != null,

                enter = fadeIn() +
                        slideInVertically(
                            initialOffsetY = { -40 }
                        ) +
                        expandVertically(),

                exit = fadeOut() +
                        slideOutVertically(
                            targetOffsetY = { -40 }
                        ) +
                        shrinkVertically()

            ) {

                Text(
                    text = errorFechas ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }


        item {

            AnimatedVisibility(

                visible = vacacionesState.solicitudOk,

                enter = fadeIn() +
                        slideInVertically(
                            initialOffsetY = { -40 }
                        ) +
                        expandVertically(),

                exit = fadeOut() +
                        slideOutVertically(
                            targetOffsetY = { -40 }
                        ) +
                        shrinkVertically()

            ) {

                Surface(

                    shape = RoundedCornerShape(50),

                    color = Color(0xFF22C55E).copy(alpha = 0.12f)

                ) {

                    Row(

                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 10.dp
                        ),

                        verticalAlignment = Alignment.CenterVertically

                    ) {

                        Icon(
                            Icons.Default.CheckCircle,
                            null,
                            tint = Color(0xFF22C55E)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Solicitud enviada correctamente",
                            color = Color(0xFF22C55E),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        item {

            AnimatedVisibility(

                visible = vacacionesState.errorSolicitud != null,

                enter = fadeIn() +
                        slideInVertically(
                            initialOffsetY = { -40 }
                        ) +
                        expandVertically(),

                exit = fadeOut() +
                        slideOutVertically(
                            targetOffsetY = { -40 }
                        ) +
                        shrinkVertically()

            ) {

                ErrorView(
                    message = vacacionesState.errorSolicitud
                        ?: "Error desconocido"
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

        if (vacacionesState.isLoading) {

            item {
                LoadingView()
            }

        } else if (vacacionesState.error != null) {

            item {
                Text(
                    "Error: ${vacacionesState.error}",
                    color = MaterialTheme.colorScheme.error
                )
            }

        } else {

            itemsIndexed(
                vacacionesState.vacaciones
            ) { index, v ->

                val visibleState = remember {
                    mutableStateOf(false)
                }

                LaunchedEffect(Unit) {

                    kotlinx.coroutines.delay(
                        index * 80L
                    )

                    visibleState.value = true
                }

                LaunchedEffect(
                    vacacionesState.solicitudOk,
                    vacacionesState.errorSolicitud
                ) {

                    if (
                        vacacionesState.solicitudOk ||
                        vacacionesState.errorSolicitud != null
                    ) {

                        kotlinx.coroutines.delay(900)

                        enviandoSolicitud = false
                    }
                }

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

                AnimatedVisibility(

                    visible = visibleState.value,

                    enter = fadeIn(
                        animationSpec = tween(400)
                    ) + slideInVertically(

                        initialOffsetY = { 40 },

                        animationSpec = tween(400)
                    )

                ) {

                    Row(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        // Línea temporal
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .background(
                                        colorEstado,
                                        CircleShape
                                    )
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(54.dp)
                                    .alpha(0.35f)
                                    .background(colorEstado)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                "${v.fechaInicio} → ${v.fechaFin}",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(
                                modifier = Modifier.height(10.dp)
                            )

                            Surface(

                                shape = RoundedCornerShape(50),

                                color = colorEstado.copy(alpha = 0.12f)

                            ) {

                                Row(

                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 6.dp
                                    ),

                                    verticalAlignment = Alignment.CenterVertically

                                ) {

                                    Icon(
                                        icono,
                                        null,
                                        tint = colorEstado,
                                        modifier = Modifier.size(18.dp)
                                    )

                                    Spacer(
                                        modifier = Modifier.width(6.dp)
                                    )

                                    Text(
                                        text = estado,
                                        color = colorEstado,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
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

    var focused by remember {
        mutableStateOf(false)
    }

    val borderColor by animateColorAsState(

        targetValue = if (focused)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),

        label = ""
    )

    val datePickerState =
        rememberDatePickerState()

    AppCard {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.5.dp,
                    brush = SolidColor(borderColor),
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(horizontal = 14.dp, vertical = 4.dp)
        ) {

            OutlinedTextField(

                value = selectedDate,

                onValueChange = {},

                readOnly = true,

                label = {
                    Text(label)
                },

                modifier = Modifier.fillMaxWidth(),

                colors = OutlinedTextFieldDefaults.colors(

                    focusedBorderColor = Color.Transparent,

                    unfocusedBorderColor = Color.Transparent
                ),

                trailingIcon = {

                    Icon(
                        Icons.Default.CalendarMonth,
                        null,
                        tint = if (focused)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable {

                        focused = true

                        showDialog = true
                    }
            )
        }
    }

    if (showDialog) {

        DatePickerDialog(

            onDismissRequest = {

                focused = false

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

                        focused = false

                        showDialog = false
                    }
                ) {

                    Text("OK")
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {

                        focused = false

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