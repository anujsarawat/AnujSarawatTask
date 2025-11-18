package com.example.anujsarawattask.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.anujsarawattask.domain.model.Holding

@Entity(tableName = "holdings")
data class HoldingEntity(
    @PrimaryKey
    val symbol: String,
    val quantity: Int,
    val ltp: Double,
    val avgPrice: Double,
    val close: Double
)

fun HoldingEntity.toDomain(): Holding {
    return Holding(
        symbol = symbol,
        quantity = quantity,
        ltp = ltp,
        avgPrice = avgPrice,
        close = close
    )
}

fun Holding.toEntity(): HoldingEntity {
    return HoldingEntity(
        symbol = symbol,
        quantity = quantity,
        ltp = ltp,
        avgPrice = avgPrice,
        close = close
    )
}