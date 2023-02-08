package com.belkanoid.weatherapp2.data.remote.dto

data class WeatherInfoDto(
    val coord: CoordDto,
    val dt: Int,
    val main: MainDto,
    val name: String,
    val sys: SysDto,
    val timezone: Int,
    val visibility: Int,
    val weather: List<WeatherDto>,
    val wind: WindDto
)