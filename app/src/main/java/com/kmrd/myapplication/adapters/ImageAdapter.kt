package com.kmrd.myapplication.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kmrd.myapplication.R

class ImageAdapter: RecyclerView.Adapter<ImageAdapter.ImageHolder>() {
    val mainArray = ArrayList<Uri>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_adapter_item, parent, false)
        return ImageHolder(view)

    }

    override fun getItemCount(): Int {
        return mainArray.size
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(mainArray[position])
    }



    class ImageHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        lateinit var imItem: ImageView
        fun setData(uri: Uri) {
            imItem = itemView.findViewById(R.id.imItem)
            imItem.setImageURI(uri)
        }

    }

    fun update(newList: ArrayList<Uri>) {
        mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }

}