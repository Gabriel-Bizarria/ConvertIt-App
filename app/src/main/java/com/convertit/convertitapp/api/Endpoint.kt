package com.convertit.convertitapp.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Endpoint{
    @GET("json/last/{from}-{to}")
    fun getCurrency(
        @Path("from", encoded = true)from: String,
        @Path("to", encoded = true)to: String)
    : Call<JsonObject>


}