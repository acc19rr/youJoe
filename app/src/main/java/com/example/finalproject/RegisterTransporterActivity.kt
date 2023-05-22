package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.finalproject.databinding.ActivityRegisterTransporterBinding
import com.example.finalproject.models.TransporterModel
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterTransporterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterTransporterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    private lateinit var userName: EditText
    private lateinit var userFamilyName: EditText
    private lateinit var userAge: EditText
    private lateinit var userEmail: EditText
    private lateinit var pdfView : PDFView

    private var isChecked: Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterTransporterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        userEmail = findViewById(R.id.emailRegT)
        userName = findViewById(R.id.nameRegT)
        userFamilyName = findViewById(R.id.familyNameRegT)
        userAge = findViewById(R.id.ageRegT)

        dbRef = FirebaseDatabase.getInstance().getReference("Transporters")

        binding.textView.setOnClickListener {
            val intent = Intent(this, LoginTransporterActivity::class.java)
            startActivity(intent)
        }

        binding.buttonConditions.setOnClickListener {
            val intent = Intent(this, RegisterTransporterContractActivity::class.java)
            startActivity(intent)
        }

        isChecked = intent.getBooleanExtra("checkboxState", false)
        binding.conditionsRegister.isChecked = isChecked


        binding.button.setOnClickListener {

            if(isChecked){
            val email = binding.emailRegT.text.toString()
            val pass = binding.passwordConfirmRegT.text.toString()
            val confirmPass = binding.passwordConfirmRegT.text.toString()

            val nameDB = userName.text.toString()
            val familyNameDB = userFamilyName.text.toString()
            val ageDB = userAge.text.toString()

            val transporter = TransporterModel(email, nameDB, familyNameDB, ageDB)



            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            dbRef.child(nameDB).setValue(transporter)
                            val intent = Intent(this, LoginTransporterActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
            else {
                Toast.makeText(this, "Accept conditions before ", Toast.LENGTH_SHORT).show()
            }
        }
    }
}