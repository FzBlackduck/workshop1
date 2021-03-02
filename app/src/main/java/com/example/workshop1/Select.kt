package com.example.workshop1


import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.*

import androidx.appcompat.app.AppCompatActivity
import com.example.qrscan.CameraXQRscan
import com.example.workshop1.modern_main.Main
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_select.*


class Select:AppCompatActivity() {

    var firebaseUser: FirebaseUser? = null
    var refUsers: DatabaseReference? = null
    val List_store = arrayListOf("Please select store")
    var select = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation =  (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_select)

        var bundle = intent.extras
        if (bundle != null) {
            select = bundle.getString("select")!!

        }


      val option = findViewById<Spinner>(R.id.option)
        var textString:String = ""


        select_ok.setOnClickListener {
            if(textString == "Please select store"){
                Toast.makeText(this, "Not Found store" ,Toast.LENGTH_SHORT).show()
            }else {
                if(select == "IMAGE SCANNER") {
                    val intent = Intent(this, StillImageActivity::class.java)
                    intent.putExtra("qr", textString)
                    startActivity(intent)
                }
                if(select == "REALTIME SCANNER"){
                    val intent = Intent(this, CameraXLivePreviewActivity::class.java)
                    intent.putExtra("qr", textString)
                    startActivity(intent)
                }
            }
        }


        val scanqrbtn = findViewById<ImageView>(R.id.scanqr)
        scanqrbtn.setOnClickListener {
            startActivity(Intent(this,CameraXQRscan ::class.java))
        }


        var home = findViewById<View>(R.id.home)
        home.setOnClickListener {
            val i = Intent(this, Main::class.java)
            startActivity(i)
        }

        loadstoreid()

        option.adapter =  ArrayAdapter(this,android.R.layout.simple_list_item_1,List_store)
        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                textString = List_store[position]
                if(textString == "a"){
                    Toast.makeText(applicationContext,""+textString,Toast.LENGTH_SHORT).show()

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                textString = "Please select store"
            }
        }



    }

    private fun loadstoreid(){
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers =  FirebaseDatabase.getInstance().reference.child("Account")
                .child(firebaseUser!!.uid)
                .child("store")
        refUsers!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (datas in dataSnapshot.children) {
                    val key = datas.key
                    List_store.add("$key")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }



}