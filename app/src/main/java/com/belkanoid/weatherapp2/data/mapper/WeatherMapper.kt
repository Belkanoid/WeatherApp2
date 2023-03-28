package com.belkanoid.weatherapp2.data.mapper

import com.belkanoid.weatherapp2.data.remote.dto.*
import com.belkanoid.weatherapp2.domain.model.*

fun WeatherInfoDto.toWeatherInfo() = WeatherInfo(
    coord = coord.toCoord(),
    dt = dt,
    main = main.toMain(),
    name = name,
    sys = sys.toSys(),
    timezone = timezone,
    visibility = visibility,
    weather = weather.map { it.toWeather() },
    wind = wind.toWind()

)

fun CoordDto.toCoord() = Coord(
    lat = lat,
    lon = lon
)

fun MainDto.toMain() = Main(
    feels_like = feels_like,
    grnd_level = grnd_level,
    humidity = humidity,
    pressure = pressure,
    sea_level = sea_level,
    temp = temp,
    temp_max = temp_max,
    temp_min = temp_min
)

fun SysDto.toSys() = Sys(
    country = country,
    id = id,
    sunrise = sunrise,
    sunset = sunset,
    type = type
)

fun WeatherDto.toWeather() = Weather(
    description = description,
    icon = icon,
    id = id,
    main = main
)

fun WindDto.toWind() = Wind(
    deg = deg,
    gust = gust,
    speed = speed
)