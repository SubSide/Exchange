package dev.subside.exchange.services.notification

import twitter4j.Twitter
import twitter4j.TwitterFactory
import java.text.SimpleDateFormat
import java.util.*

/** A simple notification service that will send a tweet with the Euro to USD rate */
class TwitterNotificationService : NotificationService {
    private val twitter: Twitter = TwitterFactory.getSingleton()

    override suspend fun notify(rates: Map<String, Double>) {
        // Create the date string
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm")
        val date = sdf.format(Date())

        // Create the message
        val message = """
            Current rate ($date):
            EUR €1.00 = USD $${rates["USD"]}
        """.trimIndent()

        // Send the tweet
        twitter.updateStatus(message)

        println("Sent a tweet!")
    }
}