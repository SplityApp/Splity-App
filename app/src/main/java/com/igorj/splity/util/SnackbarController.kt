package com.igorj.splity.util

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

object SnackbarController {
    private val _snackbarColors = MutableStateFlow(SnackbarConfig())
    val snackbarColors = _snackbarColors.asStateFlow()

    private val _snackbarEvents = Channel<SnackbarEvent>()
    val snackbarEvents = _snackbarEvents.receiveAsFlow()

    suspend fun showSnackbar(event: SnackbarEvent) {
        val closableEvent = event.copy(action = SnackbarAction("OK") {})
        _snackbarColors.value = closableEvent.config
        _snackbarEvents.send(closableEvent)
    }
}

data class SnackbarConfig(
    val backgroundColor: Color = Color.Gray,
    val textColor: Color = Color.White,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val actionColor: Color = Color.Black
)

data class SnackbarEvent(
    val message: String,
    val config: SnackbarConfig = SnackbarConfig(),
    val action: SnackbarAction? = null
)

data class SnackbarAction(
    val text: String,
    val action: () -> Unit
)
