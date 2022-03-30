package com.convertit.convertitapp.core.repository

import com.convertit.convertitapp.core.api.RetrofitInstance
import com.google.gson.JsonObject
import retrofit2.Call

class Repository {

    fun getCurrency(mainCurrency: String, secondCurrency: String): Call<JsonObject> {
        return RetrofitInstance.endpoint.getCurrency(mainCurrency, secondCurrency)
    }
}