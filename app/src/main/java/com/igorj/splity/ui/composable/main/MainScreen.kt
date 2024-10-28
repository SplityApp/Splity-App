package com.igorj.splity.ui.composable.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.igorj.splity.model.main.MainNavigationScreen
import com.igorj.splity.ui.composable.main.home.HomeScreen
import com.igorj.splity.ui.composable.main.profile.ProfileScreen

@Composable
fun MainScreen(
    onLogoutAction: () -> Unit = {}
) {
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = MainNavigationScreen.Home.name,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        composable(MainNavigationScreen.Home.name) {
            HomeScreen(
                onGoToProfileAction = {
                    navController.navigate(MainNavigationScreen.Profile.name)
                }
            )
        }
        composable(MainNavigationScreen.Profile.name) {
            ProfileScreen(
                onLogoutAction = {
                    onLogoutAction()
                    navController.navigate(MainNavigationScreen.Home.name)
                },
                onGoToHomeAction = {
                    navController.navigate(MainNavigationScreen.Home.name)
                }
            )
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen()
}