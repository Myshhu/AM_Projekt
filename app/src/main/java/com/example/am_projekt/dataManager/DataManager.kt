package com.example.am_projekt.dataManager

import android.app.Activity
import android.widget.Toast
import com.example.am_projekt.database.DatabaseHelper
import com.example.am_projekt.variables.CurrentLoggedUserData
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.NullPointerException
import java.lang.ref.WeakReference
import java.net.InetSocketAddress
import java.net.Socket

class DataManager(context: Activity?) {

    private var context: WeakReference<Activity?> = WeakReference(context)
    private var clientSocket: Socket? = null
    private val WEATHER_INFO = "weatherinfo"
    private val WEATHER_SUCCESS = "weatherdatasentsuccesfully"
    private val CALCULATOR_INFO = "calculatorinfo"
    private val CALCULATOR_SUCCESS = "calculatordatasentsuccesfully"

    fun manageWeatherItem(
        username: String, cityName: String,
        temperature: Float, pressure: Float, humidity: Float
    ) {
        addToWeatherDatabase(username, cityName, temperature, pressure, humidity)

        Thread {
            sendWeatherDataToServer(username, cityName, temperature, pressure, humidity)
        }.start()
    }

    private fun addToWeatherDatabase(
        username: String?,
        cityName: String,
        temperature: Float,
        pressure: Float,
        humidity: Float
    ) {
        DatabaseHelper(this.context.get()).addWeatherItem(username, cityName, temperature, pressure, humidity)
    }

    private fun sendWeatherDataToServer(
        username: String?,
        cityName: String,
        temperature: Float,
        pressure: Float,
        humidity: Float
    ): Boolean {
        val serverIP = CurrentLoggedUserData.getCurrentServerIP()
            clientSocket = createConnection(serverIP)

            if (clientSocket == null) {
                weatherDataSendingFailed(username, cityName, temperature, pressure, humidity)
                return false
            }
            return sendWeatherData(username, cityName, temperature, pressure, humidity)
    }

    private fun createConnection(serverIP: String): Socket? {
        val clientSocket: Socket?
        val socketAddress = InetSocketAddress(serverIP, 50005)
        try {
            clientSocket = Socket()
            clientSocket.connect(socketAddress, 3000)
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("Connection failed")
            return null
        }
        return clientSocket
    }

