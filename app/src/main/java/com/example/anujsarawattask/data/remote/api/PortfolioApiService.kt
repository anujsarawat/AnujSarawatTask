package com.example.anujsarawattask.data.remote.api

import com.example.anujsarawattask.data.remote.dto.HoldingResponse
import retrofit2.Response
import retrofit2.http.GET

interface PortfolioApiService {
    @GET("/")
    suspend fun getHoldings(): Response<HoldingResponse>
}