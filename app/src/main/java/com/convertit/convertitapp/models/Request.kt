package com.convertit.convertitapp.models

data class Request(
    val firstCurrency: String,
    val secondCurrency: String,
    val value: Double
)
