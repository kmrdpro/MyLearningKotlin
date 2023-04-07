package com.kmrd.myapplication.frag

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kmrd.myapplication.R
import com.kmrd.myapplication.act.EditAdsAct
import com.kmrd.myapplication.utils.ImagePicker
import com.kmrd.myapplication.utils.ItemTouchMoveCallback
import io.ak1.pix.PixFragment
import io.ak1.pix.helpers.PixBus
import io.ak1.pix.helpers.PixEventCallback


class SelectImageRvAdapter: RecyclerView.Adapter<SelectImageRvAdapter.ImageHolder>(), ItemTouchMoveCallback.ItemTouchAdapter {

    val mainArray = ArrayList<Uri>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_image_frag_item, parent, false)
        return ImageHolder(view, parent.context, this)
    }

    override fun getItemCount(): Int {
        return mainArray.size
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(mainArray[position])
    }

    override fun onMove(startPos: Int, targetPos: Int) {
        val targetItem = mainArray[targetPos]
        mainArray[targetPos] = mainArray[startPos]
        mainArray[startPos] = targetItem
        notifyItemMoved(startPos, targetPos)

    }

    override fun onClear() {
        notifyDataSetChanged()
    }

    class ImageHolder(itemView: View, val context: Context, val adapter: SelectImageRvAdapter) : RecyclerView.ViewHolder(itemView) {
        lateinit var tvTitle: TextView
        lateinit var image: ImageView
        lateinit var imEditImage: ImageButton
        lateinit var imDeleteImage: ImageButton

        fun setData(item: Uri) {
            var arr2 = arrayListOf<Uri>()
            tvTitle = itemView.findViewById(R.id.tvTitle)
            image = itemView.findViewById(R.id.imageContent)
            imEditImage = itemView.findViewById(R.id.imEditImage)
            imDeleteImage = itemView.findViewById(R.id.imDelete)

            imEditImage.setOnClickListener {
                ImagePicker.getImages(context as EditAdsAct, 1)
                context.editImagePos = adapterPosition
//                PixBus.results { results ->
//                    arr2 = results.data as ArrayList<Uri>
//                    println("EditAdsAct.OnclickGetImages")
//                }
////
//                PixBus.results {arr2.add((it.data as ArrayList<Uri>)[0])
//                }
//
//
//                context.chooseImageFrag?.setSingleImage(arr2.get(0), adapterPosition)
            }

            imDeleteImage.setOnClickListener {
                adapter.mainArray.removeAt(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)
                for (n in 0 until adapter.mainArray.size) adapter.notifyItemChanged(n)
            }

            tvTitle.text = context.resources.getStringArray(R.array.title_array)[adapterPosition]
            image.setImageURI(item)
        }
    }

    fun updateAdapter(newList: List<Uri>, needClear: Boolean) {
        if (needClear == true) {mainArray.clear()}
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }


}