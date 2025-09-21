package com.android.walmartassessment.data.repository

import com.android.walmartassessment.data.model.CountryItem
import com.android.walmartassessment.data.remote.ApiClient

class CountryRepository {
    private val api = ApiClient.service

    suspend fun fetchCountries(): Result<List<CountryItem>> {
        return try {
            val response = api.getCountries()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
