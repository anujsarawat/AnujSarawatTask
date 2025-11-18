package com.example.anujsarawattask.domain.usecase

import com.example.anujsarawattask.domain.model.Holding
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CalculatePortfolioSummaryUseCaseTest {

    private lateinit var useCase: CalculatePortfolioSummaryUseCase

    @Before
    fun setup() {
        useCase = CalculatePortfolioSummaryUseCase()
    }

    @Test
    fun `calculate summary with empty holdings returns zero values`() {
        val holdings = emptyList<Holding>()

        val result = useCase(holdings)

        assertEquals(0.0, result.currentValue, 0.01)
        assertEquals(0.0, result.totalInvestment, 0.01)
        assertEquals(0.0, result.totalPnl, 0.01)
        assertEquals(0.0, result.todaysPnl, 0.01)
    }

    @Test
    fun `calculate summary with single holding returns correct values`() {
        val holdings = listOf(
            Holding(
                symbol = "TCS",
                quantity = 10,
                ltp = 3250.0,
                avgPrice = 3000.0,
                close = 3200.0
            )
        )

        val result = useCase(holdings)

        assertEquals(32500.0, result.currentValue, 0.01)
        assertEquals(30000.0, result.totalInvestment, 0.01)
        assertEquals(2500.0, result.totalPnl, 0.01)
        assertEquals(-500.0, result.todaysPnl, 0.01)
    }

    @Test
    fun `calculate summary with multiple holdings returns correct aggregated values`() {
        val holdings = listOf(
            Holding(
                symbol = "TCS",
                quantity = 10,
                ltp = 3250.0,
                avgPrice = 3000.0,
                close = 3200.0
            ),
            Holding(
                symbol = "INFY",
                quantity = 20,
                ltp = 1500.0,
                avgPrice = 1400.0,
                close = 1480.0
            ),
            Holding(
                symbol = "WIPRO",
                quantity = 15,
                ltp = 450.0,
                avgPrice = 400.0,
                close = 445.0
            )
        )

        val result = useCase(holdings)

        assertEquals(69250.0, result.currentValue, 0.01)
        assertEquals(64000.0, result.totalInvestment, 0.01)
        assertEquals(5250.0, result.totalPnl, 0.01)
        assertEquals(-975.0, result.todaysPnl, 0.01)
    }

    @Test
    fun `calculate summary with loss scenario`() {
        val holdings = listOf(
            Holding(
                symbol = "ABC",
                quantity = 100,
                ltp = 50.0,
                avgPrice = 60.0,
                close = 52.0
            )
        )

        val result = useCase(holdings)

        assertEquals(5000.0, result.currentValue, 0.01)
        assertEquals(6000.0, result.totalInvestment, 0.01)
        assertEquals(-1000.0, result.totalPnl, 0.01)
        assertEquals(200.0, result.todaysPnl, 0.01)
    }
}