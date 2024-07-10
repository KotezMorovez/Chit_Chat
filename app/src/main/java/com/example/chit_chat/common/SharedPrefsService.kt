package com.example.chit_chat.common

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

interface SharedPrefsService{
    fun getAccessToken(): String
    fun getRefreshToken(): String
    fun setAccessToken(token: String)
    fun setRefreshToken(token: String)
}

class SharedPrefsServiceImpl @Inject constructor(
    private val sharedPrefs: SharedPreferences
):SharedPrefsService {

    override fun getAccessToken(): String {
        return sharedPrefs.getString(ACCESS_TOKEN, "") ?: ""
    }

    override fun setAccessToken(token: String) {
        sharedPrefs.edit().putString(ACCESS_TOKEN, token).apply()
    }

    override fun getRefreshToken(): String {
        return sharedPrefs.getString(REFRESH_TOKEN, "") ?: ""
    }

    override fun setRefreshToken(token: String) {
        sharedPrefs.edit().putString(REFRESH_TOKEN, token).apply()
    }
}

