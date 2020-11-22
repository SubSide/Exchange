package dev.subside.exchange.services.db

import dev.subside.exchange.models.Rates

/**
 * A simple interface for our exchange database.
 */
interface DatabaseService {
    /**
     * Adds the given rates to the database
     *
     * @param item  the Rates object
     */
    fun putRates(item: Rates)

    /**
     * Returns a list of rates between the start and end date
     *
     * @param start     The start range
     * @param end       The end range
     * @return          A list of items which is between the start and end date
     */
    suspend fun getItems(start: Long, end: Long): List<Rates>
}