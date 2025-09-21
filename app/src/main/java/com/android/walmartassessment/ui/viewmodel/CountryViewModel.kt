package com.android.walmartassessment.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.walmartassessment.data.model.CountryItem
import com.android.walmartassessment.data.repository.CountryRepository
import kotlinx.coroutines.launch

class CountryViewModel(
    private val repository: CountryRepository = CountryRepository()
) : ViewModel() {

    private val _countries = MutableLiveData<List<CountryItem>>()
    val countries: LiveData<List<CountryItem>> = _countries

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadCountries() {
        viewModelScope.launch {
            val result = repository.fetchCountries()
            if (result.isSuccess) {
                _countries.value = result.getOrNull()
            } else {
                _error.value = result.exceptionOrNull()?.localizedMessage
            }
        }
    }
}
