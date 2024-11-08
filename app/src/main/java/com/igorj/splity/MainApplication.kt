package com.igorj.splity

import android.app.Application
import com.igorj.splity.koin.appModule
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
}
