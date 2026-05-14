package com.apkidemand.feature.home.data

import com.google.firebase.Timestamp

data class DemandEntity(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userInitials: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val budget: Int = 0,
    val isNegotiable: Boolean = false,
    val areaName: String = "",
    val cityName: String = "",
    val imageUrls: List<String> = emptyList(),
    val timestamp: Timestamp = Timestamp.now(),
    val viewCount: Int = 0,
    val offerCount: Int = 0
)

data class OfferEntity(
    val id: String = "",
    val demandId: String = "",
    val sellerId: String = "",
    val shopName: String = "",
    val shopInitials: String = "",
    val title: String = "",
    val description: String = "",
    val price: Int = 0,
    val isNegotiable: Boolean = false,
    val imageUrls: List<String> = emptyList(),
    val timestamp: Timestamp = Timestamp.now()
)

data class UserEntity(
    val uid: String = "",
    val role: String = "",
    val name: String = "",
    val city: String = "",
    val email: String = ""
)
