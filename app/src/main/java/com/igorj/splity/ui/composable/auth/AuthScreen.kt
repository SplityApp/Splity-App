package com.igorj.splity.ui.composable.auth

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.igorj.splity.AuthViewModel
import com.igorj.splity.model.auth.AuthNavigationScreen
import com.igorj.splity.ui.composable.auth.forgot_password.ForgotPasswordScreen
import com.igorj.splity.ui.composable.auth.sign_in.SignInScreen
import com.igorj.splity.ui.composable.auth.sign_up.SignUpScreen
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
) {
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
                onSignInClicked = { email, password ->
                    authViewModel.login(email, password)
                },
                onNavigate = {
                    navController.navigate(it)
                }
            )
        }
        composable(AuthNavigationScreen.SignUp.name) {
            SignUpScreen(
                modifier = Modifier.fillMaxSize(),
                onSignUpClicked = { state ->
                    authViewModel.viewModelScope.launch {
                        val result = authViewModel.register(state)
                        if (result.isSuccess) {
                            navController.navigate(AuthNavigationScreen.SignIn.name)
                        }
                    }
                },
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
                },
                onConfirm = { email ->
                    authViewModel.resetPassword(email)
                }
            )
        }
    }
}
