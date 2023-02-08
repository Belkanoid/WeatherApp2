package com.belkanoid.weatherapp2.presentation.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.belkanoid.weatherapp2.databinding.ActivityMainBinding
import com.belkanoid.weatherapp2.domain.util.showSnackBar
import com.belkanoid.weatherapp2.presentation.ViewModelFactory
import com.belkanoid.weatherapp2.presentation.WeatherApplication
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw RuntimeException("ActivityMainBinding is null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[WeatherViewModel::class.java]
    }

    private val component by lazy { (application as WeatherApplication).component }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        component.inject(this)
        lifecycleScope.launchWhenStarted {
            handleState()
        }
        handleEvents()

    }


    private fun handleEvents() {
        binding.apply {
            textEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(MainEvent.SearchByCityEvent(text.toString()))
                textInputLayout.error = null
                tvTemperature.text = null
            }
        }
    }

    private suspend fun handleState() {
        viewModel.state.collect { state ->
            binding.progressBar.visibility = View.INVISIBLE
            when (state) {
                is WeatherState.Success -> {
                    binding.tvTemperature.text = state.data?.main?.temp?.toString() ?: let {
                        showSnackBar(binding.root, "Something went wrong, try again...")
                        return@collect
                    }
                }
                is WeatherState.Error.NetworkError -> {
                    binding.textInputLayout.error = "Check for Network is Available"
                    showSnackBar(binding.root, state.message)
                }
                is WeatherState.Error.TypeError -> {
                    binding.textInputLayout.error = "Check for a mistake in City\'s name"
                    showSnackBar(binding.root, state.message)
                }

                is WeatherState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                else -> Unit
            }
        }
    }

}