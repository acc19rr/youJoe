package com.example.finalproject

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import com.example.finalproject.models.ItemModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class InsertItemActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    //declaring variables

    private lateinit var item: EditText
    private lateinit var description: EditText
    private lateinit var weight: EditText
    private lateinit var height: EditText
    private lateinit var width: EditText
    private lateinit var estimatedValue: EditText
    private lateinit var date: EditText
    private lateinit var nameReceiver: EditText
    private lateinit var btnSave: Button
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    //database reference
    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_item)

        //text fields
        item = findViewById(R.id.item_register)
        description = findViewById(R.id.description_register)
        weight = findViewById(R.id.weight_register)
        height = findViewById(R.id.height_register)
        width = findViewById(R.id.width_register)
        estimatedValue = findViewById(R.id.estimated_value_register)
        date = findViewById(R.id.date)
        nameReceiver = findViewById(R.id.sender)
        btnSave = findViewById(R.id.button_submit_object)


        firebaseAuth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("Items")

        btnSave.setOnClickListener {
            saveItemsData()
        }
    }

    fun showDatePickerDialog(view: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, this, year, month, day)
        datePickerDialog.show()
    }

    private fun saveItemsData() {


        // getting the email of the current user
        val currentUser = firebaseAuth.currentUser
        val email = currentUser?.email

        //getting values from text fields

        val itemNameDB = item.text.toString()
        val descriptionDB = description.text.toString()
        val weightDB =  weight.text.toString()
        val heightDB = height.text.toString()
        val widthDB = width.text.toString()
        val estimatedValueDB =  estimatedValue.text.toString()
        val nameReceiverDB =  nameReceiver.text.toString()
        val dateDB =  date.text.toString()
        val statusDB = "available to ship"
        val itemId = dbRef.push().key!!


        //check if fields are empty

        if (itemNameDB.isEmpty()) {
            item.error = "Please enter name"
        }
        if (descriptionDB.isEmpty()) {
            description.error = "Please enter description"
        }
        if (weightDB.isEmpty()) {
            weight.error = "Please enter weight"
        }
        if (heightDB.isEmpty()) {
            height.error = "Please enter height"
        }
        if (widthDB.isEmpty()) {
            width.error = "Please enter width"
        }
        if (estimatedValueDB.isEmpty()) {
            estimatedValue.error = "Please enter estimated value"
        }
        if (nameReceiverDB.isEmpty()) {
            nameReceiver.error = "Please enter name of the receiver"
        }

        //creating an item instance

        val item = ItemModel(itemId, email, itemNameDB, descriptionDB, weightDB, heightDB, widthDB,dateDB, estimatedValueDB,nameReceiverDB,statusDB)


        //creating a child item in the items collection
        dbRef.child(itemId).setValue(item)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()


            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
        val intent = Intent(this, UserLandingActivity::class.java)
        startActivity(intent)

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val selectedDate = calendar.time
        val formattedDate = dateFormat.format(selectedDate)

        date.setText(formattedDate)
    }
}