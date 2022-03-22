package com.convertit.convertitapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.convertit.convertitapp.api.ApiService
import com.convertit.convertitapp.model.Request
import com.convertit.convertitapp.util.NetworkUtils
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConversionViewModel(): ViewModel() {

    fun getCurrency(request: Request): LiveData<Double?> {
        val retrofitClient = NetworkUtils.getRetrofitInstance("https://economia.awesomeapi.com.br/")
        val endpoint = retrofitClient.create(ApiService::class.java)
        val conversionLiveData = MutableLiveData<Double?>()

        viewModelScope.launch(Dispatchers.IO) {
            //Using callback to get the response in async function,
            //but I could use Courotine with a IO Dispatcher to get the same result.
            endpoint.getCurrency(request.firstCurrency, request.secondCurrency).enqueue(object: Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    var data = response
                        .body()?.entrySet()?.find {
                            it.key == "${request.firstCurrency}${request.secondCurrency}"
                        }!!.toPair()

                    var rateBid = data.second.asJsonObject.entrySet().find {it.key == "bid"}!!.toPair().second
                    val rate = rateBid.toString().replace('"', ' ').toDouble()
                    conversionLiveData.postValue( rate * request.value)

                    println("THIS IS THE DATA THAT WE RECEIVED: $conversionLiveData")
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    //Toast.makeText(getApplication(), "Request to API was not possible!", Toast.LENGTH_SHORT).show()
                    Log.v("ERROR", "Was not possible to contact the API")
                    conversionLiveData.postValue(null)
                }
            })
        }
        return conversionLiveData
    }
}