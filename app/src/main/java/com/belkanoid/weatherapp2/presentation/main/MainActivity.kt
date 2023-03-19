package com.belkanoid.weatherapp2.presentation.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.belkanoid.weatherapp2.databinding.ActivityMainBinding
import com.belkanoid.weatherapp2.domain.state.State
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
                viewModel.getWeatherInfo(text.toString())
            }
        }
    }

    private suspend fun handleState() {
        viewModel.state.collect { state ->
            when(state) {
                is State.Success -> {
                    binding.tvTemperature.text = state.data.main.temp.toString()
                    binding.progressBar.visibility = View.INVISIBLE
                }
                is State.Error -> {
                    binding.textInputLayout.error = state.message
                    binding.progressBar.visibility = View.INVISIBLE
                }
                is State.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.textInputLayout.error = null
                    binding.tvTemperature.text = null
                }
                else -> {
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

}