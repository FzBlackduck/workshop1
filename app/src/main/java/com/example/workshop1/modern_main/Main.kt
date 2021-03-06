package com.example.workshop1.modern_main


import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.facedetector.CameraXFaceDetector
import com.example.googlemap.MapsActivity
import com.example.workshop1.*
import com.example.workshop1.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FacebookAuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_modern_main.*
import java.io.IOException


class Main : AppCompatActivity(), MyAdapter.MainClickListner {




    var myLists: MutableList<MyList>? = null
    var rv: RecyclerView? = null
    var adapter: MyAdapter? = null

    var firebaseUser: FirebaseUser? = null
    var refUsers: DatabaseReference? = null
    var imageBitmap: Bitmap? = null
    var name: TextView? = null
    var image : ImageView? = null
    //var getbarcode: ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation =  (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_modern_main)



        rv = findViewById<View>(R.id.rec) as RecyclerView
        rv!!.setHasFixedSize(true)
        rv!!.layoutManager = GridLayoutManager(this, 2)
        myLists = ArrayList()

        name = findViewById(R.id.name_account)
        image =findViewById(R.id.image_account)

        //Google Sign in

        val signInAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (signInAccount != null) {
            name?.text = signInAccount.displayName
            Picasso.get()
                    .load(signInAccount.photoUrl)
                    .into(image)
        }else{
            loaduser()
        }

//        val user = FirebaseAuth.getInstance().currentUser
//        if(user != null) {
//            name?.text = user!!.displayName
//            Picasso.get()
//                .load(user!!.photoUrl)
//                .into(image)
//        }else{
//            loaduser()
//        }


        getdata()


        scanbarcode2.setOnClickListener {
            val i = Intent(this, Choicescan::class.java)
            startActivity(i)
        }


        var home = findViewById<View>(R.id.home)
        home.setOnClickListener {
            startActivity(Intent(this, Main::class.java))
        }

        var settingbtn = findViewById<ImageView>(R.id.setting_modern_main)
        settingbtn.setOnClickListener {
            startActivity(Intent(this, Account::class.java))
        }
    }

    private fun getdata() {
//        myLists!!.add(MyList(R.drawable.a,"IMAGE SCANNER"))
//        myLists!!.add(MyList(R.drawable.b,"REALTIME SCANNER"))
        myLists!!.add(MyList(R.drawable.c,"DATA LIST"))
        myLists!!.add(MyList(R.drawable.d,"STAR LIST"))
        myLists!!.add(MyList(R.drawable.e,"VOTE"))
        myLists!!.add(MyList(R.drawable.f,"SHOP AROUND"))

        adapter = MyAdapter(myLists!!, this,this)
        rv!!.adapter = adapter
    }
    override fun onClick(myList: MyList, position: Int) {



//          if(myList.titlename == "IMAGE SCANNER"){
//            val intent = Intent(this, Select::class.java)
//              intent.putExtra("select","IMAGE SCANNER")
//            startActivity(intent)
//          }
//        if(myList.titlename == "REALTIME SCANNER"){
//            val intent = Intent(this, Select::class.java)
//            intent.putExtra("select","REALTIME SCANNER")
//            startActivity(intent)
//        }
        if(myList.titlename == "DATA LIST"){
            val intent = Intent(this, Showproduct::class.java)
            startActivity(intent)
        }
        if(myList.titlename == "STAR LIST"){
            val intent = Intent(this, StarList::class.java)
            startActivity(intent)
        }
        if(myList.titlename == "VOTE"){
            val intent = Intent(this, CameraXFaceDetector::class.java)
            startActivity(intent)
        }
        if(myList.titlename == "SHOP AROUND"){
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }


    }
    fun loaduser(){

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Account")
                .child(firebaseUser!!.uid)
                .child("profile")
        refUsers!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val nameDB = dataSnapshot.child("name").value.toString()
                val imageDB = dataSnapshot.child("image").value.toString()
                imageBitmap = decodeFromFirebaseBase64(imageDB)

                name?.text = nameDB
                image?.setImageBitmap(imageBitmap)


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