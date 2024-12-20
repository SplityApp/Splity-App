package com.igorj.splity.ui.composable.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.igorj.splity.R
import com.igorj.splity.model.TextFieldType
import com.igorj.splity.model.main.home.HomeState
import com.igorj.splity.ui.composable.TextField
import com.igorj.splity.ui.composable.main.groupDetails.GroupDetailsScreen
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography
import com.igorj.splity.util.LoadingController
import org.koin.androidx.compose.koinViewModel
import java.util.Currency
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = koinViewModel()
) {
    LaunchedEffect(true) {
        homeViewModel.refreshUserGroups()
    }

    val userGroupsState by homeViewModel.userGroups.collectAsStateWithLifecycle()
    val isRefreshing by homeViewModel.isRefreshing.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { homeViewModel.refreshUserGroups() })
    val navController = rememberNavController()
    val createGroupSheetState = rememberModalBottomSheetState()
    val joinGroupSheetState = rememberModalBottomSheetState()
    var showCreateGroupModalBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }
    var showJoinGroupModalBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }

    when (val state = userGroupsState) {
        HomeState.Loading -> {
            LaunchedEffect(true) {
                LoadingController.showLoading()
            }

            DisposableEffect(true) {
                onDispose {
                    LoadingController.hideLoading()
                }
            }
        }

        is HomeState.Success -> {
            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {
                    Scaffold(
                        modifier = modifier.fillMaxSize(),
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
                                    text = stringResource(R.string.homeScreen_ui_topLabel),
                                    style = typography.headlineMedium,
                                    color = localColorScheme.secondary,
                                )
                                IconButton(
                                    onClick = {
                                        showJoinGroupModalBottomSheet = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Link,
                                        contentDescription = "Join to group"
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        showCreateGroupModalBottomSheet = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = "Create group"
                                    )
                                }
                            }
                        },
                        content = { innerPadding ->
                            Box(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize()
                                    .pullRefresh(pullRefreshState),
                            ) {
                                if (state.userGroups.isEmpty()) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "No groups, join or create one",
                                            style = typography.headlineMedium,
                                            color = localColorScheme.secondary,
                                        )
                                    }
                                } else {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        items(state.userGroups) { group ->
                                            HomeCard(
                                                title = group.name,
                                                amount = group.myBalance,
                                                currency = group.currency,
                                                onClick = {
                                                    navController.navigate("groupDetails/${group.id}")
                                                }
                                            )
                                        }
                                    }
                                }

                                PullRefreshIndicator(
                                    modifier = Modifier.align(Alignment.TopCenter),
                                    refreshing = isRefreshing,
                                    state = pullRefreshState
                                )

                                if (showCreateGroupModalBottomSheet) {
                                    val focusManager = LocalFocusManager.current
                                    var name by rememberSaveable {
                                        mutableStateOf("")
                                    }
                                    var selectedCurrency by rememberSaveable {
                                        mutableStateOf(Currency.getInstance(Locale.getDefault()).currencyCode)
                                    }
                                    val currencies = remember {
                                        Currency.getAvailableCurrencies()
                                            .sortedBy { it.currencyCode }
                                            .filterCurrencies()
                                    }

                                    ModalBottomSheet(
                                        containerColor = localColorScheme.tertiaryContainer,
                                        sheetState = createGroupSheetState,
                                        onDismissRequest = {
                                            showCreateGroupModalBottomSheet = false
                                        },
                                        dragHandle = {

                                        }
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(horizontal = 24.dp)
                                                .padding(top = 16.dp, bottom = 32.dp),
                                            horizontalAlignment = Alignment.Start,
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.createGroupModalBottomSheet_ui_newGroupTitle),
                                                style = typography.headlineLarge,
                                                color = localColorScheme.secondary,
                                            )
                                            TextField(
                                                modifier = Modifier
                                                    .padding(top = 4.dp)
                                                    .fillMaxWidth(),
                                                label = stringResource(id = R.string.createGroupModalBottomSheet_ui_newGroupTextFieldHint),
                                                value = name,
                                                onValueChange = {
                                                    name = it
                                                },
                                                type = TextFieldType.Common,
                                                onImeAction = {
                                                    focusManager.clearFocus()
                                                },
                                                keyboardOptions = KeyboardOptions(
                                                    keyboardType = KeyboardType.Text,
                                                )
                                            )
                                            DropdownPicker(
                                                label = "Currency",
                                                modifier = Modifier.fillMaxWidth(),
                                                availableValues = currencies.map { DropdownPickerData(text = it.currencyCode) },
                                                selectedValue = selectedCurrency,
                                                onValueSelected = {
                                                    selectedCurrency = it
                                                }
                                            )
                                            Button(
                                                modifier = Modifier
                                                    .padding(top = 4.dp)
                                                    .align(Alignment.End),
                                                onClick = {
                                                    homeViewModel.createGroup(
                                                        name,
                                                        Currency.getInstance(selectedCurrency)
                                                    )
                                                    showCreateGroupModalBottomSheet = false
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = localColorScheme.primary
                                                )
                                            ) {
                                                Text(
                                                    text = stringResource(id = R.string.createGroupModalBottomSheet_ui_newGroupButtonLabel),
                                                    style = typography.bodyLarge.copy(
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    color = localColorScheme.secondary
                                                )
                                            }
                                        }
                                    }
                                }
                                if (showJoinGroupModalBottomSheet) {
                                    val focusManager = LocalFocusManager.current
                                    var inviteCode by rememberSaveable {
                                        mutableStateOf("")
                                    }

                                    ModalBottomSheet(
                                        containerColor = localColorScheme.tertiaryContainer,
                                        sheetState = joinGroupSheetState,
                                        onDismissRequest = {
                                            showJoinGroupModalBottomSheet = false
                                        },
                                        dragHandle = {

                                        }
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(horizontal = 24.dp)
                                                .padding(top = 16.dp, bottom = 32.dp),
                                            horizontalAlignment = Alignment.Start,
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = stringResource(R.string.joinGroupModalBottomSheet_ui_joinGroupTitle),
                                                style = typography.headlineLarge,
                                                color = localColorScheme.secondary,
                                            )
                                            TextField(
                                                modifier = Modifier
                                                    .padding(top = 4.dp)
                                                    .fillMaxWidth(),
                                                label = stringResource(R.string.joinGroupModalBottomSheet_ui_joinGroupTextFieldHint),
                                                value = inviteCode,
                                                onValueChange = {
                                                    inviteCode = it
                                                },
                                                type = TextFieldType.Common,
                                                onImeAction = {
                                                    focusManager.clearFocus()
                                                },
                                                keyboardOptions = KeyboardOptions(
                                                    keyboardType = KeyboardType.Text,
                                                )
                                            )
                                            Button(
                                                modifier = Modifier
                                                    .padding(top = 4.dp)
                                                    .align(Alignment.End),
                                                onClick = {
                                                    homeViewModel.joinGroup(inviteCode)
                                                    showJoinGroupModalBottomSheet = false
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = localColorScheme.primary
                                                )
                                            ) {
                                                Text(
                                                    text = stringResource(R.string.joinGroupModalBottomSheet_ui_joinGroupButtonLabel),
                                                    style = typography.bodyLarge.copy(
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    color = localColorScheme.secondary
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
                composable("groupDetails/{groupId}") { backStackEntry ->
                    val groupId = backStackEntry.arguments?.getString("groupId")
                    groupId?.let {
                        GroupDetailsScreen(groupId = it)
                    }
                }
            }
        }
        is HomeState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
                    .verticalScroll(rememberScrollState())
                    .background(localColorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.homeScreen_ui_errorLabel),
                    style = typography.headlineMedium,
                    color = localColorScheme.secondary
                )

                PullRefreshIndicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    refreshing = isRefreshing,
                    state = pullRefreshState
                )
            }
        }
    }
}

fun List<Currency>.filterCurrencies(): List<Currency> {
    val allowedCurrencies = setOf(
        "EUR",
        "PLN",
        "USD",
        "GBP",
        "JPY",
        "CHF"
    )

    return filter { currency ->
        currency.currencyCode in allowedCurrencies
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
