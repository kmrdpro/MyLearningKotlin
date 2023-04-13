package com.kmrd.myapplication.frag

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kmrd.myapplication.R
import com.kmrd.myapplication.act.EditAdsAct
import com.kmrd.myapplication.databinding.SelectImageFragItemBinding
import com.kmrd.myapplication.utils.AdapterCallback
import com.kmrd.myapplication.utils.ImagePicker
import com.kmrd.myapplication.utils.ItemTouchMoveCallback


class SelectImageRvAdapter(val AdapterCallback: AdapterCallback): RecyclerView.Adapter<SelectImageRvAdapter.ImageHolder>(), ItemTouchMoveCallback.ItemTouchAdapter {

    val mainArray = ArrayList<Bitmap>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val viewBinding = SelectImageFragItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageHolder(viewBinding, parent.context, this)
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

    class ImageHolder(private val viewBinding: SelectImageFragItemBinding, val context: Context, val adapter: SelectImageRvAdapter) : RecyclerView.ViewHolder(viewBinding.root) {

        fun setData(bitMap: Bitmap) {
            var arr2 = arrayListOf<Uri>()

            viewBinding.imEditImage.setOnClickListener {
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

            viewBinding.imDelete.setOnClickListener {
                adapter.mainArray.removeAt(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)
                for (n in 0 until adapter.mainArray.size) adapter.notifyItemChanged(n)
                adapter.AdapterCallback.onItemDelete()
            }

            viewBinding.tvTitle.text = context.resources.getStringArray(R.array.title_array)[adapterPosition]
            viewBinding.imageContent.setImageBitmap(bitMap)
        }
    }

    fun updateAdapter(newList: List<Bitmap>, needClear: Boolean) {
        if (needClear == true) {mainArray.clear()}
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }


}