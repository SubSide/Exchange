package dev.subside.exchange.services.api

import dev.subside.exchange.models.Rates
import retrofit2.http.GET

/**
 * Our API service, this will automatically be built for us by Retrofit
 */
interface ExchangeApiService {
    @GET("latest")
    suspend fun getLatestRates(): Rates
}