package com.apkidemand.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apkidemand.feature.auth.data.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val uid: String, val role: String?) : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkCurrentUser()
    }

    fun checkCurrentUser() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            if (authRepository.isUserLoggedIn()) {
                val uid = authRepository.getCurrentUserId()!!
                val result = authRepository.getUserRole(uid)
                if (result.isSuccess) {
                    val role = result.getOrNull()
                    if (role != null) {
                        _authState.value = AuthState.Success(uid, role)
                    } else {
                        // User exists but hasn't set up a profile/role yet
                        _authState.value = AuthState.Success(uid, null)
                    }
                } else {
                    _authState.value = AuthState.Error("Failed to fetch user data")
                }
            } else {
                _authState.value = AuthState.Idle
            }
        }
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                auth.signInWithEmailAndPassword(email, pass).await()
                val uid = auth.currentUser?.uid ?: ""
                val roleResult = authRepository.getUserRole(uid)
                val role = roleResult.getOrNull()
                _authState.value = AuthState.Success(uid, role)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.localizedMessage ?: "Login failed")
            }
        }
    }

    fun register(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                auth.createUserWithEmailAndPassword(email, pass).await()
                val uid = auth.currentUser?.uid ?: ""
                _authState.value = AuthState.Success(uid, null) // Needs profile setup
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.localizedMessage ?: "Registration failed")
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(credential).await()
                val uid = auth.currentUser?.uid ?: ""
                val roleResult = authRepository.getUserRole(uid)
                val role = roleResult.getOrNull()
                _authState.value = AuthState.Success(uid, role)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.localizedMessage ?: "Google Sign-In failed")
            }
        }
    }

    fun saveProfile(role: String, name: String, city: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val uid = auth.currentUser?.uid
            if (uid != null) {
                try {
                    // We initiate the write but we don't 'await()' for server confirmation 
                    // before navigating. Firestore handles local persistence and background syncing.
                    // This prevents the 'infinite loading' or 'timeout' issues if the network is slow.
                    authRepository.saveUserRole(uid, role, name, city)
                    
                    // Proceed to success immediately
                    _authState.value = AuthState.Success(uid, role)
                } catch (e: Exception) {
                    _authState.value = AuthState.Error(e.localizedMessage ?: "An unexpected error occurred")
                }
            } else {
                _authState.value = AuthState.Error("User not authenticated. Please log in again.")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }

    fun setError(message: String) {
        _authState.value = AuthState.Error(message)
    }
}
