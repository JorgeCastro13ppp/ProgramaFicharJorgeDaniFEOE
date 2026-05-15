package com.example.programaficharfeoe.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.ui.screens.login.LoginScreen
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.ExperimentalAnimationApi

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {

    val navController = rememberAnimatedNavController()

    val startDestination =
        if (!SessionManager.getToken().isNullOrEmpty()) {
            AppRoutes.Main.route
        } else {
            AppRoutes.Login.route
        }

    AnimatedNavHost(

        navController = navController,

        startDestination = startDestination,

        enterTransition = {

            slideIntoContainer(

                AnimatedContentTransitionScope.SlideDirection.Left,

                animationSpec = tween(300)
            ) + fadeIn(
                animationSpec = tween(300)
            )
        },

        exitTransition = {

            slideOutOfContainer(

                AnimatedContentTransitionScope.SlideDirection.Left,

                animationSpec = tween(300)
            ) + fadeOut(
                animationSpec = tween(300)
            )
        },

        popEnterTransition = {

            slideIntoContainer(

                AnimatedContentTransitionScope.SlideDirection.Right,

                animationSpec = tween(300)
            ) + fadeIn(
                animationSpec = tween(300)
            )
        },

        popExitTransition = {

            slideOutOfContainer(

                AnimatedContentTransitionScope.SlideDirection.Right,

                animationSpec = tween(300)
            ) + fadeOut(
                animationSpec = tween(300)
            )
        }

    ) {

        composable(AppRoutes.Login.route) {

            LoginScreen(

                onLoginSuccess = {

                    navController.navigate(
                        AppRoutes.Main.route
                    ) {

                        popUpTo(
                            AppRoutes.Login.route
                        ) {

                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(AppRoutes.Main.route) {

            MainScreen(

                onLogout = {

                    SessionManager.clearSession()

                    navController.navigate(
                        AppRoutes.Login.route
                    ) {

                        popUpTo(
                            AppRoutes.Main.route
                        ) {

                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
