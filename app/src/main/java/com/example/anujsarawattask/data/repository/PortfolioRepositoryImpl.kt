package com.example.anujsarawattask.data.repository

import com.example.anujsarawattask.data.local.dao.HoldingDao
import com.example.anujsarawattask.data.local.entities.toDomain
import com.example.anujsarawattask.data.local.entities.toEntity
import com.example.anujsarawattask.data.remote.api.PortfolioApiService
import com.example.anujsarawattask.data.remote.dto.toDomain
import com.example.anujsarawattask.domain.model.Holding
import com.example.anujsarawattask.domain.repository.PortfolioRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PortfolioRepositoryImpl @Inject constructor(
    private val apiService: PortfolioApiService,
    private val holdingDao: HoldingDao
) : PortfolioRepository {
    override fun getHoldingsFlow(): Flow<List<Holding>> {
        return holdingDao.getAllHoldings()
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun fetchHoldings(): Result<List<Holding>> {
        return try {
            val response = apiService.getHoldings()

            if (response.isSuccessful) {
                val holdingDtos = response.body()?.data?.holdings ?: emptyList()
                val holdings = holdingDtos.mapNotNull { it.toDomain() }

                holdingDao.replaceAll(holdings.map { it.toEntity() })

                Result.success(holdings)
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refreshHoldings(): Result<Unit> {
        return fetchHoldings().map { }
    }
}