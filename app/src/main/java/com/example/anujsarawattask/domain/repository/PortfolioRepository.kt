package com.example.anujsarawattask.domain.repository

import com.example.anujsarawattask.domain.model.Holding
import kotlinx.coroutines.flow.Flow

interface PortfolioRepository {
    suspend fun fetchHoldings(): Result<List<Holding>>
    fun getHoldingsFlow(): Flow<List<Holding>>
    suspend fun refreshHoldings(): Result<Unit>
}