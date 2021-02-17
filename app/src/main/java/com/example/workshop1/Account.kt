package com.example.workshop1

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.login.Loginpage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.io.IOException

class Account : AppCompatActivity() {

    var firebaseUser: FirebaseUser? = null
    var refUsers: DatabaseReference? = null
    var name: TextView? = null
    var mail: TextView? = null
    var logout: Button? = null
    var image : ImageView? = null
    var imageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        name = findViewById(R.id.name_account)
        mail = findViewById(R.id.email_account)
        image =findViewById(R.id.image_account)
        logout = findViewById(R.id.logout_account)

         /** Toolbar */
        val toolbar = findViewById<Toolbar>(R.id.toolbaraccount)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { super.onBackPressed() }

        logout?.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(applicationContext, Loginpage::class.java)
            startActivity(intent)
        }

        loaduser()
    }


    fun loaduser(){

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Account").child(firebaseUser!!.uid)
        refUsers!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // for (datas in dataSnapshot.children) {
                val nameDB = dataSnapshot.child("name").value.toString()
                val usernameDB = dataSnapshot.child("username").value.toString()
                val imageDB = dataSnapshot.child("image").value.toString()
                imageBitmap = decodeFromFirebaseBase64(imageDB)

                name?.text = nameDB
                mail?.text = usernameDB
                image?.setImageBitmap(imageBitmap)
                // }

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    @Throws(IOException::class)
    private fun decodeFromFirebaseBase64(value: String?): Bitmap {
        val decodedByteArray = android.util.Base64.decode(value, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
    }



}