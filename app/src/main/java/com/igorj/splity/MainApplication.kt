package com.igorj.splity

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.igorj.splity.koin.appModule
import com.igorj.splity.service.PushNotificationService
import com.tpay.sdk.api.models.CertificatePinningConfiguration
import com.tpay.sdk.api.models.Environment
import com.tpay.sdk.api.models.Language
import com.tpay.sdk.api.models.PaymentMethod
import com.tpay.sdk.api.models.merchant.Merchant
import com.tpay.sdk.api.providers.MerchantDetailsProvider
import com.tpay.sdk.api.providers.SSLCertificatesProvider
import com.tpay.sdk.api.tpayModule.TpayModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(appModule)
        }

        configureTpay()
        createNotificationChannel()
    }

    private fun configureTpay() {
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

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            PushNotificationService.NOTIFICATION_CHANNEL_ID,
            "Payments",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = "Used to notify about recent payments"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
