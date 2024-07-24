package com.example.chit_chat.data.service.auth

import com.example.chit_chat.common.JSONConverter
import com.example.chit_chat.data.model.UserTokenEntity
import com.example.chit_chat.data.service.SharedPrefsService
import com.example.chit_chat.ui.common.LogoutHandler
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

class TokenInterceptor(
    private val sharedPrefs: SharedPrefsService,
    private val logoutHandler: LogoutHandler
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var accessToken = sharedPrefs.getAccessToken()
        val refreshToken = sharedPrefs.getRefreshToken()
        val request = chain.request()

        val requestWithToken = createOriginalRequest(request, accessToken)
        val originalResponse = chain.proceed(requestWithToken)


        if (originalResponse.code >= 400) {
            originalResponse.close()
            val tokenEntity = UserTokenEntity(
                accessToken = accessToken,
                refreshToken = refreshToken
            )

            val newRequest = createRefreshTokensRequest(request, tokenEntity)
            val newResponse = chain.proceed(newRequest)

            if (newResponse.code >= 400) {
                clearPrefsAndLogout()
                return originalResponse
            }

            val newResponseBody = newResponse.body
            if (newResponseBody != null) {

                val json = String(newResponseBody.bytes())
                val newTokens = JSONConverter().unpackObjectFromJSON<UserTokenEntity>(json)
                if (newTokens != null) {
                    accessToken = newTokens.accessToken
                    sharedPrefs.setAccessToken(accessToken)
                    sharedPrefs.setRefreshToken(newTokens.refreshToken)
                } else {
                    clearPrefsAndLogout()
                }
            }

            newResponse.close()
            val retryRequest = createOriginalRequest(request, accessToken)
            val retryResponse = chain.proceed(retryRequest)

            if (retryResponse.code >= 400) {
                clearPrefsAndLogout()
                return originalResponse
            }
            return retryResponse
        } else {
            return originalResponse
        }
    }

    private fun createOriginalRequest(request: Request, accessToken: String): Request {
        return request.newBuilder()
            .header(AUTH, "Bearer $accessToken")
            .build()
    }

    private fun createRefreshTokensRequest(
        request: Request,
        tokenEntity: UserTokenEntity
    ): Request {
        return request.newBuilder()
            .post(
                Gson()
                    .toJson(tokenEntity)
                    .toRequestBody("application/json".toMediaTypeOrNull())
            )
            .url(REFRESH_URL)
            .build()
    }

    private fun clearPrefsAndLogout() {
        runBlocking {
            sharedPrefs.deletePrefs()
            logoutHandler.logout()
        }
    }

    companion object {
        private const val REFRESH_URL = "http://10.0.2.2:8080/refreshToken"
        private const val AUTH = "Authorization"
    }
}