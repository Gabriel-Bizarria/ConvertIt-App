package com.convertit.convertitapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.convertit.convertitapp.api.ApiService
import com.convertit.convertitapp.constants.BASE_URL
import com.convertit.convertitapp.models.Request
import com.convertit.convertitapp.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class MainViewModel(): ViewModel() {

    private val retrofitClient = NetworkUtils.getRetrofitInstance(BASE_URL)
    private val endpoint: ApiService = retrofitClient.create(ApiService::class.java)

    private val _conversionLiveData = MutableLiveData<Double>()
    val conversionLiveData: LiveData<Double> = _conversionLiveData

    private val _currencyLiveData = MutableLiveData<String>()
    val currencyLiveData: LiveData<String> = _currencyLiveData

    fun getConversion(request: Request): LiveData<Double> {
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = endpoint.getCurrency(request.firstCurrency, request.secondCurrency).execute()
                if(response.isSuccessful){
                    var data = response.body()?.entrySet()?.find {
                        it.key == "${request.firstCurrency}${request.secondCurrency}"
                    }!!.toPair()
                    println("THIS IS THE DATA THAT WE RECEIVED: ${data.toString()}")
                    var rateBid = data.second.asJsonObject.entrySet().find {it.key == "bid"}!!.toPair().second
                    val rate = rateBid.toString().replace('"', ' ').toDouble()
                    _conversionLiveData.postValue( rate * request.value)
                }
            }catch (e: Exception){
                Log.v("API_REQUEST_ERROR","Was not possible to contact the API: ${e.message}")
            }
        }
        return conversionLiveData
    }

    fun getCurrencisList(currency: String) {
        when{
            _currencyLiveData.value != currency -> notifyObserver(currency)
            else -> Unit
        }


    }

    private fun notifyObserver(currency: String) = _currencyLiveData.postValue(currency)
}