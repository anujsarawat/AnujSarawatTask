package com.example.anujsarawattask.domain.usecase

import com.example.anujsarawattask.domain.model.Holding
import com.example.anujsarawattask.domain.repository.PortfolioRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetHoldingsUseCase @Inject constructor(
    private val repository: PortfolioRepository
) {
    operator fun invoke(): Flow<List<Holding>> {
        return repository.getHoldingsFlow()
    }
    suspend fun refresh(): Result<Unit> {
        return repository.refreshHoldings()
    }
}