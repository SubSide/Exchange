import dev.subside.exchange.NotifyManager
import dev.subside.exchange.models.Rates
import mocks.DatabaseServiceMock
import mocks.NotificationServiceMock
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class AverageTest {
    lateinit var notifyManager: NotifyManager

    @Before
    fun `create notification object`() {
        notifyManager = NotifyManager(DatabaseServiceMock(), NotificationServiceMock())
    }


    @Test
    fun `check if all averages are correct`() {
        // Create a dummy list of Rates
        val rateList = listOf(
            Rates(
                "2020-11-22",
                "EUR",
                mapOf(
                    "USD" to 2.0,
                    "GBP" to 6.0,
                    "AUD" to 2.3,
                ),
            ),
            Rates(
                "2020-11-22",
                "EUR",
                mapOf(
                    "USD" to 4.0,
                    "GBP" to 7.0,
                    "AUD" to 13.0,
                ),
            )
        )


        // create the average list from the function in notifyManager
        val averages = notifyManager.createAverage(rateList)

        // Make sure we have exactly 3 items
        assert(averages.size == 3)

        // Check if all currencies are there
        assert(averages.containsKey("USD"))
        assertEquals(averages["USD"], 3.0)

        assert(averages.containsKey("GBP"))
        assertEquals(averages["GBP"], 6.5)

        assert(averages.containsKey("AUD"))
        assertEquals(averages["AUD"], 7.65)
    }
}