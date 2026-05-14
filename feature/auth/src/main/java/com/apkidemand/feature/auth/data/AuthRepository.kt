package com.apkidemand.feature.auth.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun saveUserRole(uid: String, role: String, name: String, city: String): Result<Unit> {
        return try {
            val userMap = mapOf(
                "uid" to uid,
                "role" to role,
                "name" to name,
                "city" to city,
                "email" to (auth.currentUser?.email ?: "")
            )
            // We don't await() here to avoid hanging on slow connections.
            // Firestore will queue this write locally and sync in the background.
            firestore.collection("users").document(uid).set(userMap)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUserRole(uid: String): Result<String?> {
        return try {
            val snapshot = firestore.collection("users").document(uid).get().await()
            val role = snapshot.getString("role")
            Result.success(role)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfileMap(uid: String): Result<Map<String, Any>?> {
        return try {
            val snapshot = firestore.collection("users").document(uid).get().await()
            if (snapshot.exists()) {
                Result.success(snapshot.data)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
    
    fun logout() {
        auth.signOut()
    }
}
