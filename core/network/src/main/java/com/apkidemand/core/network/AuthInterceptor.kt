package com.apkidemand.core.network

import com.apkidemand.core.security.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import java.util.UUID
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        val builder = originalRequest.newBuilder()
            .header("X-App-Version", "1.0.0") // Ideally from BuildConfig
            .header("X-Request-ID", UUID.randomUUID().toString())
            // X-Device-ID would go here

        tokenManager.getAccessToken()?.let { token ->
            builder.header("Authorization", "Bearer $token")
        }

        return chain.proceed(builder.build())
    }
}

