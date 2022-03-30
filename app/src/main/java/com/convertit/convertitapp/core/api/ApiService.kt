package com.convertit.convertitapp.core.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService{
    @GET("json/last/{from}-{to}")
    fun getCurrency(
        @Path("from")from: String,
        @Path("to")to: String
    ): Call<JsonObject>

}