package com.example.anujsarawattask.presentation.holdings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anujsarawattask.domain.usecase.CalculatePortfolioSummaryUseCase
import com.example.anujsarawattask.domain.usecase.GetHoldingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HoldingsViewModel @Inject constructor(
    private val getHoldingsUseCase: GetHoldingsUseCase,
    private val calculatePortfolioSummaryUseCase: CalculatePortfolioSummaryUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow(HoldingsViewState())

    val viewState: StateFlow<HoldingsViewState> = _viewState.asStateFlow()

    init {
        loadHoldings()
        observeHoldings()
    }

    private fun observeHoldings() {
        viewModelScope.launch {
            getHoldingsUseCase()
                .catch { exception ->
                    _viewState.update {
                        it.copy(
                            error = exception.message ?: "Unknown error occurred",
                            isLoading = false
                        )
                    }
                }
                .collect { holdings ->
                    val summary = calculatePortfolioSummaryUseCase(holdings)
                    _viewState.update {
                        it.copy(
                            holdings = holdings,
                            portfolioSummary = summary,
                            isLoading = false,
                            isRefreshing = false,
                            error = null
                        )
                    }
                }
        }
    }

    private fun loadHoldings() {
        viewModelScope.launch {
            _viewState.update { it.copy(isLoading = true, error = null) }

            getHoldingsUseCase.refresh()
                .onFailure { exception ->
                    _viewState.update {
                        it.copy(
                            error = exception.message ?: "Failed to load holdings",
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun refreshHoldings() {
        viewModelScope.launch {
            _viewState.update { it.copy(isRefreshing = true, error = null) }

            getHoldingsUseCase.refresh()
                .onFailure { exception ->
                    _viewState.update {
                        it.copy(
                            error = exception.message ?: "Failed to refresh holdings",
                            isRefreshing = false
                        )
                    }
                }
        }
    }

    fun toggleSummaryExpansion() {
        _viewState.update {
            it.copy(
                isSummaryExpanded = !it.isSummaryExpanded
            )
        }
    }

    fun clearError() {
        _viewState.update { it.copy(error = null) }
    }
}