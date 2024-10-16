package com.igorj.splity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.igorj.splity.ui.composable.AuthScreen
import com.igorj.splity.ui.theme.SplityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplityTheme {
                AuthScreen()
            }
        }
    }
}
