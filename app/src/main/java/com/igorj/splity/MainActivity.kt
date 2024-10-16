package com.igorj.splity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.igorj.splity.ui.composable.AuthScreen
import com.igorj.splity.ui.theme.SplityTheme
import com.igorj.splity.ui.theme.localColorScheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplityTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(localColorScheme.background)
                ) {
                    AuthScreen()
                }
            }
        }
    }
}
