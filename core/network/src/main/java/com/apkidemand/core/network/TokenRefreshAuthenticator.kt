package com.apkidemand.core.network

import com.apkidemand.core.security.TokenManager
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenRefreshAuthenticator @Inject constructor(
    private val tokenManager: TokenManager
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request.header("Authorization") == null) {
            return null // Not an authenticated request
        }

        // Logic to refresh token goes here (synchronous)
        // val newToken = ...
        // tokenManager.saveAccessToken(newToken)

        return response.request.newBuilder()
            // .header("Authorization", "Bearer $newToken")
            .build()
    }
}

