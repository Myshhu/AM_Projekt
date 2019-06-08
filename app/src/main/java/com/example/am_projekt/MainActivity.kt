package com.example.am_projekt

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.am_projekt.sensorActivity.SensorActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun btnLaunchSensorActivityClick(v: View) {
        val intent = Intent(this, SensorActivity::class.java)
        startActivity(intent)
    }
}
