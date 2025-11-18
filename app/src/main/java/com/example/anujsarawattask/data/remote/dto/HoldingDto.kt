package com.example.anujsarawattask.data.remote.dto

import com.example.anujsarawattask.domain.model.Holding
import com.google.gson.annotations.SerializedName

data class HoldingDto(
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("quantity")
    val quantity: Int?,
    @SerializedName("ltp")
    val ltp: Double?,
    @SerializedName("avgPrice")
    val avgPrice: Double?,
    @SerializedName("close")
    val close: Double?
)

fun HoldingDto.toDomain(): Holding? {
    return if (symbol != null && quantity != null && ltp != null &&
        avgPrice != null && close != null) {
        Holding(
            symbol = symbol,
            quantity = quantity,
            ltp = ltp,
            avgPrice = avgPrice,
            close = close
        )
    } else {
        null
    }
}