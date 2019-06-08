package com.example.am_projekt.sensorActivity

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.TextView
import com.example.am_projekt.R

class ProximityListener(sensorManager: SensorManager, private val context: Context) : Thread(), SensorEventListener {

    init {
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onSensorChanged(event: SensorEvent) {
        val textViewProximity = (context as SensorActivity).findViewById<TextView>(R.id.tvProximityMeasurement)
        context.runOnUiThread { textViewProximity.text = java.lang.Float.toString(event.values[0]) }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}
