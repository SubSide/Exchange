package dev.subside.exchange.services.notification

/**
 * Just a simple NotificationService which will just output the rates to the console
 */
class ConsoleNotificationService : NotificationService {
    override suspend fun notify(rates: Map<String, Double>) {
        println("----------")
        rates.forEach {
            println("${it.key} = ${it.value}")
        }
        println("----------")
    }
}