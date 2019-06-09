package com.example.am_projekt

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.am_projekt.database.DatabaseHelper
import kotlinx.android.synthetic.main.content_show_weather_db_content.*

class ShowWeatherDBActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_weather_db_content)

        showItems()
    }

    private fun showItems() {
        appendNewLinesToTextViews()
        try {
            val cursor = DatabaseHelper(this).weatherItems
            cursor.moveToFirst()
            do {
                tvShowDBContentID.append(cursor.getString(0) + "\n")
                tvShowDBContentName.append(cursor.getString(1) + "\n")
                tvShowDBContentCity.append(cursor.getString(2) + "\n")
                tvShowDBContentTemperature.append(cursor.getString(3) + "\n")
                tvShowDBContentPressure.append(cursor.getString(4) + "\n")
                tvShowDBContentHumidity.append(cursor.getString(5) + "\n")
            } while (cursor.moveToNext())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun appendNewLinesToTextViews() {
        tvShowDBContentID.append("\n" + "\n")
        tvShowDBContentName.append("\n" + "\n")
        tvShowDBContentCity.append("\n" + "\n")
        tvShowDBContentTemperature.append("\n" + "\n")
        tvShowDBContentPressure.append("\n" + "\n")
        tvShowDBContentHumidity.append("\n" + "\n")
    }

}
