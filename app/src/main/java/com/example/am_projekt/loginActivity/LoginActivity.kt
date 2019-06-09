package com.example.am_projekt.loginActivity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.am_projekt.MainActivity
import com.example.am_projekt.R
import com.example.am_projekt.variables.CurrentLoggedUser
import kotlinx.android.synthetic.main.content_login.*
import java.io.PrintWriter
import java.lang.NullPointerException
import java.net.Socket
import java.io.BufferedReader
import java.io.InputStreamReader


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun btnLoginClick(v: View) {
        Thread {
            val username = etLogin.text.toString()
            val password = etPassword.text.toString()
            val serverIP = etIP.text.toString()
            var clientSocket: Socket? = null

            if (username == "" || password == "") {
                showToast("Please fill username and password")
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
                printWriter.println("login")
                printWriter.println(username)
                printWriter.println(password)
                if(readAnswerFromServer(clientSocket)) {
                    afterSuccessfulLogin(username)
                } else {
                    afterFailedLogin()
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
        return br.readLine() == "verified"
    }

    private fun afterSuccessfulLogin(username: String) {
        CurrentLoggedUser.setCurrentLoggedUsername(username)
        showToast("Logged in")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun afterFailedLogin() {
        showToast("Login failed")
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}

