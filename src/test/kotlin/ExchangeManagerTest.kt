import dev.subside.exchange.ExchangeManager
import dev.subside.exchange.NotifyManager
import dev.subside.exchange.models.Rates
import dev.subside.exchange.services.api.ExchangeApiService
import dev.subside.exchange.services.db.DatabaseService
import kotlinx.coroutines.runBlocking
import mocks.DatabaseServiceMock
import mocks.ExchangeApiServiceMock
import mocks.NotificationServiceMock
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ExchangeManagerTest {
    lateinit var exchangeManager: ExchangeManager
    lateinit var databaseService: DatabaseServiceMock
    lateinit var exchangeApiService: ExchangeApiServiceMock

    @Before
    fun `create exchange object`() {
        databaseService = DatabaseServiceMock()
        exchangeApiService = ExchangeApiServiceMock()

        exchangeManager = ExchangeManager(exchangeApiService, databaseService)
    }


    @Test
    fun `check if the exchangeManager passes the data correctly to our database service`() {
        // We first manually call the runAsync task of exchangeManager
        runBlocking { exchangeManager.runAsync() }

        // Did we receive the api item in the database?
        assertEquals(exchangeApiService.item, databaseService.receivedRates)
    }
}