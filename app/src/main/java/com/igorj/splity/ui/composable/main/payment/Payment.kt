package com.igorj.splity.ui.composable.main.payment

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.wallet.Wallet
import com.igorj.splity.BuildConfig
import com.igorj.splity.model.payment.PaymentParticipant
import com.tpay.sdk.api.models.*
import com.tpay.sdk.api.models.merchant.Merchant
import com.tpay.sdk.api.models.payer.Payer
import com.tpay.sdk.api.models.transaction.SingleTransaction
import com.tpay.sdk.api.payment.Payment
import com.tpay.sdk.api.payment.PaymentDelegate
import com.tpay.sdk.api.providers.MerchantDetailsProvider
import com.tpay.sdk.api.providers.SSLCertificatesProvider
import com.tpay.sdk.api.screenless.Notifications
import com.tpay.sdk.api.tpayModule.TpayModule

@Composable
fun Payment(
    amount: Double,
    payer: PaymentParticipant,
    receiver: PaymentParticipant,
) {
    val context = LocalContext.current as? FragmentActivity ?: run {
        Log.e("Payment", "Invalid activity type")
        return
    }

    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        TpayModule.configure(object : SSLCertificatesProvider {
            override var apiConfiguration: CertificatePinningConfiguration =
                CertificatePinningConfiguration(
                    publicKeyHash = BuildConfig.TPAY_PUBLIC_KEY_HASH,
                )
        })

        TpayModule.configure(
            Merchant(
                authorization = Merchant.Authorization(
                    clientId = BuildConfig.TPAY_CLIENT_ID,
                    clientSecret = BuildConfig.TPAY_CLIENT_SECRET
                )
            )
        )

        TpayModule.configure(object : MerchantDetailsProvider {
            override fun merchantDisplayName(language: Language): String {
                return when (language) {
                    Language.PL -> "polish name"
                    Language.EN -> "english name"
                }
            }

            override fun merchantCity(language: Language): String {
                return when (language) {
                    Language.PL -> "Warszawa"
                    Language.EN -> "Warsaw"
                }
            }

            override fun regulationsLink(language: Language): String {
                return when (language) {
                    Language.PL -> "polish regulation url"
                    Language.EN -> "english regulation url"
                }
            }
        })

        TpayModule.configure(Environment.SANDBOX)

        TpayModule.configure(
            preferredLanguage = Language.PL,
            supportedLanguages = listOf(Language.PL, Language.EN)
        )

        TpayModule.configure(
            paymentMethods = listOf(
                PaymentMethod.Card,
                PaymentMethod.Blik,
                PaymentMethod.Pbl,
            )
        )
    }

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
                // From section
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

                // Arrow
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = "to",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                // To section
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
                    text = String.format("%.2f", amount), //TODO: Tutaj należy dodać walutę, najlepiej na podstaiwe jakiegoś enuma który będzie zrobiony pryz okazji robienia taska z grupami
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
                                        city = "Warszawa",
                                        postalCode = "00-000",
                                        countryCode = "PL",
                                        address = "ul. Testowa 1"
                                    )
                                )
                            ),
                            notifications = Notifications(
                                notificationEmail = "twoj@email.com",
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
