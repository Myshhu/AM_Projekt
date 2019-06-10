package com.example.am_projekt.calculatorActivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.am_projekt.R
import com.example.am_projekt.database.DatabaseHelper
import com.example.am_projekt.variables.CurrentLoggedUserData
import kotlinx.android.synthetic.main.activity_calculator.*

class CalculatorActivity : AppCompatActivity() {

    private var firstNumber = 0f
    private var secondNumber = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)
    }

    fun btnAddClick(v: View) {
        getNumbersFromInputs()
        val result = firstNumber + secondNumber
        setResult(result)
        saveResultsToDatabase(
            CurrentLoggedUserData.getCurrentLoggedUsername(),
            firstNumber, secondNumber, "ADD", result)
    }

    fun btnSubtractClick(v: View) {
        getNumbersFromInputs()
        val result = firstNumber - secondNumber
        setResult(result)
        saveResultsToDatabase(
            CurrentLoggedUserData.getCurrentLoggedUsername(),
            firstNumber, secondNumber, "SUB", result)
    }

    fun btnMultiplyClick(v: View) {
        getNumbersFromInputs()
        val result = firstNumber * secondNumber
        setResult(result)
        saveResultsToDatabase(
            CurrentLoggedUserData.getCurrentLoggedUsername(),
            firstNumber, secondNumber, "MUL", result)
    }

    fun btnDivideClick(v: View) {
        getNumbersFromInputs()
        val result = firstNumber / secondNumber
        setResult(result)
        saveResultsToDatabase(
            CurrentLoggedUserData.getCurrentLoggedUsername(),
            firstNumber, secondNumber, "DIV", result)
    }

    private fun getNumbersFromInputs() {
        try {
            firstNumber = etFirstNumber.text.toString().toFloat()
            secondNumber = etSecondNumber.text.toString().toFloat()
        } catch (e: Exception) {
            Toast.makeText(this, "Please give correct numbers", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setResult(result: Float) {
        tvResult.text = result.toString()
    }

    private fun saveResultsToDatabase(username: String, firstNumber: Float, secondNumber: Float, operation: String, result: Float) {
        DatabaseHelper(this).addCalculatorItem(username, firstNumber, secondNumber, operation, result)
    }
}
