package mocks

import dev.subside.exchange.services.db.DatabaseService
import dev.subside.exchange.models.Rates

class DatabaseServiceMock : DatabaseService {
    // We have a variable which we can use to check if it is working
    val items = listOf(
        Rates(
            "2020-11-22",
            "EUR",
            mapOf(
                "USD" to 3.0,
                "GBP" to 6.0,
                "AUD" to 2.0,
            ),
        ),
        Rates(
            "2020-11-22",
            "EUR",
            mapOf(
                "USD" to 2.0,
                "GBP" to 7.0,
                "AUD" to 14.0,
            ),
        )
    )

    override suspend fun getItems(start: Long, end: Long): List<Rates> {
        return items
    }

    // We have a variable where we can check if it is working
    var receivedRates: Rates? = null
    override fun putRates(item: Rates) {
        receivedRates = item
    }
}