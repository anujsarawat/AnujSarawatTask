package com.example.anujsarawattask.domain.usecase

import com.example.anujsarawattask.domain.model.Holding
import com.example.anujsarawattask.domain.model.PortfolioSummary
import jakarta.inject.Inject

class CalculatePortfolioSummaryUseCase @Inject constructor() {
    operator fun invoke(holdings: List<Holding>): PortfolioSummary {
        if (holdings.isEmpty()) {
            return PortfolioSummary(
                currentValue = 0.0,
                totalInvestment = 0.0,
                totalPnl = 0.0,
                todaysPnl = 0.0
            )
        }

        val currentValue = holdings.sumOf { it.currentValue }
        val totalInvestment = holdings.sumOf { it.investmentValue }
        val totalPnl = currentValue - totalInvestment
        val todaysPnl = holdings.sumOf { it.todaysPnl }

        return PortfolioSummary(
            currentValue = currentValue,
            totalInvestment = totalInvestment,
            totalPnl = totalPnl,
            todaysPnl = todaysPnl
        )
    }
}