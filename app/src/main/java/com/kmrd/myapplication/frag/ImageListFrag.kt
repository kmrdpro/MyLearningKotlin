package com.kmrd.myapplication.frag

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kmrd.myapplication.R
import com.kmrd.myapplication.utils.ItemTouchMoveCallback

class ImageListFrag(private val fragCloseInterface: FragmentCloseInterface, private val newList: ArrayList<Uri>): Fragment() {
    val adapter = SelectImageRvAdapter()
    val dragCallback = ItemTouchMoveCallback()
    val touchHelper = ItemTouchHelper(dragCallback)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_image_frag, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bBack = view.findViewById<Button>(R.id.bBack)
        val rcView = view.findViewById<RecyclerView>(R.id.rcViewSelectImage)
        touchHelper.attachToRecyclerView(rcView)
        rcView.layoutManager = LinearLayoutManager(activity)
        rcView.adapter = adapter

        val updateList = ArrayList<SelectImageItem>()
        for (n in 0 until newList.size) {
            updateList.add(SelectImageItem(n.toString(), newList[n]))
        }
        adapter.updateAdapter(updateList)

        bBack.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

    override fun onDetach() {
        super.onDetach()
        fragCloseInterface.onFragClose()
    }
}