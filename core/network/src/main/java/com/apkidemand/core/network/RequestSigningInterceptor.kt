package com.apkidemand.core.network

import okhttp3.Interceptor
import okhttp3.Response
import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class RequestSigningInterceptor @Inject constructor() : Interceptor {
    private val appSecret = "your_app_secret_here" // In practice, load securely

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val timestamp = System.currentTimeMillis().toString()
        val path = request.url.encodedPath

        // A basic example of HMAC-SHA256 signing
        // bodyHash should ideally be calculated if body is present
        val signatureData = "$timestamp$path" 
        val signature = calculateHmacSha256(signatureData, appSecret)

        val newRequest = request.newBuilder()
            .header("X-Timestamp", timestamp)
            .header("X-Signature", signature)
            .build()

        return chain.proceed(newRequest)
    }

    private fun calculateHmacSha256(data: String, key: String): String {
        val mac = Mac.getInstance("HmacSHA256")
        val secretKey = SecretKeySpec(key.toByteArray(), "HmacSHA256")
        mac.init(secretKey)
        val hmacData = mac.doFinal(data.toByteArray())
        return hmacData.joinToString("") { "%02x".format(it) }
    }
}

