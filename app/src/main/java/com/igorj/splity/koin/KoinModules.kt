package com.igorj.splity.koin

import android.content.Context
import android.content.SharedPreferences
import com.igorj.splity.AuthViewModel
import com.igorj.splity.ProfileViewModel
import com.igorj.splity.api.AuthApi
import com.igorj.splity.api.ExpenseApi
import com.igorj.splity.api.FcmApi
import com.igorj.splity.api.GroupApi
import com.igorj.splity.api.HomeApi
import com.igorj.splity.api.ProfileApi
import com.igorj.splity.api.StatsApi
import com.igorj.splity.repository.UserInfoRepository
import com.igorj.splity.ui.composable.main.groupDetails.GroupDetailsViewModel
import com.igorj.splity.ui.composable.main.groupDetails.balance.BalancesViewModel
import com.igorj.splity.ui.composable.main.groupDetails.expense.ExpenseViewModel
import com.igorj.splity.ui.composable.main.home.HomeViewModel
import com.igorj.splity.ui.composable.main.stats.StatsViewModel
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
        AuthViewModel(get(), get(), get(), get())
    }

    viewModel {
        HomeViewModel(get())
    }

    viewModel {
        GroupDetailsViewModel(get(), get())
    }

    viewModel {
        ExpenseViewModel(get())
    }

    viewModel {
        BalancesViewModel(get(), get())
    }

    viewModel {
        ProfileViewModel(get(), get())
    }

    viewModel {
        StatsViewModel(get())
    }

    single<SharedPreferences>(named(TOKEN_MANAGER_SHARED_PREFERENCES)){
        androidContext().getSharedPreferences(
            TOKEN_MANAGER_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    single { TokenManager(get(named(TOKEN_MANAGER_SHARED_PREFERENCES))) }


    single<SharedPreferences>(named(USER_INFO_SHARED_PREFERENCES)){
        androidContext().getSharedPreferences(
            USER_INFO_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    single { UserInfoRepository(get(named(USER_INFO_SHARED_PREFERENCES))) }

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

    single { get<Retrofit>().create(HomeApi::class.java) }

    single { get<Retrofit>().create(GroupApi::class.java) }

    single { get<Retrofit>().create(ProfileApi::class.java) }

    single { get<Retrofit>().create(StatsApi::class.java) }

    single { get<Retrofit>().create(FcmApi::class.java) }

    single { get<Retrofit>().create(ExpenseApi::class.java) }
}

const val BASE_URL = "https://bajqihucgsmrbpagxhvv.supabase.co"
const val TOKEN_MANAGER_SHARED_PREFERENCES = "token_manager_shared_preferences"
const val USER_INFO_SHARED_PREFERENCES = "user_info_shared_preferences"
