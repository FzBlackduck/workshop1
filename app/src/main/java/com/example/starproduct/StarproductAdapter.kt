package com.example.starproduct

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.barcodescanner.BarcodeAdapter
import com.example.barcodescanner.User
import com.example.workshop1.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class StarproductAdapter(private val starList: ArrayList<Star>, var clickListner1 : OnBarcodeClickListner) : RecyclerView.Adapter<StarproductAdapter.ViewHolder>() {



    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarproductAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.activity_star_list,
            parent,
            false
        )
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: StarproductAdapter.ViewHolder, position: Int) {
        holder.bindItems(starList[position],clickListner1)


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return starList.size
    }



    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("WrongViewCast")


        fun bindItems(star: Star, action:OnBarcodeClickListner) {
            val textViewName = itemView.findViewById(R.id.namestar) as TextView
            val textViewPrice  = itemView.findViewById(R.id.pricestar) as TextView
            var setimageview = itemView.findViewById<View>(R.id.imageView1star) as ImageView


            textViewName.text = star.name
            textViewPrice.text = star.price


            Picasso.get()
                .load("" + star.image)
                .into(setimageview)

            itemView.setOnClickListener{
                action.onClick(star,adapterPosition)
            }

        }


    }
    interface OnBarcodeClickListner {
        fun onClick(starList: Star,position: Int) {



        }

    }

}