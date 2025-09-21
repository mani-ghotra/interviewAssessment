package com.android.walmartassessment.data.remote

import com.android.walmartassessment.data.model.CountryItem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CountryApi {
    @GET("db25946fd77c5873b0303b858e861ce724e0dcd0/countries.json")
    suspend fun getCountries(): List<CountryItem>
}

object ApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://gist.githubusercontent.com/peymano-wmt/32dcb892b06648910ddd40406e37fdab/raw/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: CountryApi = retrofit.create(CountryApi::class.java)
}
