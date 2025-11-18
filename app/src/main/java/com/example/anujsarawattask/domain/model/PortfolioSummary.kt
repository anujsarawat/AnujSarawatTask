package com.example.anujsarawattask.domain.model

data class PortfolioSummary(
    val currentValue: Double,
    val totalInvestment: Double,
    val totalPnl: Double,
    val todaysPnl: Double
) {
    val totalPnlPercentage: Double
        get() = if (totalInvestment != 0.0)
            (totalPnl / totalInvestment) * 100
        else 0.0
}
