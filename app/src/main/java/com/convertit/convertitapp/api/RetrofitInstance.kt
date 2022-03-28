package com.convertit.convertitapp.api

import com.convertit.convertitapp.constants.BASE_URL
import com.convertit.convertitapp.util.NetworkUtils

object RetrofitInstance{
         val retrofitClient = NetworkUtils.getRetrofitInstance(BASE_URL)
         val endpoint: ApiService = retrofitClient.create(ApiService::class.java)
}
