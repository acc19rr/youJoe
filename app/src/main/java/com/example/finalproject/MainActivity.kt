package com.example.finalproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var loginBtn = findViewById<Button>(R.id.button_login_main)
        loginBtn.setOnClickListener{
            val intent = Intent(this,LoginUserActivity::class.java )
            startActivity(intent)
        }

        var registerBtn = findViewById<Button>(R.id.button_register_main)
        registerBtn.setOnClickListener{
            val intent = Intent(this,RegisterUserActivity::class.java )
            startActivity(intent)
        }

        var workBtn = findViewById<Button>(R.id.work_main)
        workBtn.setOnClickListener{
            val intent = Intent(this,RegisterTransporterActivity::class.java )
            startActivity(intent)
        }

        //sendEmail()


    }


    /*private fun sendEmail() {
        GlobalScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val mediaType = "application/x-www-form-urlencoded".toMediaType()

            val apiKey = "5a737b79fa09d11af019d040c536f7bb-6b161b0a-d336732b"
            val domain = "sandbox60d55597f40148febfe8b020978b24d5.mailgun.org"
            val fromEmail = "rakemryad@gmail.com"
            val toEmail = "rakemryad@gmail.com"
            val subject = "test"
            val text = "i made it work"

            val requestBody =
                "from=$fromEmail&to=$toEmail&subject=$subject&text=$text".toRequestBody(mediaType)
            val request = Request.Builder()
                .url("https://api.mailgun.net/v3/$domain/messages")
                .addHeader(
                    "Authorization",
                    "Basic " + Base64.encodeToString(("api:$apiKey").toByteArray(), Base64.NO_WRAP)
                )
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                val responseCode = response.code
                val responseBody = response.body?.string()
                val responseHeaders = response.headers

                Log.d("EmailSender", "Response Code: $responseCode")
                Log.d("EmailSender", "Response Body: $responseBody")
                Log.d("EmailSender", "Response Headers: $responseHeaders")
            }
        }
    }*/




}

