package com.convertit.convertitapp

import com.convertit.convertitapp.models.CurrenciesListResult

class MainCurrenciesList {

    companion object{

        val currenciesList = listOf(
            CurrenciesListResult("USD", "American Dollar", 0.0),
            CurrenciesListResult("BRL", "Brazilian Real", 0.0),
            CurrenciesListResult("EUR", "Euro",  0.0),
            CurrenciesListResult("CAD", "Canadian Dollar", 0.0),
            CurrenciesListResult("JPY", "Japanese Yen", 0.0),
            CurrenciesListResult("ARS", "Argentinian Peso", 0.0),
            CurrenciesListResult("CNY", "Chinese Yuan", 0.0)
        )

    }

}