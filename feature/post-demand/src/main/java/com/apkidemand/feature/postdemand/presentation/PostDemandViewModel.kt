package com.apkidemand.feature.postdemand.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apkidemand.feature.home.data.DemandEntity
import com.apkidemand.feature.home.data.MarketRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.apkidemand.feature.postdemand.BuildConfig
import com.apkidemand.core.network.CloudinaryManager
import android.net.Uri

@HiltViewModel
class PostDemandViewModel @Inject constructor(
    private val marketRepository: MarketRepository,
    private val auth: FirebaseAuth,
    private val cloudinaryManager: CloudinaryManager
) : ViewModel() {

    private val _isPosting = MutableStateFlow(false)
    val isPosting: StateFlow<Boolean> = _isPosting.asStateFlow()

    private val _postResult = MutableStateFlow<Result<Unit>?>(null)
    val postResult: StateFlow<Result<Unit>?> = _postResult.asStateFlow()

    fun postDemand(
        title: String,
        description: String,
        category: String,
        budget: Int,
        isUrgent: Boolean,
        imageUris: List<String>
    ) {
        viewModelScope.launch {
            _isPosting.value = true
            
            // 1. Upload Images to Cloudinary
            val uploadedUrls = uploadToCloudinary(imageUris)
            
            // 2. Create DemandEntity
            val uid = auth.currentUser?.uid ?: "unknown_user"
            val demand = DemandEntity(
                userId = uid,
                userName = "Buyer", // In a real app, fetch from AuthRepository
                userInitials = "BY",
                title = title,
                description = description,
                category = category,
                budget = budget,
                isNegotiable = true,
                areaName = "Local Area", // Could be fetched from GPS/Profile
                cityName = "City",
                imageUrls = uploadedUrls,
                viewCount = 0,
                offerCount = 0
            )
            
            // 3. Save to Firestore
            val result = marketRepository.postDemand(demand)
            _postResult.value = result
            _isPosting.value = false
        }
    }

    private suspend fun uploadToCloudinary(uris: List<String>): List<String> {
        val urls = mutableListOf<String>()
        uris.forEach { uriString ->
            val uri = Uri.parse(uriString)
            val resultUrl = cloudinaryManager.uploadImage(uri)
            if (resultUrl != null) {
                urls.add(resultUrl)
            }
        }
        return urls
    }
    
    fun resetState() {
        _postResult.value = null
    }
}