    private fun sendWeatherData(
        username: String?,
        cityName: String,
        temperature: Float,
        pressure: Float,
        humidity: Float
    ): Boolean {
        try {
            if (clientSocket != null) {
                val printWriter = PrintWriter(clientSocket!!.getOutputStream(), true)
                printWriter.println(WEATHER_INFO)
                printWriter.println(username)
                printWriter.println(cityName)
                printWriter.println(temperature)
                printWriter.println(pressure)
                printWriter.println(humidity)
                if (readWeatherAnswerFromServer(clientSocket!!)) {
                    dataSentSuccessfully(username)
                    return true
                } else {
                    weatherDataSendingFailed(username, cityName, temperature, pressure, humidity)
                }
            } else {
                throw NullPointerException()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun dataSentSuccessfully(username: String?) {
        showToast("Data sent to server")
    }

    private fun weatherDataSendingFailed(
        username: String?,
        cityName: String,
        temperature: Float,
        pressure: Float,
        humidity: Float
    ) {
        DatabaseHelper(context.get()).addUnsentWeatherItem(username, cityName, temperature, pressure, humidity)
        showToast("Data sending failed")
    }

    private fun readWeatherAnswerFromServer(clientSocket: Socket): Boolean {
        val br = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        return br.readLine() == WEATHER_SUCCESS
    }

    fun sendPendingWeatherData() {
        Thread {
            val cursor = DatabaseHelper(context.get()).unsentWeatherItems

            cursor.moveToFirst()
            if (cursor.count > 0) {
                do {
                    val id = cursor.getInt(0)
                    val username = cursor.getString(1)
                    val cityName = cursor.getString(2)
                    val temperature = cursor.getFloat(3)
                    val pressure = cursor.getFloat(4)
                    val humidity = cursor.getFloat(5)

                    val successfullySent = sendWeatherDataToServer(username, cityName, temperature, pressure, humidity)
                    if (successfullySent) {
                        DatabaseHelper(context.get()).deleteUnsentWeatherItem(id)
                    } else {
                        showToast("Pending data sending failed")
                        return@Thread
                    }
                } while (cursor.moveToNext())
            }
        }.start()
    }

    fun manageCalculatorItem(
        username: String, firstNumber: Float,
        secondNumber: Float, operation: String, result: Float
    ) {
        addToCalculatorDatabase(username, firstNumber, secondNumber, operation, result)

        Thread {
            sendCalculatorDataToServer(username, firstNumber, secondNumber, operation, result)
        }.start()
    }

    private fun addToCalculatorDatabase(
        username: String,
        firstNumber: Float,
        secondNumber: Float,
        operation: String,
        result: Float
    ) {
        DatabaseHelper(this.context.get()).addCalculatorItem(username, firstNumber, secondNumber, operation, result)
    }

    private fun sendCalculatorDataToServer(
        username: String, firstNumber: Float,
        secondNumber: Float, operation: String,
        result: Float
    ): Boolean {
        val serverIP = CurrentLoggedUserData.getCurrentServerIP()

            clientSocket = createConnection(serverIP)

            if (clientSocket == null) {
                calculatorDataSendingFailed(username, firstNumber, secondNumber, operation, result)
                return false
            }

            return sendCalculatorData(username, firstNumber,
                secondNumber, operation, result)
    }

    private fun sendCalculatorData(
        username: String,
        firstNumber: Float,
        secondNumber: Float,
        operation: String,
        result: Float
    ):  Boolean {
        try {
            if (clientSocket != null) {
                val printWriter = PrintWriter(clientSocket!!.getOutputStream(), true)
                printWriter.println(CALCULATOR_INFO)
                printWriter.println(username)
                printWriter.println(firstNumber)
                printWriter.println(secondNumber)
                printWriter.println(operation)
                printWriter.println(result)
                if (readCalculatorAnswerFromServer(clientSocket!!)) {
                    dataSentSuccessfully(username)
                    return true
                } else {
                    calculatorDataSendingFailed(username, firstNumber, secondNumber, operation, result)
                }
            } else {
                throw NullPointerException()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun readCalculatorAnswerFromServer(clientSocket: Socket): Boolean {
        val br = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        return br.readLine() == CALCULATOR_SUCCESS
    }

    private fun calculatorDataSendingFailed(
        username: String,
        firstNumber: Float,
        secondNumber: Float,
        operation: String,
        result: Float
    ) {
        DatabaseHelper(context.get()).addUnsentCalculatorItem(username, firstNumber, secondNumber, operation, result)
        showToast("Data sending failed")
    }

    fun sendPendingCalculatorData() {
        Thread {
            val cursor = DatabaseHelper(context.get()).unsentCalculatorItems

            cursor.moveToFirst()
            if (cursor.count > 0) {
                do {
                    val id = cursor.getInt(0)
                    val username = cursor.getString(1)
                    val firstNumber = cursor.getString(2)
                    val secondNumber = cursor.getFloat(3)
                    val operation = cursor.getFloat(4)
                    val result = cursor.getFloat(5)

                    val successfullySent =
                        sendWeatherDataToServer(username, firstNumber, secondNumber, operation, result)
                    if (successfullySent) {
                        DatabaseHelper(context.get()).deleteUnsentCalculatorItem(id)
                    } else {
                        showToast("Pending data sending failed")
                        return@Thread
                    }
                } while (cursor.moveToNext())
            }
        }.start()
    }

    private fun showToast(message: String) {
        context.get()?.runOnUiThread {
            Toast.makeText(context.get(), message, Toast.LENGTH_SHORT).show()
        }
    }
}