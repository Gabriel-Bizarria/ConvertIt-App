package com.convertit.convertitapp.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.convertit.convertitapp.core.repository.MainCurrenciesList
import com.convertit.convertitapp.core.repository.Repository
import com.convertit.convertitapp.models.CurrenciesListBase
import com.convertit.convertitapp.models.Request
import com.convertit.convertitapp.models.ResponseCurrencyItem
import com.convertit.convertitapp.ui.helpers.formatter
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


class MainViewModel : ViewModel() {
    private val repository = Repository()

    private val _conversionLiveData = MutableLiveData<Double>()
    val conversionLiveData: LiveData<Double> = _conversionLiveData

    private val _mainCurrencyLiveData = MutableLiveData<String>()
    val mainCurrencyLiveData: LiveData<String> = _mainCurrencyLiveData

    private val _finalListCurrencies = MutableLiveData<MutableList<CurrenciesListBase>>()
    val finalListCurrencies: LiveData<MutableList<CurrenciesListBase>> = _finalListCurrencies

    private val currenciesListFinal = mutableListOf<CurrenciesListBase>()

    //Conversion Fragments
    fun getConversion(request: Request) {
        viewModelScope.launch(Dispatchers.IO) {
           repository.getCurrency(request.firstCurrency, request.secondCurrency).enqueue(object :
                Callback<JsonObject>{
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if(!response.isSuccessful){
                        Log.e("ERROR", "The contact to API was not successful. ${response.code()}")
                    }else{
                        val mapType: Type = object : TypeToken<Map<
                                String,
                                ResponseCurrencyItem
                                >>(){}.type
                        val currencyResponse: Map<String, ResponseCurrencyItem> = Gson()
                            .fromJson(response.body(), mapType)
                        val rate = currencyResponse["${request.firstCurrency}${request.secondCurrency}"]?.bid
                        val result = request.value * (rate?.toDouble() ?: 0.0)

                        if (rate != null) {
                            _conversionLiveData.postValue(result)
                            Log.v("DATA_REACHED_CONVERSION", "$currenciesListFinal")
                        }
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("ERROR", "Was not possible to contact the API.")
                }
            })
        }

    }


    //Currencies Fragment
    fun getCurrencySelected(itemDropdown: String) {
        _mainCurrencyLiveData.postValue(itemDropdown)
    }

    fun getCurrenciesList() {
        if(currenciesListFinal.size > 0){
            currenciesListFinal.clear()
        }

        viewModelScope.launch(Dispatchers.IO) {
            val secondaryCurrenciesList = MainCurrenciesList.currenciesList
            val mainCurrency = mainCurrencyLiveData.value!!

            for (currency in secondaryCurrenciesList) {
                if (currency.acronym != mainCurrency) {
                    repository.getCurrency(mainCurrency, currency.acronym).enqueue(object :
                        Callback<JsonObject>{
                        override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                        ) {
                            if(!response.isSuccessful){
                                Log.e("ERROR", "The contact to API was not successful. ${response.code()}")
                            }else{
                                val mapType: Type = object : TypeToken<Map<
                                        String,
                                        ResponseCurrencyItem
                                        >>(){}.type
                                val currencyResponse: Map<String, ResponseCurrencyItem> = Gson()
                                    .fromJson(response.body(), mapType)
                                val rate = currencyResponse["$mainCurrency${currency.acronym}"]?.bid

                                if (rate != null) {
                                    val formattedRate = formatter.format(rate.toDouble())
                                    currenciesListFinal.add(
                                        CurrenciesListBase(
                                            currency.acronym,
                                            currency.currencyName,
                                            "$$formattedRate"
                                        )
                                    )
                                }
                                if(currenciesListFinal.size == secondaryCurrenciesList.size - 1){
                                    _finalListCurrencies.postValue(currenciesListFinal)
                                }
                            }
                        }
                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            Log.e("ERROR", "Was not possible to contact the API.")
                        }
                    })
                }
            }
        }
    }
}