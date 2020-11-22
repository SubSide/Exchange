import dev.subside.exchange.services.api.ExchangeApiService
import dev.subside.exchange.models.Rates
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.test.assertEquals

class ApiTest {
    lateinit var api: ExchangeApiService
    lateinit var rates: Rates

    @Before
    fun `create api object`() {
        // Create the ExchangeService object with Retrofit
        api = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.exchangeratesapi.io/")
            .build()
            .create(ExchangeApiService::class.java)


        // Get the rates a single time, so we're not making unnecessary calls
        rates = runBlocking { api.getLatestRates() }
    }



    @Test
    fun `check if Euro is used as base`() {
        assertEquals(rates.base, "EUR")
    }

    // Apparently the API doesn't change during weekends.
    @Test
    fun `check if the date is following the yyyy-MM-dd format and is within 7 days of today`() {
        // We create a date in the same format YYYY-MM-DD
        val sdf = SimpleDateFormat("yyyy-MM-dd")

        // Throws ParseException if it is an incorrect format
        val parsedDate = sdf.parse(rates.date)


        // Current date minus 2 days to compare to
        val comparingDate = Date.from(Instant.now().minus(7, ChronoUnit.DAYS))

        // Check if the parsedDate is after the comparingDate
        assert(parsedDate.after(comparingDate)) { "parsedDate: $parsedDate, comparingDate: $comparingDate" }
    }

    @Test
    fun `make sure the rate object contains rates`() {
        assert(rates.rates.isNotEmpty())
    }

    @Test
    fun `check if all rates keys are strings`() {
        rates.rates.keys.forEach {
            @Suppress("USELESS_IS_CHECK")
            assert(it is String)
        }
    }

    @Test
    fun `check if all rates values are doubles`() {
        rates.rates.values.forEach {
            @Suppress("USELESS_IS_CHECK")
            assert(it is Double)
        }
    }
}