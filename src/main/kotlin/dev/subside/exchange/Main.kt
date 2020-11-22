package dev.subside.exchange

import dev.subside.exchange.services.api.ExchangeApiService
import dev.subside.exchange.services.db.DynamoDatabaseService
import dev.subside.exchange.services.notification.ConsoleNotificationService
import dev.subside.exchange.services.notification.NotificationService
import dev.subside.exchange.services.notification.TwitterNotificationService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

// We want to pull data from the API and post it to our database every 10 minutes
const val SCHEDULE_PULL_PERIOD = 10 * 60 * 1_000L

// We want to send a notification every hour
const val SCHEDULE_NOTIFY_PERIOD = 60 * 60 * 1_000L
// We also add a small delay of 15 seconds, this is to stop race conditions with the API
const val SCHEDULE_NOTIFY_DELAY = 15_000L

@Suppress("UNUSED_PARAMETER")
fun main(args: Array<String>) {
    // Create our services
    val exchangeApi = getExchangeApiService()
    val exchangeDatabase = DynamoDatabaseService()
    // We switch between the different notification services based on what we received in env. Default is console.
    val notificationManager: NotificationService = when (Env.NOTIFICATION_SERVICE.get()?.toUpperCase()) {
        "TWITTER" -> TwitterNotificationService()
        else -> ConsoleNotificationService()
    }


    // Create the Exchange Manager
    val exchangeManager = ExchangeManager(exchangeApi, exchangeDatabase)

    // Create the NotifyManager
    val notifyManager = NotifyManager(exchangeDatabase, notificationManager)


    // Create a new timer
    val timer = Timer()
    // Schedule the exchange manager
    timer.schedule(exchangeManager, calcNextOpportunity(SCHEDULE_PULL_PERIOD), SCHEDULE_PULL_PERIOD)
    // Schedule the notification manager
    // We do add 15 seconds so we can be certainly sure that our exchange manager has pushed the results to the database
    timer.schedule(notifyManager, calcNextOpportunity(SCHEDULE_NOTIFY_PERIOD) + SCHEDULE_NOTIFY_DELAY, SCHEDULE_NOTIFY_PERIOD)
}

/**
 * This function calculates when the next best possible start would be
 * So if you want to run something every hour, and it is 14:36, it will return 24 minutes in milliseconds.
 * This is just a convenience function to calculate the the delay for scheduling
 */
private fun calcNextOpportunity(period: Long): Long {
    val now = System.currentTimeMillis()
    return period - (now % period)
}

/**
 * Generates our exchange API service using Retrofit
 */
private fun getExchangeApiService(): ExchangeApiService {
    // Build retrofit
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.exchangeratesapi.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create and return the exchange service
    return retrofit.create(ExchangeApiService::class.java)

}

