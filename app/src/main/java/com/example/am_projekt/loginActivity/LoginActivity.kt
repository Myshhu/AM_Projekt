package com.example.am_projekt.loginActivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.am_projekt.R
import kotlinx.android.synthetic.main.content_login.*
import java.io.PrintWriter
import java.lang.NullPointerException
import java.net.Socket

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
                Toast.makeText(this, "Please fill username and password", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show()
            return null
        }
        return clientSocket
    }

    private fun sendUsernameAndPasswordToServer(username: String, password: String, clientSocket: Socket?) {
        try {
            if(clientSocket != null) {
                val printWriter = PrintWriter(clientSocket.getOutputStream(), true)
                printWriter.println(username)
                printWriter.println(password)
            } else {
                throw NullPointerException()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

