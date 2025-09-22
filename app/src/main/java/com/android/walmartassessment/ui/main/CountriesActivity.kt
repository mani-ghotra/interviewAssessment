package com.android.walmartassessment.ui.main

import CountryAdapter
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.walmartassessment.data.model.CountryItem
import com.android.walmartassessment.databinding.ActivityCountriesBinding
import com.android.walmartassessment.ui.viewmodel.CountryViewModel

class CountriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCountriesBinding
    private lateinit var adapter: CountryAdapter
    private lateinit var viewModel: CountryViewModel
    private var countryList = listOf<CountryItem>()
    private var recyclerViewState: Parcelable? = null
    private var currentQuery: String = ""
    private var shouldRestoreState = false

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        recyclerViewState = binding.countryRecyclerView.layoutManager?.onSaveInstanceState()
        outState.putParcelable("recycler_state", recyclerViewState)
        outState.putString("search_query", currentQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        recyclerViewState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            savedInstanceState.getParcelable("recycler_state", Parcelable::class.java)
        } else {
            savedInstanceState.getParcelable("recycler_state")
        }
        currentQuery = savedInstanceState.getString("search_query", "")
        shouldRestoreState = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CountryAdapter()
        binding.countryRecyclerView.adapter = adapter
        binding.countryRecyclerView.setHasFixedSize(true)

        // Edge-to-edge insets
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, systemBars.top, 0, systemBars.bottom)
            insets
        }

        setupLayoutManager(resources.configuration.orientation)

        viewModel = CountryViewModel()

        // Observe countries data
        viewModel.countries.observe(this) { list ->
            countryList = list
            filterAndSubmitList(currentQuery)
        }

        viewModel.error.observe(this) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        // Set the current query in EditText if restoring state
        if (savedInstanceState != null) {
            binding.etSearch.setText(currentQuery)
        }

        // Live search
        binding.etSearch.addTextChangedListener(afterTextChanged = { editable ->
            val newQuery = editable?.toString()?.trim() ?: ""
            if (newQuery != currentQuery) {
                currentQuery = newQuery
                filterAndSubmitList(currentQuery)
            }
        })
    }

    private fun filterAndSubmitList(query: String) {
        val filteredList = if (query.isBlank()) {
            countryList
        } else {
            countryList.filter { item ->
                item.name?.contains(query, ignoreCase = true) == true ||
                        item.capital?.contains(query, ignoreCase = true) == true ||
                        item.region?.contains(query, ignoreCase = true) == true ||
                        item.code?.contains(query, ignoreCase = true) == true
            }
        }

        adapter.submitList(filteredList) {
            // Only restore state once after configuration change and when showing all items
            if (shouldRestoreState && query.isBlank()) {
                recyclerViewState?.let { state ->
                    binding.countryRecyclerView.layoutManager?.onRestoreInstanceState(state)
                }
                shouldRestoreState = false
            }
        }

        // Empty state
        binding.emptyListText.visibility = if (filteredList.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun setupLayoutManager(orientation: Int) {
        binding.countryRecyclerView.layoutManager =
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(this, 2)
            } else {
                LinearLayoutManager(this)
            }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Save current state before changing layout
        recyclerViewState = binding.countryRecyclerView.layoutManager?.onSaveInstanceState()

        // Setup new layout manager
        setupLayoutManager(newConfig.orientation)

        // Restore the adapter
        binding.countryRecyclerView.adapter = adapter

        // Restore state
        recyclerViewState?.let { state ->
            binding.countryRecyclerView.layoutManager?.onRestoreInstanceState(state)
        }
    }
}