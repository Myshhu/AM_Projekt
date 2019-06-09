package com.example.am_projekt.weatherActivity

import android.app.Activity
import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Build
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.am_projekt.R
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class WeatherDataDownloader(context: Activity) : AsyncTask<Void, Int, Void>() {

    private var context: WeakReference<Activity> = WeakReference(context)
    private var textViewTemp: WeakReference<TextView>? = null
    private var textViewPressure: WeakReference<TextView>? = null
    private var textViewHumidity: WeakReference<TextView>? = null
    private var editTextCityName: WeakReference<EditText>? = null

    private lateinit var myJSONObject: JSONObject

    override fun doInBackground(vararg params: Void?): Void? {
        val connection: HttpURLConnection
        textViewTemp = WeakReference(context.get()?.findViewById(R.id.textViewTemperature))
        textViewPressure = WeakReference(context.get()?.findViewById(R.id.textViewPressure))
        textViewHumidity = WeakReference(context.get()?.findViewById(R.id.textViewHumidity))
        editTextCityName = WeakReference(context.get()?.findViewById(R.id.editTextCityName))
        val cityName = editTextCityName?.get()?.text.toString()

        try {
            val url =
                URL("https://api.openweathermap.org/data/2.5/weather?q= $cityName &APPID=4871bda0d9f2f723cb0da219ef9f1a28")
            connection = url.openConnection() as HttpURLConnection
            connection.connect()

            //Read response
            when {
                connection.responseCode == 200 -> {
                    myJSONObject = createJSONFromResponse(connection)
                    publishProgress(200)
                }
                connection.responseCode == 404 -> publishProgress(404)
                else -> publishProgress(connection.responseCode)
            }
            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    override fun onProgressUpdate(vararg values: Int?) {

        //City not found dialog
        when {
            values[0] == 404 -> {
                val builder: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    AlertDialog.Builder(context.get(), android.R.style.Theme_Material_Dialog_Alert)
                } else {
                    AlertDialog.Builder(context.get())
                }
                builder.setTitle("Error").setMessage("City not found").show()
                Toast.makeText(context.get(), "City not found", Toast.LENGTH_LONG).show()
            }
            values[0] == 200 -> updateTextViews()
            else -> Toast.makeText(context.get(), "Error code: " + Integer.toString(values[0] ?: 0), Toast.LENGTH_LONG).show()
        }
    }

    private fun createJSONFromResponse(connection: HttpURLConnection): JSONObject {
        val bufferedReader: BufferedReader

        val inputStream = connection.inputStream
        bufferedReader = BufferedReader(InputStreamReader(inputStream))

        var line: String?
        val result = StringBuilder()

        while (true) {
            line = bufferedReader.readLine()
            if (line == null) {
                break
            }
            result.append(line).append("\n")
        }
        return JSONObject(result.toString())
    }

    private fun updateTextViews() {
        try {
            val humidity = myJSONObject.getJSONObject("main").getString("humidity")
            val pressure = myJSONObject.getJSONObject("main").getString("pressure")
            val rawTemperature = java.lang.Double.parseDouble(myJSONObject.getJSONObject("main").getString("temp"))
            val temperature = Math.round((rawTemperature - 273.15) * 100.0) / 100.0
            textViewTemp?.get()?.text = String.format("%s °C", java.lang.Double.toString(temperature))
            textViewPressure?.get()?.text = String.format("%s hPa", pressure)
            textViewHumidity?.get()?.text = String.format("%s%%", humidity)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}