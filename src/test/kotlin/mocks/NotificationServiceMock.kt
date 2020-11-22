package mocks

import dev.subside.exchange.services.notification.NotificationService

class NotificationServiceMock : NotificationService {

    // We have a variable which we can use to check if it is working
    var notifyRates: Map<String, Double>? = null
    override suspend fun notify(rates: Map<String, Double>) {
        notifyRates = rates
    }
}