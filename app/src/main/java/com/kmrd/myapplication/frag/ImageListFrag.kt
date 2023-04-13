package com.kmrd.myapplication.frag

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kmrd.myapplication.R
import com.kmrd.myapplication.act.EditAdsAct
import com.kmrd.myapplication.databinding.ListImageFragBinding
import com.kmrd.myapplication.dialoghelper.ProgressDialog
import com.kmrd.myapplication.utils.AdapterCallback
import com.kmrd.myapplication.utils.ImageManager
import com.kmrd.myapplication.utils.ImagePicker
import com.kmrd.myapplication.utils.ItemTouchMoveCallback
import io.ak1.pix.helpers.PixBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ImageListFrag(private val fragCloseInterface: FragmentCloseInterface, private val newList: ArrayList<Uri>?): Fragment(), AdapterCallback {
    lateinit var rootElement: ListImageFragBinding
    val adapter = SelectImageRvAdapter(this)
    val dragCallback = ItemTouchMoveCallback(adapter)
    val touchHelper = ItemTouchHelper(dragCallback)
    private var job: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootElement = ListImageFragBinding.inflate(inflater)
        return rootElement.root
    }

    override fun onItemDelete() {

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        touchHelper.attachToRecyclerView(rootElement.rcViewSelectImage)
        rootElement.rcViewSelectImage.layoutManager = LinearLayoutManager(activity)
        rootElement.rcViewSelectImage.adapter = adapter

        if (newList != null) {
            resizeSelectedImages(newList, true)
        }

    }

    fun updateAdapterFromEdit(bitmapList: List<Bitmap>) {
        adapter.updateAdapter(bitmapList, true)
    }

    override fun onDetach() {
        super.onDetach()
        fragCloseInterface.onFragClose(adapter.mainArray)
        job?.cancel()
    }

    private fun resizeSelectedImages(newList: ArrayList<Uri>, needClear: Boolean) {
        job = CoroutineScope(Dispatchers.Main).launch {
            val dialog = ProgressDialog.createProgressDialog(activity as Activity)
            val bitmapList = ImageManager.imageResize(newList, activity as Activity)
            dialog.dismiss()
            adapter.updateAdapter(bitmapList, needClear)
        }
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
                    println("ImageListFrag.addItem")


                    adapter.updateAdapter(adapter.mainArray, false)
                }
            } else {
                Toast.makeText(activity, "Maximum Images", Toast.LENGTH_LONG).show()
            }
            true
        }

    }

    fun updateAdapter(newList: ArrayList<Uri>) {
        resizeSelectedImages(newList, false)
    }

    fun setSingleImage(uri: Uri, pos: Int) {
        val pBar = rootElement.rcViewSelectImage[pos].findViewById<ProgressBar>(R.id.pBar)
        job = CoroutineScope(Dispatchers.Main).launch {
            pBar.visibility = View.VISIBLE
            val bitmapList = ImageManager.imageResize(listOf(uri), activity as Activity)
            pBar.visibility = View.GONE
            adapter.mainArray[pos] = bitmapList[0]
            adapter.notifyItemChanged(pos)
        }


    }
}