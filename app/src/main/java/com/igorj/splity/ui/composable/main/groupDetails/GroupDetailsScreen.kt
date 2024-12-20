package com.igorj.splity.ui.composable.main.groupDetails

import ScreenSwitch
import ScreenSwitchOption
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.igorj.splity.R
import com.igorj.splity.model.main.groupDetails.GroupDetailsState
import com.igorj.splity.ui.composable.main.groupDetails.balance.BalancesScreen
import com.igorj.splity.ui.composable.main.groupDetails.expense.ExpenseViewModel
import com.igorj.splity.ui.composable.main.groupDetails.expense.ExpensesScreen
import com.igorj.splity.ui.composable.main.groupDetails.expense.NewExpenseBottomSheet
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography
import com.igorj.splity.util.LoadingController
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailsScreen(
    groupDetailsViewModel: GroupDetailsViewModel = koinViewModel(),
    expenseViewModel: ExpenseViewModel = koinViewModel(),
    groupId: String
) {
    val clipboardManager = LocalClipboardManager.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(true) {
        groupDetailsViewModel.getGroupDetails(groupId)
    }

    val groupDetailsState by groupDetailsViewModel.groupDetails.collectAsStateWithLifecycle()
    val isRefreshing by groupDetailsViewModel.isRefreshing.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { groupDetailsViewModel.getGroupDetails(groupId) })
    var showInviteToGroupDialog by rememberSaveable { mutableStateOf(false) }
    val newExpenseSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showNewExpenseModalBottomSheet by rememberSaveable { mutableStateOf(false) }
    var selectedOption by rememberSaveable { mutableStateOf(ScreenSwitchOption.First) }

    when (val state = groupDetailsState) {
        GroupDetailsState.Loading -> {
            LaunchedEffect(true) {
                LoadingController.showLoading()
            }

            DisposableEffect(true) {
                onDispose {
                    LoadingController.hideLoading()
                }
            }
        }
        is GroupDetailsState.Success -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                backgroundColor = localColorScheme.background,
                topBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = state.groupDetails.name,
                            style = typography.headlineLarge,
                            color = localColorScheme.secondary,
                        )
                        IconButton(
                            onClick = {
                                showNewExpenseModalBottomSheet = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AddCard,
                                contentDescription = stringResource(R.string.groupDetailsScreen_alt_newExpenseButton)
                            )
                        }
                        IconButton(
                            onClick = {
                                showInviteToGroupDialog = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = stringResource(R.string.groupDetailsScreen_alt_inviteToGroupButton)
                            )
                        }
                    }
                },
                content = { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .pullRefresh(pullRefreshState),
                    ) {
                        Column {
                            ScreenSwitch(
                                leftScreen = {
                                    ExpensesScreen(
                                        groupId = groupId,
                                        currency = state.groupDetails.currency
                                    )
                                },
                                rightScreen = {
                                    BalancesScreen(
                                        groupId = groupId,
                                        currency = state.groupDetails.currency
                                    )
                                },
                                leftLabel = stringResource(id = R.string.groupDetailsScreen_ui_screenSwitchButtonLeftLabel),
                                rightLabel = stringResource(id = R.string.groupDetailsScreen_ui_screenSwitchButtonRightLabel),
                                leftIcon = Icons.Default.AttachMoney,
                                rightIcon = Icons.Default.Balance,
                                selectedOption = selectedOption,
                                onSwitch = {
                                    selectedOption = it
                                }
                            )
                        }

                        PullRefreshIndicator(
                            modifier = Modifier.align(Alignment.TopCenter),
                            refreshing = isRefreshing,
                            state = pullRefreshState
                        )

                        NewExpenseBottomSheet(
                            showSheet = showNewExpenseModalBottomSheet,
                            focusManager = focusManager,
                            sheetState = newExpenseSheetState,
                            users = state.groupDetails.profiles,
                            groupId = groupId,
                            onDismiss = {
                                showNewExpenseModalBottomSheet = false
                            },
                            onAddExpense = {
                                expenseViewModel.addExpense(it)
                            }
                        )
                    }

                    if (showInviteToGroupDialog) {
                        Dialog(
                            onDismissRequest = {
                                showInviteToGroupDialog = false
                            },
                            properties = DialogProperties(
                                dismissOnBackPress = true,
                                dismissOnClickOutside = true,
                                usePlatformDefaultWidth = false
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(localColorScheme.background.copy(alpha = 0.8f))
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = {
                                            showInviteToGroupDialog = false
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .align(Alignment.TopEnd),
                                    onClick = {
                                        showInviteToGroupDialog = false
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = stringResource(R.string.groupDetailsScreen_alt_closeInviteToGroupDialog)
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = stringResource(R.string.groupDetailsScreen_ui_inviteCodeDialogTitle),
                                        style = typography.displayMedium,
                                        color = localColorScheme.secondary
                                    )
                                    Text(
                                        text = state.groupDetails.inviteCode,
                                        style = typography.displayLarge.copy(
                                            letterSpacing = 4.sp
                                        ),
                                        color = localColorScheme.secondary
                                    )
                                    Row(
                                        modifier = Modifier.clickable {
                                            clipboardManager.setText(AnnotatedString(state.groupDetails.inviteCode))
                                        },
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = stringResource(R.string.groupDetailsScreen_ui_inviteCodeDialogCopyCodeButtonText),
                                            style = typography.bodyMedium,
                                            color = localColorScheme.secondary
                                        )
                                        Icon(
                                            imageVector = Icons.Filled.ContentCopy,
                                            contentDescription = stringResource(R.string.groupDetailsScreen_alt_inviteCodeDialogCopyCodeButton)
                                        )
                                    }

                                    Text(
                                        modifier = Modifier
                                            .padding(top = 8.dp)
                                            .padding(horizontal = 16.dp),
                                        text = stringResource(R.string.groupDetailsScreen_ui_inviteCodeDialogDescription),
                                        style = typography.bodyMedium.copy(
                                            textAlign = TextAlign.Center
                                        ),
                                        color = localColorScheme.secondary
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }
        is GroupDetailsState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.message,
                    style = typography.headlineMedium,
                    color = localColorScheme.secondary
                )
            }
        }
    }
}
