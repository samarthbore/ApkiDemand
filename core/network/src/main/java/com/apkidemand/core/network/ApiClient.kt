package com.apkidemand.core.network

import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.CertificatePinner
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiClient @Inject constructor(
    private val authInterceptor: AuthInterceptor,
    private val tokenRefreshAuthenticator: TokenRefreshAuthenticator,
    private val requestSigningInterceptor: RequestSigningInterceptor
) {
    companion object {
        private const val BASE_URL = "https://api.apkidemand.in/v1/"
    }

    private val json = Json { ignoreUnknownKeys = true }

    private val certificatePinner = CertificatePinner.Builder()
        .add("api.apkidemand.in", "sha256/XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX=") // Replace with actual pin
        .add("api.apkidemand.in", "sha256/YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY=") // Backup pin
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(requestSigningInterceptor)
        .authenticator(tokenRefreshAuthenticator)
        .certificatePinner(certificatePinner)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // TODO: Change to NONE in release
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
}

