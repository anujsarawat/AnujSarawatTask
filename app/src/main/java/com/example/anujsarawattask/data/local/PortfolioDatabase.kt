package com.example.anujsarawattask.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.anujsarawattask.data.local.dao.HoldingDao
import com.example.anujsarawattask.data.local.entities.HoldingEntity

@Database(entities = [HoldingEntity::class], version = 1, exportSchema = false)
abstract class PortfolioDatabase : RoomDatabase() {
    abstract fun holdingDao(): HoldingDao
}