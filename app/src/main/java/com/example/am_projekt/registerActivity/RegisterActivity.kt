package com.example.am_projekt.registerActivity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.view.View
import android.widget.Toast
import com.example.am_projekt.MainActivity
import com.example.am_projekt.R
import com.example.am_projekt.variables.CurrentLoggedUser

import kotlinx.android.synthetic.main.content_register.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.NullPointerException
import java.net.Socket

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }


    fun btnRegisterClick(v: View) {
        Thread {
            val username = etRegisterLogin.text.toString()
            val password = etRegisterPassword.text.toString()
            val confirmedPassword = etRegisterConfirmPassword.text.toString()
            val serverIP = etRegisterIP.text.toString()
            var clientSocket: Socket? = null

            if (username == "" || password == "" || confirmedPassword == "") {
                showToast("Please fill username and passwords")
                return@Thread
            }

            if (password != confirmedPassword) {
                showToast("Given passwords do not match")
                return@Thread
            }

            clientSocket = createConnection(serverIP)
            if (clientSocket == null) {
                return@Thread
            }

            sendUsernameAndPasswordToServer(username, password, clientSocket)
        }.start()

    }

    private fun createConnection(serverIP: String): Socket? {
        val clientSocket: Socket?
        try {
            clientSocket = Socket(serverIP, 50005)
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("Connection failed")
            return null
        }
        return clientSocket
    }

    private fun sendUsernameAndPasswordToServer(username: String, password: String, clientSocket: Socket?) {
        try {
            if(clientSocket != null) {
                val printWriter = PrintWriter(clientSocket.getOutputStream(), true)
                printWriter.println("register")
                printWriter.println(username)
                printWriter.println(password)
                if(readAnswerFromServer(clientSocket)) {
                    afterSuccessfulRegister(username)
                } else {
                    afterFailedRegister()
                }
            } else {
                throw NullPointerException()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun readAnswerFromServer(clientSocket: Socket): Boolean {
        val br = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        return br.readLine() == "registered"
    }

    private fun afterSuccessfulRegister(username: String) {
        CurrentLoggedUser.setCurrentLoggedUsername(username)
        showToast("Successfully registered user")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun afterFailedRegister() {
        showToast("Register failed")
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

}
