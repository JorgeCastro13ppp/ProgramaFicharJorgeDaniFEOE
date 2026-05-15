package com.example.programaficharfeoe.ui.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color

@Composable
fun BottomBar(navController: NavController) {

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Fichaje,
        BottomNavItem.Vacaciones,
        BottomNavItem.Faltas,
        BottomNavItem.Documentos,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .navigationBarsPadding(),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)
        )
    ) {

        NavigationBar(
            modifier = Modifier
                .height(74.dp)
                .clip(RoundedCornerShape(28.dp)),
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            tonalElevation = 0.dp
        ) {

            items.forEach { item ->

                val selected = currentRoute?.startsWith(item.route) == true

                val iconScale by animateFloatAsState(

                    targetValue = if (selected)
                        1.22f
                    else
                        1f,

                    animationSpec = spring(

                        dampingRatio = Spring.DampingRatioMediumBouncy,

                        stiffness = Spring.StiffnessLow
                    ),

                    label = ""
                )

                NavigationBarItem(
                    selected = selected,

                    onClick = {
                        navController.navigate(item.route) {

                            popUpTo(navController.graph.startDestinationId) {
                                saveState = false
                            }

                            launchSingleTop = true
                            restoreState = false
                        }
                    },

                    icon = {

                        Box(
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {

                            androidx.compose.animation.AnimatedVisibility(

                                visible = selected,

                                enter = fadeIn(
                                    animationSpec = tween(250)
                                ),

                                exit = fadeOut(
                                    animationSpec = tween(180)
                                )

                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .background(
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                            CircleShape
                                        )
                                )
                            }

                            Icon(

                                imageVector = item.icon,

                                contentDescription = item.title,

                                modifier = Modifier
                                    .scale(iconScale)
                                    .size(
                                        if (selected)
                                            30.dp
                                        else
                                            24.dp
                                    ),

                                tint = if (selected)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },

                    label = {

                        AnimatedVisibility(

                            visible = selected,

                            enter = fadeIn(
                                animationSpec = tween(220)
                            ) + scaleIn(),

                            exit = fadeOut(
                                animationSpec = tween(180)
                            ) + scaleOut()

                        ) {

                            androidx.compose.material3.Text(

                                text = item.title,

                                maxLines = 1,

                                overflow = TextOverflow.Ellipsis,

                                fontSize = 11.sp
                            )
                        }
                    },

                    alwaysShowLabel = false,

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)
                    )
                )
            }
        }
    }
}