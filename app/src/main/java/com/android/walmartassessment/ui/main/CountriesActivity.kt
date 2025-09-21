package com.android.walmartassessment.ui.main

import CountryAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.walmartassessment.databinding.ActivityCountriesBinding
import com.android.walmartassessment.ui.viewmodel.CountryViewModel

class CountriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCountriesBinding
    private lateinit var adapter: CountryAdapter
    private lateinit var viewModel: CountryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCountriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        adapter = CountryAdapter()
        binding.countryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.countryRecyclerView.adapter = adapter

        // Setup ViewModel
        viewModel = CountryViewModel() // simple initialization

        // Observe data
        viewModel.countries.observe(this) { list ->
            adapter.submitList(list)
        }

        viewModel.error.observe(this) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
                // Show error however you like
            }
        }

        // Load data
        viewModel.loadCountries()
    }
}
