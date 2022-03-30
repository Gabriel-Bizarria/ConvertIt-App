package com.convertit.convertitapp

import com.convertit.convertitapp.models.CurrenciesListBase

class MainCurrenciesList {

    companion object{

        val currenciesList = listOf(
            CurrenciesListBase("USD", "American Dollar", "$0.0"),
            CurrenciesListBase("BRL", "Brazilian Real", "$0.0"),
            CurrenciesListBase("EUR", "Euro", "$0.0"),
            CurrenciesListBase("CAD", "Canadian Dollar", "$0.0"),
            CurrenciesListBase("JPY", "Japanese Yen", "$0.0"),
            CurrenciesListBase("ARS", "Argentinian Peso", "$0.0"),
            CurrenciesListBase("CNY", "Chinese Yuan", "$0.0"),
            CurrenciesListBase("MXN", "Mexican Peso", "0.0")
        )

    }

}