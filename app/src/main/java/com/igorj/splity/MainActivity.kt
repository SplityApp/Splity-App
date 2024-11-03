package com.igorj.splity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.igorj.splity.model.auth.AuthState
import com.igorj.splity.ui.composable.auth.AuthScreen
import com.igorj.splity.ui.composable.main.MainScreen
import com.igorj.splity.ui.theme.SplityTheme
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.util.ObserveAsEvents
import com.igorj.splity.util.SnackbarController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : FragmentActivity() {

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplityTheme {
                val authState = authViewModel.authState.collectAsState()
                val isLoading = authViewModel.isLoading.collectAsState()
                
                val snackbarHostState = remember {
                    SnackbarHostState() 
                }
                val scope = rememberCoroutineScope()
                val snackbarColor = SnackbarController.snackbarColors.collectAsState()
                
                ObserveAsEvents(
                    flow = SnackbarController.snackbarEvents,
                    snackbarHostState
                ) { event ->
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()

                        val result = snackbarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.action?.text,
                            duration = snackbarColor.value.duration
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            event.action?.action?.invoke()
                        }
                    }
                }
                
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = localColorScheme.background,
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState
                        ) { snackbarData ->  
                            Snackbar(
                                snackbarData = snackbarData,
                                containerColor = snackbarColor.value.backgroundColor,
                                contentColor = snackbarColor.value.textColor
                            )
                        }
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
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
    }
}
