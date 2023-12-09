package com.belkanoid.weatherapp2.presentation.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.belkanoid.weatherapp2.databinding.ActivityMainBinding
import com.belkanoid.weatherapp2.presentation.ViewModelFactory
import com.belkanoid.weatherapp2.presentation.WeatherApplication
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[WeatherViewModel::class.java]
    }
    private val component by lazy { (application as WeatherApplication).component }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        component.inject(this)
        binding.input.doOnTextChanged { text, _, _, _ ->
            text?.let { viewModel.getWeatherInfo(it) }
        }
        handleState()
    }


    private fun handleState() {
        viewModel.state
            .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
            .onEach { state ->
                binding.apply {
                    progressBar.visibility = View.INVISIBLE
                    weatherError.visibility = View.INVISIBLE

                    when (state) {
                        is State.Idle -> Unit

                        is State.Loading -> {
                            progressBar.visibility = View.VISIBLE
                            weatherTemperature.text = ""
                            weatherFeelsLike.text = ""
                        }

                        is State.Success -> {
                            val main = state.data.main
                            weatherTemperature.text = main.temp.roundToInt().toString()
                            weatherFeelsLike.text = main.feels_like.roundToInt().toString()
                        }

                        is State.Error -> {
                            weatherError.visibility = View.VISIBLE
                            weatherError.text = state.message
                        }
                    }
                }
            }
            .launchIn(lifecycleScope)
    }
}