package com.convertit.convertitapp.viewModel

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.convertit.convertitapp.R
import com.convertit.convertitapp.api.ApiService
import com.convertit.convertitapp.constants.BASE_URL
import com.convertit.convertitapp.model.Request
import com.convertit.convertitapp.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class MainViewModel(): ViewModel() {

    val retrofitClient = NetworkUtils.getRetrofitInstance(BASE_URL)
    val endpoint = retrofitClient.create(ApiService::class.java)

    fun getCurrency(request: Request): LiveData<Double?> {
        val conversionLiveData = MutableLiveData<Double?>()
        val conversionError = MutableLiveData<String?>()

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
                    conversionLiveData.postValue( rate * request.value)
                }else{
                    conversionError.postValue("Was not possible to contact the API, check your internet connection and try again.")
                }

            }catch (e: Exception){
                Log.v("API_REQUEST_ERROR","Was not possible to contact the API: ${e.message}")
            }
        }
        return conversionLiveData
    }

    /*fun getOneCurrency(currency: String): List<LiveData<Double?>> {



    }*/
}