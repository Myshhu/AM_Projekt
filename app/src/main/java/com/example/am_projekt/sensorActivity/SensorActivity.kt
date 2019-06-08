package com.example.am_projekt.sensorActivity

import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.example.am_projekt.R

class SensorActivity : AppCompatActivity() {

    private lateinit var lightPowerListener: LightListener
    private lateinit var proximityListener: ProximityListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)

        showLightMeasurement()
        showProximityMeasurement()
    }

    private fun showLightMeasurement() {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightPowerListener = LightListener(sensorManager, this)
    }

    private fun showProximityMeasurement() {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximityListener = ProximityListener(sensorManager, this)
    }
}
