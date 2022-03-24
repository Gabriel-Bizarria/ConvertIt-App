package com.convertit.convertitapp.models

data class CurrenciesListBase(
    val acronym: String,
    val currencyName: String,
    var currencyValue: Double
)