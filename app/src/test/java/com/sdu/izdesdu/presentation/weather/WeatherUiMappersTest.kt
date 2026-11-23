package com.sdu.izdesdu.presentation.weather

import com.sdu.izdesdu.R
import com.sdu.izdesdu.domain.model.WeatherCondition
import com.sdu.izdesdu.domain.model.WeatherErrorType
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherUiMappersTest {

    @Test
    fun labelRes_mapsEveryCondition() {
        assertEquals(R.string.weather_cond_clear, WeatherCondition.CLEAR.labelRes())
        assertEquals(R.string.weather_cond_partly_cloudy, WeatherCondition.PARTLY_CLOUDY.labelRes())
        assertEquals(R.string.weather_cond_cloudy, WeatherCondition.CLOUDY.labelRes())
        assertEquals(R.string.weather_cond_fog, WeatherCondition.FOG.labelRes())
        assertEquals(R.string.weather_cond_drizzle, WeatherCondition.DRIZZLE.labelRes())
        assertEquals(R.string.weather_cond_rain, WeatherCondition.RAIN.labelRes())
        assertEquals(R.string.weather_cond_snow, WeatherCondition.SNOW.labelRes())
        assertEquals(R.string.weather_cond_showers, WeatherCondition.SHOWERS.labelRes())
        assertEquals(R.string.weather_cond_thunderstorm, WeatherCondition.THUNDERSTORM.labelRes())
    }

    @Test
    fun campusAdviceRes_respectsHazardAndTemperaturePriority() {
        assertEquals(
            R.string.weather_advice_thunderstorm,
            WeatherCondition.THUNDERSTORM.campusAdviceRes(temperatureC = 2)
        )
        assertEquals(
            R.string.weather_advice_rain,
            WeatherCondition.RAIN.campusAdviceRes(temperatureC = 3)
        )
        assertEquals(
            R.string.weather_advice_snow,
            WeatherCondition.SNOW.campusAdviceRes(temperatureC = -5)
        )
        assertEquals(
            R.string.weather_advice_fog,
            WeatherCondition.FOG.campusAdviceRes(temperatureC = 4)
        )
        assertEquals(
            R.string.weather_advice_cold,
            WeatherCondition.CLEAR.campusAdviceRes(temperatureC = 5)
        )
        assertEquals(
            R.string.weather_advice_clear,
            WeatherCondition.CLEAR.campusAdviceRes(temperatureC = 18)
        )
        assertEquals(
            R.string.weather_advice_cloudy,
            WeatherCondition.PARTLY_CLOUDY.campusAdviceRes(temperatureC = 16)
        )
    }

    @Test
    fun messageRes_mapsEveryErrorType() {
        assertEquals(R.string.weather_error_network, WeatherErrorType.NETWORK.messageRes())
        assertEquals(R.string.weather_error_timeout, WeatherErrorType.TIMEOUT.messageRes())
        assertEquals(R.string.weather_error_server, WeatherErrorType.SERVER.messageRes())
        assertEquals(R.string.weather_error_default, WeatherErrorType.UNKNOWN.messageRes())
    }
}
