package dev.subside.exchange

import dev.subside.exchange.services.db.DatabaseService
import dev.subside.exchange.models.Rates
import dev.subside.exchange.services.notification.NotificationService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * This manager will grab the latest hour of data from our database and will call our notificationService to pass it on
 */
class NotifyManager(
    private val exchangeDatabase: DatabaseService,
    private val notificationService: NotificationService,
) : TimerTask() {
    override fun run() {
        GlobalScope.launch { runAsync() }
    }

    suspend fun runAsync() {
        // Get the start and end date
        val startDate = Instant.now().minus(1, ChronoUnit.HOURS).toEpochMilli()
        val endDate = Instant.now().toEpochMilli()
        // Grab the data using the start and end date
        val data = exchangeDatabase.getItems(startDate, endDate)

        // Convert the list of rates to an average
        val averageRates = createAverage(data)

        // And notify
        notificationService.notify(averageRates)
    }

    /** Creates an average for every currency from a list of Rates */
    fun createAverage(data: List<Rates>): Map<String, Double> {
        // Create a new map that we will populate with all the rates
        val rateList = HashMap<String, MutableList<Double>>()

        data.forEach {  rate ->
            rate.rates.forEach {
                // If the key doesn't exist in rateList yet we add it with an empty list
                if (!rateList.containsKey(it.key)) {
                    rateList[it.key] = ArrayList()
                }

                // Add the value to the the list
                rateList[it.key]!!.add(it.value)
            }
        }

        // We create a new map where we just average the list we populated above and then return it
        return rateList.map {
            it.key to it.value.average()
        }.toMap()
    }
}