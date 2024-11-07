package com.igorj.splity.ui.composable.main.groupDetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.igorj.splity.model.main.groupDetails.GroupDetailsState
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GroupDetailsScreen(
    groupDetailsViewModel: GroupDetailsViewModel = koinViewModel(),
    groupId: String
) {
    LaunchedEffect(true) {
        groupDetailsViewModel.getGroupDetails(groupId)
    }

    val groupDetailsState by groupDetailsViewModel.groupDetails.collectAsStateWithLifecycle()
    val isRefreshing by groupDetailsViewModel.isRefreshing.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { groupDetailsViewModel.getGroupDetails(groupId) })

    when (val state = groupDetailsState) {
        GroupDetailsState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is GroupDetailsState.Success -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                backgroundColor = localColorScheme.background,
                topBar = {
                    Text(
                        text = state.groupDetails.name,
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
                            items(state.groupDetails.expenses) { expense ->
                                ExpenseCard(
                                    expense = expense,
                                    currency = state.groupDetails.currency
                                )
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