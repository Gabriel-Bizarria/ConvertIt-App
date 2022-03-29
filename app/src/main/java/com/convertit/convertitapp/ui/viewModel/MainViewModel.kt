package com.convertit.convertitapp.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.convertit.convertitapp.MainCurrenciesList
import com.convertit.convertitapp.core.api.RetrofitInstance.endpoint
import com.convertit.convertitapp.core.repository.Repository
import com.convertit.convertitapp.models.CurrenciesListBase
import com.convertit.convertitapp.models.Request
import com.convertit.convertitapp.models.ResponseCurrency
import com.convertit.convertitapp.models.ResponseCurrencyItem
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
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
            val response = repository.getCurrency(request.firstCurrency, request.secondCurrency).enqueue(object :
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

    suspend fun getCurrenciesList() {
        if(currenciesListFinal.size > 0){
            currenciesListFinal.clear()
        }

        viewModelScope.launch(Dispatchers.IO) {
            val secondaryCurrenciesList = MainCurrenciesList.currenciesList
            val mainCurrency = mainCurrencyLiveData.value!!

            for (currency in secondaryCurrenciesList) {
                if (currency.acronym != mainCurrency) {
                    val extraData =
                        CurrenciesListBase(currency.acronym, currency.currencyName, "1.0")

                }
            }
        }
        _finalListCurrencies.postValue(currenciesListFinal)
        Log.v("DATA_REACHED_LIVE_DATA", "$currenciesListFinal")
    }
}




















/*try {

             val response =
                    endpoint.getCurrency(request.firstCurrency, request.secondCurrency).execute()
                if (response.isSuccessful) {
                    val data = response.body()?.entrySet()?.find {
                        it.key == "${request.firstCurrency}${request.secondCurrency}"
                    }?.toPair()
                    val rateBid = data?.second?.asJsonObject?.entrySet()?.find { it.key == "bid" }
                        ?.toPair()?.second
                    val rate = rateBid.toString().replace('"', ' ').toDouble()
                    _conversionLiveData.postValue(rate * request.value)
                }
            } catch (e: Exception) {
                Log.v(
                    "API_REQUEST_ERROR_1",
                    "Was not possible to contact the API: ${e.message}"
                )
            }*/













//MÉTODO ANTES DO REPOSITORY, SE NECESSÁRIO MUDAR O RETORNO DO API SERVICE PARA CALL<JSONOBJECT>
/*val response = endpoint.getCurrency(mainCurrency, currency.acronym).enqueue(object :
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
                currenciesListFinal.add(
                    CurrenciesListBase(
                        currency.acronym,
                        currency.currencyName,
                        "$$rate"
                    )
                )
                Log.v("DATA_REACHED_FINAL_LIST", "$currenciesListFinal")
            }
        }
    }
    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
        Log.e("ERROR", "Was not possible to contact the API.")
    }
})*/
