package com.igorj.splity.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.igorj.splity.model.main.UserInfo

class UserInfoRepository(private val sharedPreferences: SharedPreferences) {
    fun saveUserInfo(userInfo: UserInfo) {
        val userInfoAsJson = Gson().toJson(userInfo)
        sharedPreferences.edit().putString(USER_INFO_KEY, userInfoAsJson).apply()
    }

    fun getUserInfo(): UserInfo? {
        val userInfoAsJson = sharedPreferences.getString(USER_INFO_KEY, null)
        return Gson().fromJson(userInfoAsJson, UserInfo::class.java)
    }

    fun clearUserInfo() {
        sharedPreferences.edit().remove(USER_INFO_KEY).apply()
    }

    private companion object {
        const val USER_INFO_KEY = "user_info_key"
    }
}
