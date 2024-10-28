package com.igorj.splity.ui.composable.main

import BottomNavBar
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.igorj.splity.model.main.BottomNavItem
import com.igorj.splity.ui.composable.main.home.HomeScreen
import com.igorj.splity.ui.composable.main.profile.ProfileScreen

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onLogoutClicked: () -> Unit = {}
) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    val bottomNavBarItems = remember {
        listOf(
            BottomNavItem.Home,
            BottomNavItem.Stats,
            BottomNavItem.Profile
        )
    }

    Scaffold(
        modifier = modifier,
        content = { paddingValues ->
            NavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                navController = navController,
                startDestination = BottomNavItem.Home.route,
                enterTransition = {
                    EnterTransition.None
                },
                exitTransition = {
                    ExitTransition.None
                }
            ) {
                composable(BottomNavItem.Home.route) {
                    HomeScreen()
                }
                composable(BottomNavItem.Stats.route) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Stats")
                    }
                }
                composable(BottomNavItem.Profile.route) {
                    ProfileScreen(
                        onLogoutClicked = {
                            onLogoutClicked()
                        }
                    )
                }
            }
        },
        bottomBar = {
             BottomNavBar(
                 items = bottomNavBarItems,
                 onNavigate = { route ->
                     navController.navigate(route)
                 },
                 currentRoute = currentRoute
             )
        }
    )
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen()
}