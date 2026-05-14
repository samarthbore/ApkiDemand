package com.apkidemand.core.network

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class CloudinaryManager @Inject constructor() {

    /**
     * Uploads an image to Cloudinary and returns the secure URL.
     * @param uri The local Uri of the image to upload.
     * @param folder Optional folder in Cloudinary.
     */
    suspend fun uploadImage(uri: Uri, folder: String = "demands"): String? {
        return suspendCancellableCoroutine { continuation ->
            MediaManager.get().upload(uri)
                .option("folder", folder)
                .option("resource_type", "image")
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String) {
                        // Started
                    }

                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                        // Progress
                    }

                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        val url = resultData["secure_url"] as? String
                        if (continuation.isActive) {
                            continuation.resume(url)
                        }
                    }

                    override fun onError(requestId: String, error: ErrorInfo) {
                        if (continuation.isActive) {
                            continuation.resume(null)
                        }
                    }

                    override fun onReschedule(requestId: String, error: ErrorInfo) {
                        if (continuation.isActive) {
                            continuation.resume(null)
                        }
                    }
                })
                .dispatch()
        }
    }
}
