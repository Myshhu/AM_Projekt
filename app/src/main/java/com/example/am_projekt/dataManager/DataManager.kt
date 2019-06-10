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
    private val WEATHER_SUCCESS = "datasentsuccesfully"

    fun manageWeatherItem(username: String?, cityName: String,
                          temperature: Float, pressure: Float, humidity: Float) {
        addToWeatherDatabase(username, cityName, temperature, pressure, humidity)
        sendDataToServer(username, cityName, temperature, pressure, humidity)
    }

    private fun addToWeatherDatabase(username: String?, cityName: String, temperature: Float, pressure: Float, humidity: Float) {
        DatabaseHelper(this.context.get()).addWeatherItem(username, cityName, temperature, pressure, humidity)
    }

    private fun sendDataToServer(username: String?, cityName: String, temperature: Float, pressure: Float, humidity: Float) {
        val serverIP = CurrentLoggedUserData.getCurrentServerIP()

        Thread {
            clientSocket = createConnection(serverIP)

            if (clientSocket == null) {
                return@Thread
            }

            sendWeatherData(username, cityName, temperature, pressure, humidity)
        }.start()
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

    private fun sendWeatherData(username: String?, cityName: String, temperature: Float, pressure: Float, humidity: Float) {
        try {
            if (clientSocket != null) {
                val printWriter = PrintWriter(clientSocket!!.getOutputStream(), true)
                printWriter.println(WEATHER_INFO)
                printWriter.println(username)
                printWriter.println(cityName)
                printWriter.println(temperature)
                printWriter.println(pressure)
                printWriter.println(humidity)
                if (readAnswerFromServer(clientSocket!!)) {
                    dataSentSuccessfully(username)
                } else {
                    dataSendingFailed()
                }
            } else {
                throw NullPointerException()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun dataSentSuccessfully(username: String?) {
        showToast("Data sent to server")
    }

    private fun dataSendingFailed() {
        showToast("Data sending failed")
    }

    private fun readAnswerFromServer(clientSocket: Socket): Boolean {
        val br = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        return br.readLine() == WEATHER_SUCCESS
    }

    private fun showToast(message: String) {
        context.get()?.runOnUiThread {
            Toast.makeText(context.get(), message, Toast.LENGTH_SHORT).show()
        }
    }
}