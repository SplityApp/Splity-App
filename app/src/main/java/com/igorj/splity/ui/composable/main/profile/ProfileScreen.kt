package com.igorj.splity.ui.composable.main.profile

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.igorj.splity.ProfileViewModel
import com.igorj.splity.R
import com.igorj.splity.model.main.profile.NotificationsPreferencesSettingRow
import com.igorj.splity.model.main.profile.UserInfoState
import com.igorj.splity.ui.composable.TextField
import com.igorj.splity.ui.composable.main.EmojiAvatar
import com.igorj.splity.ui.theme.Red
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = koinViewModel(),
    onLogoutClicked: () -> Unit = {},
) {
    val context = LocalContext.current
    val userInfoState by profileViewModel.userInfoState.collectAsState()

    var showChangeUserInfoModalBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }

    val dialogQueue = profileViewModel.visiblePermissionDialogQueue

    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else true
        )
    }

    LifecycleResumeEffect(Unit) {
        hasNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true

        onPauseOrDispose {

        }
    }

    val notificationsPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            profileViewModel.onPermissionResult(
                permission = Manifest.permission.POST_NOTIFICATIONS,
                isGranted = isGranted
            )
        }
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(localColorScheme.background)
            .padding(
                horizontal = 16.dp,
                vertical = 16.dp
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        val targetWidth = (maxWidth * 0.35f).coerceAtMost(200.dp)

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
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    IconButton(
                        onClick = {
                            showChangeUserInfoModalBottomSheet = true
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = stringResource(R.string.profile_screen_ui_edit)
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        EmojiAvatar(
                            size = 120.dp,
                            emoji = state.userInfo.charImage,
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
                            areNotificationsAllowed = hasNotificationPermission,
                            onNotificationsAllowedChanged = { _ ->
                                if (hasNotificationPermission) {
                                    context.openAppSettings()
                                } else {
                                    notificationsPermissionResultLauncher.launch(
                                        Manifest.permission.POST_NOTIFICATIONS
                                    )
                                }
                            },
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
                            text = stringResource(R.string.profile_screen_ui_logout),
                            style = typography.headlineMedium,
                            color = localColorScheme.secondary
                        )
                    }
                }

                if (showChangeUserInfoModalBottomSheet) {
                    val focusManager = LocalFocusManager.current
                    val (usernameRef, emailRef, charImageRef) = FocusRequester.createRefs()
                    var username by rememberSaveable {
                        mutableStateOf(state.userInfo.username)
                    }
                    var email by rememberSaveable {
                        mutableStateOf(state.userInfo.email)
                    }
                    var charImage by rememberSaveable {
                        mutableStateOf(state.userInfo.charImage)
                    }
                    var isLoading by rememberSaveable {
                        mutableStateOf(false)
                    }

                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(localColorScheme.background.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else  {
                        ModalBottomSheet(
                            containerColor = localColorScheme.tertiaryContainer,
                            onDismissRequest = {
                                showChangeUserInfoModalBottomSheet = false
                            },
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.profile_screen_ui_change_user_info),
                                    style = typography.headlineMedium,
                                    color = localColorScheme.secondary,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                TextField(
                                    modifier = Modifier
                                        .focusRequester(usernameRef)
                                        .focusProperties {
                                            next = emailRef
                                        },
                                    label = stringResource(id = R.string.signUpScreen_ui_usernameTextFieldLabel),
                                    value = username,
                                    onValueChange = {
                                        username = it
                                    },
                                    onImeAction = {
                                        focusManager.moveFocus(FocusDirection.Next)
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Next
                                    )
                                )
                                TextField(
                                    modifier = Modifier
                                        .focusRequester(emailRef)
                                        .focusProperties {
                                            previous = usernameRef
                                            next = charImageRef
                                        },
                                    label = stringResource(id = R.string.signUpScreen_ui_emailTextFieldLabel),
                                    value = email,
                                    onValueChange = {
                                        email = it
                                    },
                                    onImeAction = {
                                        focusManager.moveFocus(FocusDirection.Next)
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Email,
                                        imeAction = ImeAction.Next
                                    )
                                )
                                TextField(
                                    modifier = Modifier
                                        .focusRequester(charImageRef)
                                        .focusProperties {
                                            previous = emailRef
                                        },
                                    label = stringResource(R.string.profile_screen_ui_character_image),
                                    value = charImage,
                                    onValueChange = { input ->
                                        val codePointCount = input.codePointCount(0, input.length)
                                        if (codePointCount <= 1) {
                                            charImage = input
                                        }
                                    },
                                    onImeAction = {
                                        focusManager.clearFocus()
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Ascii,
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    modifier = Modifier
                                        .height(50.dp)
                                        .width(targetWidth)
                                        .align(Alignment.CenterHorizontally),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = localColorScheme.primary,
                                    ),
                                    onClick = {
                                        isLoading = true
                                        profileViewModel.changeUserInfo(
                                            username,
                                            email,
                                            charImage,
                                            state.userInfo.email != email,
                                            onComplete = {
                                                isLoading = false
                                                showChangeUserInfoModalBottomSheet = false
                                            }
                                        )
                                    }
                                ) {
                                    Text(
                                        text = stringResource(R.string.profile_screen_ui_save),
                                        style = typography.bodyLarge,
                                        color = localColorScheme.secondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
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

        dialogQueue
            .reversed()
            .forEach { _ ->
                PermissionDialog(
                    onDismiss = profileViewModel::dismissDialog,
                    onGoToAppSettingsClick = {
                        profileViewModel.dismissDialog()
                        context.openAppSettings()
                    }
                )
            }
    }
}

fun Context.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}
