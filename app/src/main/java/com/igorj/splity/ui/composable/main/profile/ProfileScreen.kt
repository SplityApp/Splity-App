package com.igorj.splity.ui.composable.main.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.igorj.splity.ProfileViewModel
import com.igorj.splity.model.main.profile.NotificationsPreferencesSettingRow
import com.igorj.splity.model.main.profile.UserInfoState
import com.igorj.splity.ui.composable.main.EmojiAvatar
import com.igorj.splity.ui.theme.Red
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = koinViewModel(),
    onLogoutClicked: () -> Unit = {},
) {
    val context = LocalContext.current
    val userInfoState by profileViewModel.userInfoState.collectAsState()
    val isUpdatingPreferences by profileViewModel.isUpdatingPreferences.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(localColorScheme.background)
            .padding(
                horizontal = 16.dp,
                vertical = 16.dp
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        when (val state = userInfoState) {
            is UserInfoState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(localColorScheme.background.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is UserInfoState.Success -> {
                Log.d("LOGCAT", state.userInfo.toString())
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EmojiAvatar(
                        size = 120.dp,
                        emoji = state.userInfo.username.first().toString(),
                    )
                    Text(
                        text = state.userInfo.username,
                        style = typography.headlineMedium.copy(
                            fontWeight = FontWeight.Normal
                        ),
                        color = localColorScheme.secondary
                    )
                    Text(
                        text = state.userInfo.email,
                        style = typography.headlineSmall.copy(
                            fontWeight = FontWeight.Normal
                        ),
                        color = localColorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    NotificationsPreferencesSettingRow(
                        areNotificationsAllowed = state.userInfo.allowedNotifications,
                        onNotificationsAllowedChanged = { allowed ->
                            profileViewModel.updateNotificationPreferences(context, allowed)
                        },
                        enabled = !isUpdatingPreferences
                    )
                }
                Button(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    onClick = {
                        onLogoutClicked()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red
                    )
                ) {
                    Text(
                        text = "Logout",
                        style = typography.headlineMedium,
                        color = localColorScheme.secondary
                    )
                }
            }
            is UserInfoState.Error -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Error: ${state.message}",
                        color = Red
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            profileViewModel.refreshUserInfo()
                        }
                    ) {
                        Text("Retry")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            onLogoutClicked()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Red
                        )
                    ) {
                        Text(
                            text = "Logout",
                            style = typography.headlineMedium,
                            color = localColorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}
