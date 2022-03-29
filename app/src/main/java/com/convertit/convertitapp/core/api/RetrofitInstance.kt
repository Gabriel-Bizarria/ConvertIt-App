package com.convertit.convertitapp.core.api

import com.convertit.convertitapp.core.constants.BASE_URL
import com.convertit.convertitapp.core.util.NetworkUtils

object RetrofitInstance{
         private val retrofitClient = NetworkUtils.getRetrofitInstance(BASE_URL)
         val endpoint: ApiService = retrofitClient.create(ApiService::class.java)
}
