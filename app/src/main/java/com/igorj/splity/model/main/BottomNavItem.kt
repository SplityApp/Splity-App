package com.igorj.splity.model.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.igorj.splity.R

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: Int) {
    data object Home : BottomNavItem("home", Icons.Default.Home, R.string.home)
    data object Stats : BottomNavItem("stats", Icons.Default.Search, R.string.stats)
    data object Profile : BottomNavItem("profile", Icons.Default.Person, R.string.profile)
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Stats,
    BottomNavItem.Profile
)
