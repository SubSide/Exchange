package dev.subside.exchange

enum class Env {
    DYNAMO_ENDPOINT,
    NOTIFICATION_SERVICE,
    ;

    fun get(): String? = System.getenv(this.name)
}