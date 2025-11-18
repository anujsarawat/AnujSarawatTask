package com.example.anujsarawattask.data.remote.dto

import com.google.gson.annotations.SerializedName

data class HoldingResponse(
    @SerializedName("data")
    val data: UserHoldingData?
)