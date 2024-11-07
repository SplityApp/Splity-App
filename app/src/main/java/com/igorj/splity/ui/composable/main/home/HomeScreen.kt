package com.igorj.splity.ui.composable.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.igorj.splity.R
import com.igorj.splity.model.TextFieldType
import com.igorj.splity.model.main.home.HomeState
import com.igorj.splity.ui.composable.TextField
import com.igorj.splity.ui.theme.DarkGrey
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography
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
        homeViewModel.getUserGroups()
    }

    val userGroupsState by homeViewModel.userGroups.collectAsStateWithLifecycle()
    val isRefreshing by homeViewModel.isRefreshing.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { homeViewModel.getUserGroups() })

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }

    when (val state = userGroupsState) {
        HomeState.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(localColorScheme.background.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is HomeState.Success -> {
            Scaffold(
                modifier = modifier.fillMaxSize(),
                backgroundColor = localColorScheme.background,
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        text = {
                            Text("Show bottom sheet")
                        },
                        icon = {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = ""
                            )
                        },
                        onClick = {
                            showBottomSheet = true
                        }
                    )
                },
                topBar = {
                    Text(
                        text = stringResource(R.string.homeScreen_ui_topLabel),
                        style = typography.headlineMedium,
                        color = localColorScheme.secondary,
                        modifier = Modifier.padding(16.dp)
                    )
                },
                content = { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .pullRefresh(pullRefreshState),
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.userGroups) { group ->
                                HomeCard(
                                    title = group.name,
                                    amount = group.myBalance,
                                    currency = group.currency
                                )
                            }
                        }

                        PullRefreshIndicator(
                            modifier = Modifier.align(Alignment.TopCenter),
                            refreshing = isRefreshing,
                            state = pullRefreshState
                        )

                        if (showBottomSheet) {
                            val focusManager = LocalFocusManager.current
                            var name by rememberSaveable {
                                mutableStateOf("")
                            }
                            var selectedCurrency by rememberSaveable {
                                mutableStateOf(Currency.getInstance(Locale.getDefault()))
                            }

                            ModalBottomSheet(
                                containerColor = DarkGrey,
                                sheetState = sheetState,
                                onDismissRequest = {
                                    showBottomSheet = false
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
                                    CurrencyPicker(
                                        modifier = Modifier.fillMaxWidth(),
                                        selectedCurrency = selectedCurrency,
                                        onCurrencySelected = {
                                            selectedCurrency = it
                                        }
                                    )
                                    Button(
                                        modifier = Modifier
                                            .padding(top = 4.dp)
                                            .align(Alignment.End),
                                        onClick = {
                                            homeViewModel.createGroup(name, selectedCurrency)
                                            showBottomSheet = false
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
                    }
                }
            )
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

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
