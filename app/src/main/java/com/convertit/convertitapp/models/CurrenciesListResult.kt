package com.convertit.convertitapp.models

data class CurrenciesListResult(
    val acronym: String,
    val currencyName: String,
    var currencyValue: Double
)