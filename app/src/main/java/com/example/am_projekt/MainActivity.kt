package com.example.am_projekt

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.am_projekt.calculatorActivity.CalculatorActivity
import com.example.am_projekt.loginActivity.LoginActivity
import com.example.am_projekt.registerActivity.RegisterActivity
import com.example.am_projekt.sensorActivity.SensorActivity
import com.example.am_projekt.variables.CurrentLoggedUser
import com.example.am_projekt.weatherActivity.WeatherActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setCurrentUsernameTextView()
    }

    private fun setCurrentUsernameTextView() {
        val resultString = "Current user: " + CurrentLoggedUser.getCurrentLoggedUsername()
        tvCurrentLoggedUser.text = resultString
    }

    fun btnLaunchSensorActivityClick(v: View) {
        val intent = Intent(this, SensorActivity::class.java)
        startActivity(intent)
    }

    fun btnLaunchWeatherActivityClick(v: View) {
        val intent = Intent(this, WeatherActivity::class.java)
        startActivity(intent)
    }

    fun btnLaunchCalculatorActivityClick(v: View) {
        val intent = Intent(this, CalculatorActivity::class.java)
        startActivity(intent)
    }

    fun btnLaunchShowCalculatorDBActivityClick(v: View) {
        val intent = Intent(this, ShowCalculatorDBActivity::class.java)
        startActivity(intent)
    }

    fun btnLaunchShowWeatherDBActivityClick(v: View) {
        val intent = Intent(this, ShowWeatherDBActivity::class.java)
        startActivity(intent)
    }

    fun btnLaunchLoginActivityClick(v: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun btnLaunchRegisterActivityClick(v: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
