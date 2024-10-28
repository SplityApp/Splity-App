package com.igorj.splity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.igorj.splity.model.auth.AuthState
import com.igorj.splity.ui.composable.auth.AuthScreen
import com.igorj.splity.ui.composable.main.MainScreen
import com.igorj.splity.ui.theme.SplityTheme
import com.igorj.splity.ui.theme.localColorScheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplityTheme {
                val authState = authViewModel.authState.collectAsState()
                val isLoading = authViewModel.isLoading.collectAsState()

                when (authState.value) {
                    AuthState.Initial -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(localColorScheme.background)
                        ) {
                            AuthScreen(
                                authViewModel = authViewModel
                            )
                        }
                    }
                    AuthState.Authenticated -> {
                        MainScreen(
                            onLogoutClicked = {
                                authViewModel.logout()
                            }
                        )
                    }
                }

                if (isLoading.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(localColorScheme.background.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
