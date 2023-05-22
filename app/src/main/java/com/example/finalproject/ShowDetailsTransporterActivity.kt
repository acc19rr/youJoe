package com.example.finalproject

import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

class ShowDetailsTransporterActivity : AppCompatActivity() {
    private lateinit var tvItemName: TextView
    private lateinit var tvItemId: TextView
    private lateinit var tvItemHeight: TextView
    private lateinit var tvItemWidth: TextView
    private lateinit var tvItemWeight: TextView
    private lateinit var btnUpdate: Button
    private lateinit var tvDate: TextView
    private lateinit var tvStatus: TextView
    private lateinit var itemId: String
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_details_transporter)

        firebaseAuth = FirebaseAuth.getInstance()



        initView()
        setValuesToViews()
    }

    private fun sendEmailToTransporter() {



        //commented for testing purposes
        /*val currentUser = FirebaseAuth.getInstance().currentUser
        val email = currentUser?.email*/


        GlobalScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val mediaType = "application/x-www-form-urlencoded".toMediaType()

            val apiKey = "5a737b79fa09d11af019d040c536f7bb-6b161b0a-d336732b"
            val domain = "sandbox60d55597f40148febfe8b020978b24d5.mailgun.org"
            val fromEmail = "rakemryad@gmail.com"
            val toEmail = "rakemryad@gmail.com"
            val subject = "confirmation for the transporter"
            val text = "this is what you will do next"

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
    }

    private fun sendEmailToReceiver() {



        //commented for testing purposes
        /*val currentUser = FirebaseAuth.getInstance().currentUser
        val email = currentUser?.email*/


        GlobalScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val mediaType = "application/x-www-form-urlencoded".toMediaType()

            val apiKey = "5a737b79fa09d11af019d040c536f7bb-6b161b0a-d336732b"
            val domain = "sandbox60d55597f40148febfe8b020978b24d5.mailgun.org"
            val fromEmail = "rakemryad@gmail.com"
            val toEmail = "rakemryad@gmail.com"
            val subject = "confirmation for the receiver"
            val text = "this is what you will do next"

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
    }





    private fun initView() {
        tvItemId = findViewById(R.id.tvItemId)
        tvItemName = findViewById(R.id.tvItem)
        tvItemWeight= findViewById(R.id.tvWeight)
        tvItemHeight = findViewById(R.id.tvHeight)
        tvItemWidth = findViewById(R.id.tvWidth)
        tvDate = findViewById(R.id.tvDate)
        tvStatus = findViewById(R.id.tvStatus)
        btnUpdate = findViewById(R.id.btnUpdate)



        btnUpdate.setOnClickListener {
            updateEmployeeStatus() // call the method to update the status attribut
            sendEmailToTransporter()
            sendEmailToReceiver()
        }

    }

    private fun setValuesToViews() {
        tvItemName.text = intent.getStringExtra("item name")
        tvItemId.text = intent.getStringExtra("itemId")
        itemId = tvItemId.text.toString()
        tvItemWeight.text = intent.getStringExtra("item weight")
        tvItemWidth.text = intent.getStringExtra("item width")
        tvItemHeight.text = intent.getStringExtra("item height")
        tvDate.text = intent.getStringExtra("item date")
        tvStatus.text = intent.getStringExtra("item status")

    }

    private fun updateEmployeeStatus() {

        val currentUser = FirebaseAuth.getInstance().currentUser
        val email = currentUser?.email

        val database = FirebaseDatabase.getInstance()
        val employeeRef = database.getReference("Items").child(itemId)
        val statusUpdate = HashMap<String, Any>()
        statusUpdate["statusM"] = email.toString()
        employeeRef.updateChildren(statusUpdate)
            .addOnSuccessListener {
                Toast.makeText(this, "Status updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show()
            }
    }
}