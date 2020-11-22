package dev.subside.exchange.services.notification

/** Our NotificationService interface. If you want to have a different way of notifying, just implement this */
interface NotificationService {
    /**
     * Notify the given notification service with the given rates.
     */
    suspend fun notify(rates: Map<String, Double>)
}