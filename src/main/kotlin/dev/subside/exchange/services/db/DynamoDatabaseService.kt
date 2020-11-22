package dev.subside.exchange.services.db

import dev.subside.exchange.Env
import dev.subside.exchange.models.Rates
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*
import java.net.URI

/**
 * Our dynamo Database service
 */
class DynamoDatabaseService : DatabaseService {

    /** Our dynamo database instance */
    private val dynamoDb: DynamoDbClient

    init {
        // Here we build our dynamoDb, we use env variables so we can run it locally or on the server
        val dynamoBuilder = DynamoDbClient.builder()
        // For now we set our Region to be static
        dynamoBuilder.region(Region.EU_CENTRAL_1)

        // If we provided a custom endpoint we use that
        if (Env.DYNAMO_ENDPOINT.get() != null) {
            println("Found custom dynamo endpoint in our env, using this instead")
            dynamoBuilder.endpointOverride(URI.create(Env.DYNAMO_ENDPOINT.get()!!))
        }

        // Lastly we build our database
        dynamoDb = dynamoBuilder.build()


        try {
            // Try to create Amazon Dynamo Database link
            createDynamoDb()
        } catch (e: ResourceInUseException) {
            println("Database already exists.")
            println("ASDASDASD")
        }
    }


    override fun putRates(item: Rates) {
        dynamoDb.putItem {
            it.tableName(TABLE_NAME)
            it.item(mapItem(item))
        }
    }

    override suspend fun getItems(start: Long, end: Long): List<Rates> {
        val attrValues = mapOf(
            ":start" to AttributeValue.builder().n(start.toString()).build(),
            ":end" to AttributeValue.builder().n(end.toString()).build(),
        )

        return dynamoDb.scan {
            it.tableName(TABLE_NAME)
            it.filterExpression("$FETCH_KEY BETWEEN :start AND :end")
            it.expressionAttributeValues(attrValues)
        }
            .items()
            .map { getItem(it) }
    }


    /** This function sets up the database if it does not yet exist */
    private fun createDynamoDb() {
        dynamoDb.createTable {
            // Create a table
            it.tableName(TABLE_NAME)

            it.attributeDefinitions(
                AttributeDefinition.builder()
                    .attributeName(HASH_KEY)
                    .attributeType(ScalarAttributeType.S)
                    .build(),
                AttributeDefinition.builder()
                    .attributeName(FETCH_KEY)
                    .attributeType(ScalarAttributeType.N)
                    .build(),
            )

            it.keySchema(
                KeySchemaElement.builder()
                    .attributeName(HASH_KEY)
                    .keyType(KeyType.HASH)
                    .build(),
                KeySchemaElement.builder()
                    .attributeName(FETCH_KEY)
                    .keyType(KeyType.RANGE)
                    .build(),
            )

            it.provisionedThroughput(
                ProvisionedThroughput.builder()
                    .readCapacityUnits(10L)
                    .writeCapacityUnits(10L)
                    .build()
            )
        }
    }

    /** This function maps a Rates object to a map with string and attributeValues */
    private fun mapItem(item: Rates): Map<String, AttributeValue> {
        val items = HashMap<String, AttributeValue>()

        items[FETCH_KEY] = AttributeValue.builder().n(System.currentTimeMillis().toString()).build()
        items["base"] = AttributeValue.builder().s(item.base).build()
        items["date"] = AttributeValue.builder().s(item.date).build()
        items["rates"] = AttributeValue.builder().m(
            item.rates.map {
                it.key to AttributeValue.builder().n(it.value.toString()).build()
            }.toMap()
        ).build()

        return items
    }

    /**
     * Creates a Rates object from a map of string => Attributevalue
     *
     * This is just a convenience function from what we receive from the DynamoDB
     */
    private fun getItem(attrs: Map<String, AttributeValue>): Rates {
        // Make sure none of them are null
        val date = checkNotNull(attrs["date"]?.s()) { "date is null" }
        val base = checkNotNull(attrs["base"]?.s()) { "base is null" }
        val rawRates = checkNotNull(attrs["rates"]?.m()) { "rates is null" }

        // Go over all rawRates, check if their values are correct (or throw an error) and map it to a key-=>value pair
        val rates = rawRates.map {
            checkNotNull(it.key) { "Rate key is null ($it)" }
            checkNotNull(it.value) { "Rate value is null ($it)" }
            val value = it.value.n().toDouble()

            it.key to value
        }.toMap()

        // Create the Rates object and return
        return Rates(date, base, rates)
    }

    companion object {
        private const val TABLE_NAME = "exchange"
        private const val FETCH_KEY = "timeFetched"
        private const val HASH_KEY = "date"
    }
}