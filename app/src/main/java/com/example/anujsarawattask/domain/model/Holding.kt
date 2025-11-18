package com.example.anujsarawattask.domain.model

data class Holding(
    val symbol: String,
    val quantity: Int,
    val ltp: Double,
    val avgPrice: Double,
    val close: Double
) {
    val currentValue: Double
        get() = ltp * quantity

    val investmentValue: Double
        get() = avgPrice * quantity

    val pnl: Double
        get() = currentValue - investmentValue

    val todaysPnl: Double
        get() = (close - ltp) * quantity
}