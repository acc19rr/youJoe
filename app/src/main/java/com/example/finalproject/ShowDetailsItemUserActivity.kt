package com.example.finalproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.finalproject.models.ItemModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ShowDetailsItemUserActivity : AppCompatActivity() {

    private lateinit var tvItemName: TextView
    private lateinit var tvItemId: TextView
    private lateinit var tvItemHeight: TextView
    private lateinit var tvItemWidth: TextView
    private lateinit var tvItemWeight: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var tvDate: TextView
    private lateinit var tvStatus: TextView
    private lateinit var itemId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_details_item_user)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("itemId").toString(),
                intent.getStringExtra("item name").toString(),
                intent.getStringExtra("item height").toString(),
                intent.getStringExtra("item weight").toString(),
                intent.getStringExtra("item width").toString(),
                intent.getStringExtra("item date").toString()


            )
        }

        btnDelete.setOnClickListener {
            intent.getStringExtra("itemId")?.let { it1 ->
                deleteRecord(
                    it1
                )
            }
        }
    }

    private fun deleteRecord(
        id: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Items").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Item deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, UserLandingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
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
        btnDelete = findViewById(R.id.btnDelete)



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

    @SuppressLint("MissingInflatedId")
    private fun openUpdateDialog(
        itemId:String,
        itemName: String,
        itemHeight: String,
        itemWeight: String,
        itemWidth: String,
        itemDate: String

    ){
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etItemName = mDialogView.findViewById<EditText>(R.id.etItem)
        val etItemWeight = mDialogView.findViewById<EditText>(R.id.etWeight)
        val etItemHeight = mDialogView.findViewById<EditText>(R.id.etHeight)
        val etItemWidth = mDialogView.findViewById<EditText>(R.id.etWidth)
        val etItemDate = mDialogView.findViewById<EditText>(R.id.etDate)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdate)

        etItemName.setText(intent.getStringExtra("item name").toString())
        etItemWeight.setText(intent.getStringExtra("item weight").toString())
        etItemHeight.setText(intent.getStringExtra("item height").toString())
        etItemWidth.setText(intent.getStringExtra("item width").toString())
        etItemDate.setText(intent.getStringExtra("item date").toString())

        mDialog.setTitle("Updating $itemName Record" )

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateItemData(
                itemId,
                etItemName.text.toString(),
                etItemHeight.text.toString(),
                etItemWeight.text.toString(),
                etItemWidth.text.toString(),
                etItemDate.text.toString()
            )

            Toast.makeText(applicationContext, "Item data updated", Toast.LENGTH_LONG).show()

            // setting updated data to text views

            tvItemName.text =  etItemName.text.toString()
            tvItemHeight.text = etItemHeight.text.toString()
            tvItemWeight.text = etItemWeight.text.toString()
            tvDate.text = etItemDate.text.toString()
            tvItemWidth.text = etItemWidth.text.toString()



            alertDialog.dismiss()
        }

    }


    private fun updateItemData(id: String,
                               name: String,
                               weight: String,
                               height: String,
                               width:String,
                               date:String
                ){

        val currentUser = FirebaseAuth.getInstance().currentUser
        val email = currentUser?.email

       /* val updates = HashMap<String, Any>()

        if (weight.isNotEmpty()) {
            updates["weight"] = weight
        }
        if (height.isNotEmpty()) {
            updates["height"] = height
        }
        if (width.isNotEmpty()) {
            updates["width"] = width
        }
        if (date.isNotEmpty()) {
            updates["date"] = date
        }

        dbRef.updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Item data updated", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { error ->
                Toast.makeText(applicationContext, "Failed to update item data: ${error.message}", Toast.LENGTH_LONG).show()
            }
*/
        val des = "desciption test"
        val es = "estimate test"
        val reciever = "reciever test"
        val status = "status test"

        val dbRef = FirebaseDatabase.getInstance().getReference("Items").child(id)
        val itemInfo = ItemModel(id, email, name, des, height, weight, width, es, date,reciever, tvStatus.text.toString())
        dbRef.setValue(itemInfo)
    }





}