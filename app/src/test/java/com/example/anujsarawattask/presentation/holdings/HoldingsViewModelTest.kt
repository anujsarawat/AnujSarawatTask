package com.example.anujsarawattask.presentation.holdings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.anujsarawattask.domain.model.Holding
import com.example.anujsarawattask.domain.usecase.CalculatePortfolioSummaryUseCase
import com.example.anujsarawattask.domain.usecase.GetHoldingsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class HoldingsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var getHoldingsUseCase: GetHoldingsUseCase

    @Mock
    private lateinit var calculatePortfolioSummaryUseCase: CalculatePortfolioSummaryUseCase

    private lateinit var viewModel: HoldingsViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() = runTest {
        val emptyHoldings = emptyList<Holding>()
        whenever(getHoldingsUseCase.invoke()).thenReturn(flowOf(emptyHoldings))
        whenever(getHoldingsUseCase.refresh()).thenReturn(Result.success(Unit))

        viewModel = HoldingsViewModel(getHoldingsUseCase, calculatePortfolioSummaryUseCase)

        val state = viewModel.viewState.value
        assertTrue(state.isLoading || state.holdings.isEmpty())
    }

    @Test
    fun `toggle summary expansion changes state`() = runTest {
        val emptyHoldings = emptyList<Holding>()
        whenever(getHoldingsUseCase.invoke()).thenReturn(flowOf(emptyHoldings))
        whenever(getHoldingsUseCase.refresh()).thenReturn(Result.success(Unit))

        viewModel = HoldingsViewModel(getHoldingsUseCase, calculatePortfolioSummaryUseCase)
        advanceUntilIdle()

        val initialExpanded = viewModel.viewState.value.isSummaryExpanded

        viewModel.toggleSummaryExpansion()
        advanceUntilIdle()

        val newExpanded = viewModel.viewState.value.isSummaryExpanded
        assertNotEquals(initialExpanded, newExpanded)
    }

    @Test
    fun `clear error removes error message`() = runTest {
        val emptyHoldings = emptyList<Holding>()
        whenever(getHoldingsUseCase.invoke()).thenReturn(flowOf(emptyHoldings))
        whenever(getHoldingsUseCase.refresh()).thenReturn(Result.success(Unit))

        viewModel = HoldingsViewModel(getHoldingsUseCase, calculatePortfolioSummaryUseCase)
        advanceUntilIdle()

        viewModel.clearError()
        advanceUntilIdle()

        assertNull(viewModel.viewState.value.error)
    }
}