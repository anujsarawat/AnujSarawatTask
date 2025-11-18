package com.example.anujsarawattask.presentation.holdings

import com.example.anujsarawattask.domain.model.Holding
import com.example.anujsarawattask.domain.model.PortfolioSummary

data class HoldingsViewState(
    val holdings: List<Holding> = emptyList(),
    val portfolioSummary: PortfolioSummary? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val isSummaryExpanded: Boolean = false
)