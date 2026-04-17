package com.example.programaficharfeoe.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.ui.screens.documentos.DocumentosMenuScreen
import com.example.programaficharfeoe.ui.screens.documentos.DocumentosScreen
import com.example.programaficharfeoe.ui.screens.faltas.FaltasScreen
import com.example.programaficharfeoe.ui.screens.fichaje.FichajeScreen
import com.example.programaficharfeoe.ui.screens.home.HomeScreen
import com.example.programaficharfeoe.ui.screens.perfil.CambiarPasswordScreen
import com.example.programaficharfeoe.ui.screens.perfil.MiCuentaScreen
import com.example.programaficharfeoe.ui.screens.vacaciones.VacacionesScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLogout: () -> Unit
) {

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    var mostrarLogoutDialog by remember {
        mutableStateOf(false)
    }

    val username =
        SessionManager.getUsername() ?: "Usuario"

    ModalNavigationDrawer(

        drawerState = drawerState,

        drawerContent = {

            ModalDrawerSheet(
                modifier = Modifier.width(300.dp)
            ) {

                // HEADER
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF1E3A8A),
                                    Color(0xFF2563EB)
                                )
                            )
                        )
                        .padding(22.dp)
                ) {

                    Column {

                        Box(
                            modifier = Modifier
                                .size(62.dp)
                                .background(
                                    Color.White.copy(alpha = 0.20f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = username.take(1).uppercase(),
                                color = Color.White,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = username,
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "HIDROCAEX",
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                NavigationDrawerItem(
                    label = { Text("Mi perfil") },
                    selected = false,
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            null
                        )
                    },
                    onClick = {
                        navController.navigate("cuenta")
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 4.dp
                    )
                )

                NavigationDrawerItem(
                    label = { Text("Cambiar contraseña") },
                    selected = false,
                    icon = {
                        Icon(
                            Icons.Default.Lock,
                            null
                        )
                    },
                    onClick = {
                        navController.navigate("password")
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 4.dp
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                NavigationDrawerItem(
                    label = {
                        Text(
                            "Cerrar sesión",
                            color = Color(0xFFEF4444)
                        )
                    },
                    selected = false,
                    icon = {
                        Icon(
                            Icons.Default.Logout,
                            null,
                            tint = Color(0xFFEF4444)
                        )
                    },
                    onClick = {
                        mostrarLogoutDialog = true
                    },
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 14.dp
                    )
                )
            }
        }
    ) {

        Scaffold(

            topBar = {

                TopAppBar(

                    title = {
                        Text("HIDROCAEX")
                    },

                    navigationIcon = {

                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Menu,
                                null
                            )
                        }
                    }
                )
            },

            bottomBar = {
                BottomBar(navController)
            }

        ) { paddingValues ->

            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Home.route,
                modifier = Modifier.padding(paddingValues)
            ) {

                composable(BottomNavItem.Home.route) {
                    HomeScreen(
                        onIrFichaje = {
                            navController.navigate(
                                BottomNavItem.Fichaje.route
                            )
                        },
                        onIrVacaciones = {
                            navController.navigate(
                                BottomNavItem.Vacaciones.route
                            )
                        },
                        onIrDocumentos = {
                            navController.navigate(
                                BottomNavItem.Documentos.route
                            )
                        }
                    )
                }

                composable(BottomNavItem.Fichaje.route) {
                    FichajeScreen()
                }

                composable(BottomNavItem.Vacaciones.route) {
                    VacacionesScreen()
                }

                composable(BottomNavItem.Faltas.route) {
                    FaltasScreen()
                }

                composable(BottomNavItem.Documentos.route) {
                    DocumentosMenuScreen(navController)
                }

                composable("password") {
                    CambiarPasswordScreen()
                }

                composable(
                    "documentos/{tipo}/{titulo}"
                ) { backStackEntry ->

                    val tipo =
                        backStackEntry.arguments
                            ?.getString("tipo") ?: ""

                    val titulo =
                        backStackEntry.arguments
                            ?.getString("titulo")
                            ?: "Documentos"

                    DocumentosScreen(
                        titulo = titulo,
                        tipo = tipo
                    )
                }
                composable("cuenta") {
                    MiCuentaScreen()
                }
            }
        }
        if (mostrarLogoutDialog) {

            AlertDialog(

                onDismissRequest = {
                    mostrarLogoutDialog = false
                },

                title = {
                    Text("Cerrar sesión")
                },

                text = {
                    Text("¿Seguro que deseas cerrar sesión?")
                },

                confirmButton = {
                    TextButton(
                        onClick = {
                            mostrarLogoutDialog = false
                            onLogout()
                        }
                    ) {
                        Text(
                            "Cerrar sesión",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },

                dismissButton = {
                    TextButton(
                        onClick = {
                            mostrarLogoutDialog = false
                        }
                    ) {
                        Text("Cancelar")
                    }
                },

                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}