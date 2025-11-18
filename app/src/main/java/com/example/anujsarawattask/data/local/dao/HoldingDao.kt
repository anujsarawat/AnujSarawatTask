package com.example.anujsarawattask.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.anujsarawattask.data.local.entities.HoldingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HoldingDao {
    @Query("SELECT * FROM holdings")
    fun getAllHoldings(): Flow<List<HoldingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(holdings: List<HoldingEntity>)

    @Query("DELETE FROM holdings")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(holdings: List<HoldingEntity>) {
        deleteAll()
        insertAll(holdings)
    }
}