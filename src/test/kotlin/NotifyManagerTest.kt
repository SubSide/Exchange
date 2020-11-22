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
import kotlin.test.assertNotNull

class NotifyManagerTest {
    lateinit var notifyManager: NotifyManager
    lateinit var databaseService: DatabaseServiceMock
    lateinit var notificationService: NotificationServiceMock

    @Before
    fun `create notify object`() {
        databaseService = DatabaseServiceMock()
        notificationService = NotificationServiceMock()

        notifyManager = NotifyManager(databaseService, notificationService)
    }


    @Test
    fun `check if the notifyManager passes the data correctly to our notification service`() {
        // We first manually call the runAsync task of notifyManager
        runBlocking { notifyManager.runAsync() }

        // Did we receive the rates in the notificationService?
        assertNotNull(notificationService.notifyRates)
    }
}