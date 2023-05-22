package com.example.finalproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.adapters.itemAdapter
import com.example.finalproject.models.ItemModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TransporterLandingActivity : AppCompatActivity() {
    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var itemList: ArrayList<ItemModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transporter_landing)

        itemRecyclerView = findViewById(R.id.rvItem)
        itemRecyclerView.layoutManager = LinearLayoutManager(this)
        itemRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)
        firebaseAuth = FirebaseAuth.getInstance()

        itemList = arrayListOf<ItemModel>()

        var acceptedBtn = findViewById<Button>(R.id.button2)
        acceptedBtn.setOnClickListener{
            val intent = Intent(this,ShowAcceptedItem::class.java )
            startActivity(intent)
        }

        getEmployeesData()

    }
    private fun getEmployeesData() {

        val currentUser = FirebaseAuth.getInstance().currentUser
        val email = currentUser?.email

        itemRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Items")

        dbRef.orderByChild("statusM").equalTo("available to ship")
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val itemData = empSnap.getValue(ItemModel::class.java)
                        itemList.add(itemData!!)
                    }
                    val mAdapter = itemAdapter(itemList)
                    itemRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : itemAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@TransporterLandingActivity, ShowDetailsTransporterActivity::class.java)

                            //put extras
                            intent.putExtra("itemId", itemList[position].itemIDM)
                            intent.putExtra("item name", itemList[position].itemNameM)
                            intent.putExtra("item weight", itemList[position].weightM)
                            intent.putExtra("item height", itemList[position].heightM)
                            intent.putExtra("item width", itemList[position].widthM)
                            intent.putExtra("item date", itemList[position].dateM)
                            intent.putExtra("item status", itemList[position].statusM)
                            startActivity(intent)
                        }

                    })
                    if (snapshot.exists()) {
                        itemRecyclerView.visibility = View.VISIBLE
                        tvLoadingData.visibility = View.GONE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}