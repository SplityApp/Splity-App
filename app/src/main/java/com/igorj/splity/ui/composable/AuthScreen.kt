package com.igorj.splity.ui.composable

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.igorj.splity.model.AuthNavigationScreen
import com.igorj.splity.ui.composable.auth.forgot_password.ForgotPasswordScreen
import com.igorj.splity.ui.composable.auth.sign_in.SignInScreen
import com.igorj.splity.ui.composable.auth.sign_up.SignUpScreen
import com.igorj.splity.ui.theme.SplityTheme

@Composable
fun AuthScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AuthNavigationScreen.SignIn.name,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        composable(AuthNavigationScreen.SignIn.name) {
            SignInScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigate = {
                    navController.navigate(it)
                }
            )
        }
        composable(AuthNavigationScreen.SignUp.name) {
            SignUpScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigate = {
                    navController.navigate(it)
                }
            )
        }
        composable(AuthNavigationScreen.ForgotPassword.name) {
            ForgotPasswordScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigate = {
                    navController.navigate(it)
                }
            )
        }
    }
}

@Preview
@Composable
private fun AuthScreenPreview() {
    SplityTheme(
        darkTheme = true
    ) {
        AuthScreen()
    }
}
