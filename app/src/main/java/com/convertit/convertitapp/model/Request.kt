package com.convertit.convertitapp.model

data class Request(
    val firstCurrency: String,
    val secondCurrency: String,
    val value: Double
)
