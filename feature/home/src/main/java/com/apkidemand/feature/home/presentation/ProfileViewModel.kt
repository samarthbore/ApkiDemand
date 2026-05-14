package com.apkidemand.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apkidemand.feature.auth.data.AuthRepository
import com.apkidemand.feature.home.data.DemandEntity
import com.apkidemand.feature.home.data.MarketRepository
import com.apkidemand.feature.home.data.OfferEntity
import com.apkidemand.feature.home.data.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val marketRepository: MarketRepository
) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserEntity?>(null)
    val userProfile: StateFlow<UserEntity?> = _userProfile.asStateFlow()

    private val _myDemands = MutableStateFlow<List<DemandEntity>>(emptyList())
    val myDemands: StateFlow<List<DemandEntity>> = _myDemands.asStateFlow()

    private val _myOffers = MutableStateFlow<List<OfferEntity>>(emptyList())
    val myOffers: StateFlow<List<OfferEntity>> = _myOffers.asStateFlow()

    val currentUserId: String
        get() = authRepository.getCurrentUserId() ?: ""

    init {
        loadUserProfile()
        loadUserContent()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            if (currentUserId.isNotEmpty()) {
                val result = authRepository.getUserProfileMap(currentUserId)
                if (result.isSuccess) {
                    val data = result.getOrNull()
                    if (data != null) {
                        _userProfile.value = UserEntity(
                            uid = data["uid"] as? String ?: "",
                            role = data["role"] as? String ?: "",
                            name = data["name"] as? String ?: "",
                            city = data["city"] as? String ?: "",
                            email = data["email"] as? String ?: ""
                        )
                    }
                }
            }
        }
    }

    private fun loadUserContent() {
        viewModelScope.launch {
            if (currentUserId.isNotEmpty()) {
                marketRepository.getUserDemands(currentUserId)
                    .catch { /* Handle error */ }
                    .collect { demands ->
                        _myDemands.value = demands
                    }
            }
        }
        
        viewModelScope.launch {
            if (currentUserId.isNotEmpty()) {
                marketRepository.getUserOffers(currentUserId)
                    .catch { /* Handle error */ }
                    .collect { offers ->
                        _myOffers.value = offers
                    }
            }
        }
    }

    fun logout() {
        authRepository.logout()
    }
}
