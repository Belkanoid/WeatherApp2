package com.belkanoid.weatherapp2.presentation.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.belkanoid.weatherapp2.databinding.ActivityMainBinding
import com.belkanoid.weatherapp2.domain.state.State
import com.belkanoid.weatherapp2.domain.util.showSnackBar
import com.belkanoid.weatherapp2.presentation.ViewModelFactory
import com.belkanoid.weatherapp2.presentation.WeatherApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

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
            input.doOnTextChanged{ text, _, _, _ ->
                viewModel.getWeatherInfo(text.toString().trim())
            }
        }
    }

    private suspend fun handleState() {
        viewModel.state.collect { state ->
            binding.apply {
                when(state) {
                    is State.Success -> {
                        progressBar.visibility = View.INVISIBLE
                        weatherTemperature.text = state.data.main.temp.roundToInt().toString()
                        weatherFeelsLike.text = state.data.main.feels_like.roundToInt().toString()
                    }
                    is State.Error -> {
                        progressBar.visibility = View.INVISIBLE
                        weatherError.apply {
                            visibility = View.VISIBLE
                            text = state.message
                        }
                    }
                    is State.Loading -> {
                        weatherError.visibility = View.INVISIBLE
                        progressBar.visibility = View.VISIBLE
                        weatherBackToOnline.visibility = View.INVISIBLE
                    }
                    is State.InternetStatus -> {
                            weatherBackToOnline.visibility = View.VISIBLE
                            weatherBackToOnline.text = state.message
                            weatherBackToOnline.setBackgroundColor(ContextCompat.getColor(this@MainActivity, state.color))
                            delay(5000)
                            weatherBackToOnline.visibility = View.INVISIBLE
                    }
                    else -> {
                        weatherError.visibility = View.INVISIBLE
                        progressBar.visibility = View.INVISIBLE
                        weatherBackToOnline.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

}