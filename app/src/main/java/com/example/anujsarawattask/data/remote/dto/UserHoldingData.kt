package com.example.anujsarawattask.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserHoldingData(
    @SerializedName("userHolding")
    val holdings: List<HoldingDto>?
)