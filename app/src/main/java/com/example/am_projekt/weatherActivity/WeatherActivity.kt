package com.example.am_projekt.weatherActivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.am_projekt.R

class WeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
    }

    fun buttonFindWeatherInfoClick(v: View) {
        WeatherDataDownloader(this).execute()
    }
}
