package com.example.am_projekt

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.am_projekt.database.DatabaseHelper
import kotlinx.android.synthetic.main.content_show_calculator_db.*

class ShowWeatherDBActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_calculator_db)

        showItems()
    }

    private fun showItems() {
        try {
            val cursor = DatabaseHelper(this).weatherItems
            tvDBContent.append("ID  Name    City    Temp  Pressure  Humidity\n")
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                tvDBContent.append(
                    cursor.getString(0) + " " + cursor.getString(1)
                            + " " + cursor.getString(2) + " " + cursor.getString(3) + " " + cursor.getString(4)
                            + " " + cursor.getString(5)
                )
                tvDBContent.append("\n")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
