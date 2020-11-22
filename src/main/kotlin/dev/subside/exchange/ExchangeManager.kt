package dev.subside.exchange

import dev.subside.exchange.services.api.ExchangeApiService
import dev.subside.exchange.services.db.DatabaseService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

/** A simple timer task that grabs data from our API, and puts it in our database */
class ExchangeManager(
    private val exchangeApiService: ExchangeApiService,
    private val databaseService: DatabaseService,
) : TimerTask() {

    override fun run() {
        GlobalScope.launch { runAsync() }
    }

    suspend fun runAsync() {
        // Grab our data from our API
        val rates = exchangeApiService.getLatestRates()

        // And put it in our database
        databaseService.putRates(rates)

        println("Added new exchange rates to the database")
    }
}