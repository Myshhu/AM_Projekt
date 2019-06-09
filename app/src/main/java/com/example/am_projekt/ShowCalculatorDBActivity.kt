package com.example.am_projekt

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.am_projekt.database.DatabaseHelper
import kotlinx.android.synthetic.main.content_show_calculator_db_content.*

class ShowCalculatorDBActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_calculator_db_content)

        showItems()
    }

    private fun showItems() {
        appendNewLinesToTextViews()
        try {
            val cursor = DatabaseHelper(this).calculatorItems
            cursor.moveToFirst()
            do {
                tvShowCalcDBContentID.append(cursor.getString(0) + "\n")
                tvShowCalcDBContentName.append(cursor.getString(1) + "\n")
                tvShowDBContentFirstNumber.append(cursor.getString(2) + "\n")
                tvShowDBContentSecondNumber.append(cursor.getString(3) + "\n")
                tvShowDBContentOperation.append(cursor.getString(4) + "\n")
                tvShowDBContentResult.append(cursor.getString(5) + "\n")
            } while(cursor.moveToNext())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun appendNewLinesToTextViews() {
        tvShowCalcDBContentID.append("\n" + "\n")
        tvShowCalcDBContentName.append("\n" + "\n")
        tvShowDBContentFirstNumber.append("\n" + "\n")
        tvShowDBContentSecondNumber.append("\n" + "\n")
        tvShowDBContentOperation.append("\n" + "\n")
        tvShowDBContentResult.append("\n" + "\n")
    }
}
