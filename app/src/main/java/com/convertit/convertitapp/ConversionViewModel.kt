package com.convertit.convertitapp

import android.util.Log
import androidx.lifecycle.ViewModel
import com.convertit.convertitapp.api.Endpoint
import com.convertit.convertitapp.util.NetworkUtils
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.convertit.convertitapp.formatter

class ConversionViewModel: ViewModel() {

    fun getCurrency(first: String, second: String, value: Double): Double{
        val retrofitClient = NetworkUtils.getRetrofitInstance("https://economia.awesomeapi.com.br/")
        val endpoint = retrofitClient.create(Endpoint::class.java)
        var conversion: Double = 0.0

        endpoint.getCurrency(first, second).enqueue(object: Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                //var data = response.body()?.entrySet()?.find { it.key == second }
                var data = response.body()?.entrySet()?.find { it.key == "bid" }
                println("THIS IS THE DATA THAT WE RECEIVED: $data")
                val rate: Double = data?.value.toString().toDouble()

                conversion = value * rate
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                //Toast.makeText(getApplication(), "Request to API was not possible!", Toast.LENGTH_SHORT).show()
                Log.v("ERROR", "Was not possible to contact the API")
            }
        })
        return conversion
    }
}