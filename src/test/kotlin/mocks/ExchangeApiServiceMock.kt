package mocks

import dev.subside.exchange.services.api.ExchangeApiService
import dev.subside.exchange.models.Rates

class ExchangeApiServiceMock : ExchangeApiService {
    // We have a variable which we can use to check if it is working
    val item = Rates(
        "2020-11-22",
        "EUR",
        mapOf(
            "USD" to 3.0,
            "GBP" to 6.0,
            "AUD" to 2.0,
        ),
    )

    override suspend fun getLatestRates(): Rates {
        return item
    }
}