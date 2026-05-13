package com.apkidemand.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DemandDao {
    @Query("SELECT * FROM demands ORDER BY cachedAt DESC")
    suspend fun getAllDemands(): List<DemandEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDemands(demands: List<DemandEntity>)

    @Query("DELETE FROM demands")
    suspend fun clearDemands()
}

@Dao
interface OfferDao {
    @Query("SELECT * FROM offers WHERE demandId = :demandId ORDER BY cachedAt DESC")
    suspend fun getOffersForDemand(demandId: String): List<OfferEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOffers(offers: List<OfferEntity>)
}

@Dao
interface ShopDao {
    @Query("SELECT * FROM shops WHERE id = :shopId")
    suspend fun getShopById(shopId: String): ShopEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShop(shop: ShopEntity)
}

