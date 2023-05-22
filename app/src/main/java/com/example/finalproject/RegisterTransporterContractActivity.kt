package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import com.github.barteksc.pdfviewer.PDFView

class RegisterTransporterContractActivity : AppCompatActivity() {

    lateinit var myCheckBox: CheckBox
    lateinit var pdfView : PDFView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user_contract)


        pdfView = findViewById(R.id.pdfView)
        myCheckBox = findViewById(R.id.conditions_register)

        myCheckBox.setOnCheckedChangeListener { _, isChecked ->
            navigateToNextActivity(isChecked)
        }

        val assetManager = this.assets
        val inputStream = assetManager.open("transporter contract.pdf")
        pdfView.fromStream(inputStream).load()
    }

    private fun navigateToNextActivity(isChecked: Boolean) {
        val intent = Intent(this@RegisterTransporterContractActivity, RegisterTransporterActivity::class.java)
        intent.putExtra("checkboxState", isChecked)
        startActivity(intent)
    }



}
