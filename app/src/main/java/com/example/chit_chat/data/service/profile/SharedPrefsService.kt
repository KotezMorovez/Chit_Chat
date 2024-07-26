package com.example.chit_chat.data.service.profile

import android.content.SharedPreferences
import com.example.chit_chat.common.ACCESS_TOKEN
import com.example.chit_chat.common.REFRESH_TOKEN
import javax.inject.Inject

interface SharedPrefsService{
    fun getAccessToken(): String
    fun getRefreshToken(): String
    fun setAccessToken(token: String)
    fun setRefreshToken(token: String)
    fun deletePrefs()
}

class SharedPrefsServiceImpl @Inject constructor(
    private val sharedPrefs: SharedPreferences
): SharedPrefsService {

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

    override fun deletePrefs() {
        sharedPrefs.edit().clear().apply()
    }
}

