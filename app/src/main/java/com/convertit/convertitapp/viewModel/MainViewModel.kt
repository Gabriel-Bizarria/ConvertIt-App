package com.convertit.convertitapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.convertit.convertitapp.MainCurrenciesList
import com.convertit.convertitapp.api.RetrofitInstance.endpoint
import com.convertit.convertitapp.models.CurrenciesListBase
import com.convertit.convertitapp.models.Request
import com.convertit.convertitapp.ui.helpers.formatter
import kotlinx.coroutines.*


class MainViewModel() : ViewModel() {
    private val _conversionLiveData = MutableLiveData<Double>()
    val conversionLiveData: LiveData<Double> = _conversionLiveData

    private val _mainCurrencyLiveData = MutableLiveData<String>()
    val mainCurrencyLiveData: LiveData<String> = _mainCurrencyLiveData

    private val _finalListCurrencies = MutableLiveData<MutableList<CurrenciesListBase>>()
    val finalListCurrencies: LiveData<MutableList<CurrenciesListBase>> = _finalListCurrencies

    private val currenciesListFinal = mutableListOf<CurrenciesListBase>()

    //Conversion Fragments
    fun getConversion(request: Request): LiveData<Double> {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    endpoint.getCurrency(request.firstCurrency, request.secondCurrency).execute()
                if (response.isSuccessful) {
                    val data = response.body()?.entrySet()?.find {
                        it.key == "${request.firstCurrency}${request.secondCurrency}"
                    }!!.toPair()
                    val rateBid = data.second.asJsonObject.entrySet().find { it.key == "bid" }!!
                        .toPair().second
                    val rate = rateBid.toString().replace('"', ' ').toDouble()
                    _conversionLiveData.postValue(rate * request.value)
                }
            } catch (e: Exception) {
                Log.v(
                    "API_REQUEST_ERROR_1",
                    "Was not possible to contact the API: ${e.message}"
                )
            }
        }
        return conversionLiveData
    }


    //Currencies Fragment
    fun getCurrencySelected(itemDropdown: String) {
        _mainCurrencyLiveData.postValue(itemDropdown)
    }

    fun getCurrenciesList() {
        if(currenciesListFinal.size > 0){
            currenciesListFinal.clear()
        }

        val secondaryCurrenciesList = MainCurrenciesList.currenciesList
        val mainCurrency = mainCurrencyLiveData.value!!

        viewModelScope.launch(Dispatchers.IO) {
            for (currency in secondaryCurrenciesList) {
                if (currency.acronym != mainCurrency) {
                    val request = Request(mainCurrency, currency.acronym, 1.0)
                    val extraData =
                        CurrenciesListBase(currency.acronym, currency.currencyName, 1.0)

                    try {
                        val response =
                            endpoint.getCurrency(request.firstCurrency, request.secondCurrency)
                                .execute()
                        if (response.isSuccessful) {
                            val data = response.body()?.entrySet()?.find {
                                it.key == "${request.firstCurrency}${request.secondCurrency}"
                            }!!.toPair()
                            val rateBid =
                                data.second.asJsonObject.entrySet().find { it.key == "bid" }!!
                                    .toPair().second
                            val rate = rateBid.toString().replace('"', ' ').toDouble()
                            val currencyResponse = CurrenciesListBase(
                                extraData.acronym,
                                extraData.currencyName,
                                formatter.format(rate).toDouble()
                            )
                            currenciesListFinal.add(currencyResponse)
                            Log.v("DATA_REACHED_REQUEST", "$currencyResponse")
                        }
                    } catch (e: Exception) {
                        Log.v(
                            "API_REQUEST_ERROR_1",
                            "Was not possible to contact the API: ${e.message}"
                        )
                    }
                }
            }
            _finalListCurrencies.postValue(currenciesListFinal)
            Log.v("DATA_REACHED_LIVE_DATA", "$currenciesListFinal")
        }
    }
}

