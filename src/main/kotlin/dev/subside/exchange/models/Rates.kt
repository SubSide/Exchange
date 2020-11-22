package dev.subside.exchange.models

/** Our simple Rates object */
data class Rates(
    val date: String,
    val base: String,
    val rates: Map<String, Double>
)