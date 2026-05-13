package com.apkidemand.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [DemandEntity::class, OfferEntity::class, ShopEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun demandDao(): DemandDao
    abstract fun offerDao(): OfferDao
    abstract fun shopDao(): ShopDao
}

