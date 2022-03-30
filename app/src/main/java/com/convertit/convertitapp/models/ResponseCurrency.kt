package com.convertit.convertitapp.models

import com.google.gson.annotations.SerializedName

data class ResponseCurrencyItem(

	@field:SerializedName("varBid")
	val varBid: String? = null,

	@field:SerializedName("high")
	val high: String? = null,

	@field:SerializedName("pctChange")
	val pctChange: String? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("low")
	val low: String? = null,

	@field:SerializedName("codein")
	val codein: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("ask")
	val ask: String? = null,

	@field:SerializedName("bid")
	val bid: String? = null,

	@field:SerializedName("create_date")
	val createDate: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
)
