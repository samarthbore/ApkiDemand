package com.apkidemand.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "demands")
data class DemandEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val buyerId: String,
    val title: String,
    val description: String,
    val category: String,
    val photoUrls: List<String>,
    val status: String,
    val offerCount: Int,
    val expiresAt: Long,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "offers")
data class OfferEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val demandId: String,
    val sellerId: String,
    val shopId: String,
    val matchType: String,
    val photoUrls: List<String>,
    val price: Int,
    val isNegotiable: Boolean,
    val status: String,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "shops")
data class ShopEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val shopName: String,
    val categories: List<String>,
    val cityName: String,
    val lat: Double,
    val lng: Double,
    val ratingAvg: Double,
    val totalOffersSent: Int,
    val cachedAt: Long = System.currentTimeMillis()
)

