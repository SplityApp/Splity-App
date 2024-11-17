package com.igorj.splity.ui.composable.main.groupDetails.balance

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.igorj.splity.R
import com.igorj.splity.model.main.balance.BalanceState
import com.igorj.splity.ui.composable.main.home.HomeCard
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BalancesScreen(
    balancesViewModel: BalancesViewModel = koinViewModel(),
    groupId: String
) {
    LaunchedEffect(true) {
        balancesViewModel.getBalances(groupId)
    }

    val balancesState by balancesViewModel.balances.collectAsStateWithLifecycle()
    val isRefreshing by balancesViewModel.isRefreshing.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { balancesViewModel.getBalances(groupId) })

    when (val state = balancesState) {
        BalanceState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is BalanceState.Success -> {
            Column {
                HomeCard(
                    title = stringResource(R.string.balanceScreen_ui_yourBalanceCardTitle),
                    amount = state.balancesResponse.requestUser.balance,
                    currency = state.balancesResponse.currency,
                    onClick = {}
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    backgroundColor = localColorScheme.background,
                    topBar = {
                        Text(
                            text = stringResource(R.string.balanceScreen_ui_balancesSectionLabel),
                            style = typography.headlineSmall,
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
                                state.balancesResponse.users.forEach { balance ->
                                    item {
                                        HomeCard(
                                            title = balance.name,
                                            amount = balance.balance,
                                            currency = state.balancesResponse.currency,
                                            onClick = {}
                                        )
                                    }
                                }
                            }
                            PullRefreshIndicator(
                                modifier = Modifier.align(Alignment.TopCenter),
                                refreshing = isRefreshing,
                                state = pullRefreshState
                            )
                        }
                    }
                )
            }
        }
        is BalanceState.Error -> {
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