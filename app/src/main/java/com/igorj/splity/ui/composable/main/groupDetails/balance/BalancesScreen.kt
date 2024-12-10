package com.igorj.splity.ui.composable.main.groupDetails.balance

import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.igorj.splity.ProfileViewModel
import com.igorj.splity.R
import com.igorj.splity.model.main.balance.BalanceState
import com.igorj.splity.model.main.expense.ExpenseEvent
import com.igorj.splity.model.main.profile.UserInfoState
import com.igorj.splity.ui.composable.main.groupDetails.expense.ExpenseViewModel
import com.igorj.splity.ui.composable.main.home.HomeCard
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography
import com.igorj.splity.util.LoadingController
import com.tpay.sdk.api.models.PayerContext
import com.tpay.sdk.api.models.SheetOpenResult
import com.tpay.sdk.api.models.payer.Payer
import com.tpay.sdk.api.models.transaction.SingleTransaction
import com.tpay.sdk.api.payment.Payment
import com.tpay.sdk.api.payment.PaymentDelegate
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BalancesScreen(
    balancesViewModel: BalancesViewModel = koinViewModel(),
    profileViewModel: ProfileViewModel = koinViewModel(),
    expenseViewModel: ExpenseViewModel = koinViewModel(),
    groupId: String,
    currency: String
) {
    LaunchedEffect(true) {
        balancesViewModel.getBalances(groupId)
    }

    LaunchedEffect(true) {
        expenseViewModel.events.collect { event ->
            when (event) {
                ExpenseEvent.ExpenseAdded -> {
                    balancesViewModel.getBalances(groupId)
                }
            }
        }
    }

    val context = LocalContext.current as? FragmentActivity ?: run {
        Log.e("Payment", "Invalid activity type")
        return
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
                    currency = currency,
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
                                            currency = currency,
                                            onClick = {  // TODO Move all of this to separate function
                                                if (balance.balance <= 0.0) {
                                                    return@HomeCard
                                                }
                                                LoadingController.showLoading()

                                                val userInfo = profileViewModel.userInfoState.value
                                                if (userInfo !is UserInfoState.Success) {
                                                    LoadingController.hideLoading()
                                                    return@HomeCard
                                                }

                                                val description = buildString {
                                                    append("From: ${userInfo.userInfo.id}, ")
                                                    append("To: ${balance.id}, ")
                                                    append("Amount: ${balance.balance}, ")
                                                }

                                                val paymentSheet = Payment.Sheet(
                                                    transaction = SingleTransaction(
                                                        amount = balance.balance,
                                                        description = description,
                                                        payerContext = PayerContext(
                                                            payer = Payer(
                                                                name = userInfo.userInfo.username,
                                                                email = userInfo.userInfo.email,
                                                                phone = userInfo.userInfo.phoneNumber,
                                                                address = Payer.Address(
                                                                    city = "Warszawa",
                                                                    postalCode = "00-000",
                                                                    countryCode = "PL",
                                                                    address = "ul. Testowa 1"
                                                                )
                                                            )
                                                        ),
                                                        notifications = null
                                                    ),
                                                    activity = context,
                                                    supportFragmentManager = context.supportFragmentManager
                                                )

                                                paymentSheet.addObserver(object : PaymentDelegate {
                                                    override fun onPaymentCreated(transactionId: String?) {
                                                        Log.d("Payment", "Payment created: $transactionId")
                                                    }

                                                    override fun onPaymentCompleted(transactionId: String?) {
                                                        LoadingController.hideLoading()
                                                        balancesViewModel.sendPushNotification(
                                                            balance.id,
                                                            "${userInfo.userInfo.username} sent you ${balance.balance} $currency",
                                                            "Check your balance"
                                                        )
                                                        Log.d("Payment", "Payment completed: $transactionId")
                                                    }

                                                    override fun onPaymentCancelled(transactionId: String?) {
                                                        LoadingController.hideLoading()
                                                        Log.d("Payment", "Payment cancelled: $transactionId")
                                                    }

                                                    override fun onModuleClosed() {
                                                        LoadingController.hideLoading()
                                                        Log.d("Payment", "Payment module closed")
                                                    }
                                                })

                                                val result = paymentSheet.present()
                                                if (result !is SheetOpenResult.Success) {
                                                    LoadingController.hideLoading()
                                                    Log.e("Payment", "Failed to open payment module")
                                                }
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