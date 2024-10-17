package com.igorj.splity.koin

import android.content.Context
import android.content.SharedPreferences
import com.igorj.splity.api.AuthApi
import com.igorj.splity.ui.composable.AuthViewModel
import com.igorj.splity.util.auth.AuthInterceptor
import com.igorj.splity.util.auth.TokenManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    viewModel {
        AuthViewModel(get(), get())
    }

    single<SharedPreferences>(named(TOKEN_MANAGER_SHARED_PREFERENCES)){
        androidContext().getSharedPreferences(
            TOKEN_MANAGER_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    single { TokenManager(get(named(TOKEN_MANAGER_SHARED_PREFERENCES))) }

    single<Interceptor> { AuthInterceptor(get()) }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("${BASE_URL}/functions/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(get())
                    .build()
            )
            .build()
    }

    single { get<Retrofit>().create(AuthApi::class.java) }
}

const val TOKEN_MANAGER_SHARED_PREFERENCES = "token_manager_shared_preferences"
const val BASE_URL = "https://bajqihucgsmrbpagxhvv.supabase.co"
