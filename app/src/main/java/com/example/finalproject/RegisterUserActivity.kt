package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.finalproject.databinding.ActivityRegisterUserBinding
import com.example.finalproject.models.UserModel
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterUserBinding
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

        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //for user authentication
        firebaseAuth = FirebaseAuth.getInstance()

        //initializing variables for the texfields of the form
        userEmail = findViewById(R.id.emailReg)
        userName = findViewById(R.id.nameReg)
        userFamilyName = findViewById(R.id.family_name_reg)
        userAge = findViewById(R.id.ageReg)

        //to push the user data in the Users collection in the database
        dbRef = FirebaseDatabase.getInstance().getReference("Users")


        //button login
        binding.textView.setOnClickListener {
            val intent = Intent(this, LoginUserActivity::class.java)
            startActivity(intent)
        }


        //button to display the contract as a pdf file
        binding.buttonConditions.setOnClickListener {
            val intent = Intent(this, RegisterUserContractActivity::class.java)
            startActivity(intent)
        }


        isChecked = intent.getBooleanExtra("checkboxState", false)
        binding.conditionsRegister.isChecked = isChecked


        //button register
        binding.button.setOnClickListener {


            if(isChecked){
            val email = binding.emailReg.text.toString()
            val pass = binding.passwordReg.text.toString()
            val confirmPass = binding.passwordConfirmReg.text.toString()

            val nameDB = userName.text.toString()
            val familyNameDB = userFamilyName.text.toString()
            val ageDB = userAge.text.toString()



            // initializing user
            val user = UserModel(email,nameDB,familyNameDB,ageDB)


            // if success registering the user and creating an instance of the user with the data
            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            dbRef.child(nameDB).setValue(user)
                            val intent = Intent(this, LoginUserActivity::class.java)
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
            else{
                Toast.makeText(this, "Accept conditions before ", Toast.LENGTH_SHORT).show()

            }
        }
    }
}