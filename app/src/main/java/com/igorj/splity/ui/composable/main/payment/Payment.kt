package com.igorj.splity.ui.composable.main.payment

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.igorj.splity.model.payment.PaymentParticipant
import com.tpay.sdk.api.models.PayerContext
import com.tpay.sdk.api.models.SheetOpenResult
import com.tpay.sdk.api.models.payer.Payer
import com.tpay.sdk.api.models.transaction.SingleTransaction
import com.tpay.sdk.api.payment.Payment
import com.tpay.sdk.api.payment.PaymentDelegate
import com.tpay.sdk.api.screenless.Notifications
import java.util.Currency
import java.util.Locale

@Composable
fun Payment(
    amount: Double,
    currency: Currency,
    payer: PaymentParticipant,
    receiver: PaymentParticipant,
) {
    val context = LocalContext.current as? FragmentActivity ?: run {
        Log.e("Payment", "Invalid activity type")
        return
    }

    var isLoading by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "From user",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "From",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = payer.username,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = "to",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "To user",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "To",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = receiver.username,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(
                    text = String.format(Locale.getDefault(), "%.2f %s", amount, currency.symbol),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                onClick = {
                    isLoading = true

                    val description = buildString {
                        append("From: ${payer.username}, ")
                        append("To: ${receiver.username}, ")
                        append("Amount: ${amount}, ")
                        append("target: ${receiver.phoneNumber}")
                    }

                    val paymentSheet = Payment.Sheet(
                        transaction = SingleTransaction(
                            amount = amount,
                            description = description,
                            payerContext = PayerContext(
                                payer = Payer(
                                    name = payer.username,
                                    email = payer.email,
                                    phone = payer.phoneNumber,
                                    address = Payer.Address(
                                        city = "Warszawa",  // TODO: First empty, then after 1st completed payment get address from shared preferences
                                        postalCode = "00-000",
                                        countryCode = "PL",
                                        address = "ul. Testowa 1"
                                    )
                                )
                            ),
                            notifications = Notifications(
                                notificationEmail = "twoj@email.com",  // TODO: analyze if it's needed
                                notificationUrl = "https://twojadomena.pl/notification"
                            )
                        ),
                        activity = context,
                        supportFragmentManager = context.supportFragmentManager
                    )

                    paymentSheet.addObserver(object : PaymentDelegate {
                        override fun onPaymentCreated(transactionId: String?) {
                            Log.d("Payment", "Payment created: $transactionId")
                        }

                        override fun onPaymentCompleted(transactionId: String?) {
                            isLoading = false
                            Log.d("Payment", "Payment completed: $transactionId")
                        }

                        override fun onPaymentCancelled(transactionId: String?) {
                            isLoading = false
                            Log.d("Payment", "Payment cancelled: $transactionId")
                        }

                        override fun onModuleClosed() {
                            isLoading = false
                            Log.d("Payment", "Payment module closed")
                        }
                    })

                    val result = paymentSheet.present()
                    if (result !is SheetOpenResult.Success) {
                        isLoading = false
                        Log.e("Payment", "Failed to open payment module")
                    }
                }
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Pay Now")
                }
            }
        }
    }
}
