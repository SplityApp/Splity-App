package com.igorj.splity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.igorj.splity.ui.composable.AuthenticationTopBar
import com.igorj.splity.ui.theme.SplityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplityTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AuthenticationTopBar()
                    },
                    content = { innerPadding ->
                        Text(
                            modifier = Modifier.padding(innerPadding),
                            text = "Hello, World!",
                        )
                    },
                )
            }
        }
    }
}
