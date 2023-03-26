package com.kmrd.myapplication.frag

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kmrd.myapplication.R
import com.kmrd.myapplication.databinding.ListImageFragBinding
import com.kmrd.myapplication.utils.ImagePicker
import com.kmrd.myapplication.utils.ItemTouchMoveCallback
import io.ak1.pix.helpers.PixBus

class ImageListFrag(private val fragCloseInterface: FragmentCloseInterface, private val newList: ArrayList<Uri>): Fragment() {
    lateinit var rootElement: ListImageFragBinding
    val adapter = SelectImageRvAdapter()
    val dragCallback = ItemTouchMoveCallback(adapter)
    val touchHelper = ItemTouchHelper(dragCallback)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootElement = ListImageFragBinding.inflate(inflater)
        return rootElement.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        touchHelper.attachToRecyclerView(rootElement.rcViewSelectImage)
        rootElement.rcViewSelectImage.layoutManager = LinearLayoutManager(activity)
        rootElement.rcViewSelectImage.adapter = adapter

        adapter.updateAdapter(newList, true)

    }

    override fun onDetach() {
        super.onDetach()
        fragCloseInterface.onFragClose(adapter.mainArray)
    }

    private fun setUpToolbar() {
        rootElement.tb.inflateMenu(R.menu.menu_choose_image)

        val deleteItem = rootElement.tb.menu.findItem(R.id.id_delete_image)
        val addImageItem = rootElement.tb.menu.findItem(R.id.id_add_image)

        rootElement.tb.setNavigationOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        deleteItem.setOnMenuItemClickListener {
            adapter.updateAdapter(ArrayList(), true)
            true
        }
        addImageItem.setOnMenuItemClickListener {
            val imageCount = ImagePicker.MAX_IMAGE_COUNT - adapter.mainArray.size
            if (adapter.mainArray.size < 3) {
                ImagePicker.getImages(activity as AppCompatActivity, imageCount)
                PixBus.results { results ->
                    //Toast.makeText(activity, "What!!", Toast.LENGTH_LONG).show()
                    println("what!")

                    adapter.updateAdapter(adapter.mainArray, false)
                }
            } else {
                Toast.makeText(activity, "Maximum Images", Toast.LENGTH_LONG).show()
            }
            true
        }

    }



    fun updateAdapter(newList: ArrayList<Uri>) {
        adapter.updateAdapter(newList, false)
    }
}