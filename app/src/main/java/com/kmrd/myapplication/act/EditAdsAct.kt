package com.kmrd.myapplication.act

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.kmrd.myapplication.R
import com.kmrd.myapplication.adapters.ImageAdapter
import com.kmrd.myapplication.databinding.ActivityEditAdsBinding
import com.kmrd.myapplication.dialogs.DialogSpinnerHelper
import com.kmrd.myapplication.frag.FragmentCloseInterface
import com.kmrd.myapplication.frag.ImageListFrag
import com.kmrd.myapplication.utils.CityHelper
import com.kmrd.myapplication.utils.ImageManager
import com.kmrd.myapplication.utils.ImagePicker
import io.ak1.pix.helpers.PixBus

class EditAdsAct : AppCompatActivity(), FragmentCloseInterface {
    var chooseImageFrag: ImageListFrag? = null
    var arr2: ArrayList<Uri> = arrayListOf()

    lateinit var rootElement: ActivityEditAdsBinding
    private val dialog = DialogSpinnerHelper()
    private lateinit var imageAdapter: ImageAdapter

    var editImagePos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(rootElement.root)
        init()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun init() {
        imageAdapter = ImageAdapter()
        rootElement.vpImages.adapter = imageAdapter
    }

    //OnClicks
    fun onClickSelectCountry(view: android.view.View) {
        val listCountry = CityHelper.getAllCountries(this)
        dialog.showSpinnerDialog(this, listCountry, rootElement.tvCountry)

        if (rootElement.tvCity.text.toString() != getString(R.string.select_city)) {
            rootElement.tvCity.text = getString(R.string.select_city)
        }
    }

    fun onClickSelectCity(view: android.view.View) {
        val selectedCountry = rootElement.tvCountry.text.toString()
        if (selectedCountry != getString(R.string.select_country)) {
            val listCity = CityHelper.getAllCities(selectedCountry, this)
            dialog.showSpinnerDialog(this, listCity, rootElement.tvCity)
        } else {
            Toast.makeText(this, "No country selected", Toast.LENGTH_LONG).show()
        }
    }

    fun onClickGetImages(view: android.view.View) {


        if (imageAdapter.mainArray.size == 0) {
            ImagePicker.getImages(this, 3)

            PixBus.results { results ->
                arr2 = results.data as ArrayList<Uri>
                println("EditAdsAct.OnclickGetImages")

                if (chooseImageFrag == null) {
                    openChooseImageFragment(arr2)
                    println("EditAdsAct.OnclickGetImages ImageList null")
                } else if (chooseImageFrag != null) {
                    chooseImageFrag?.updateAdapter(arr2)
                    println("EditAdsAct.OnclickGetImages ImageList NOT null")
                }

                val tempList = ImageManager.getImageSize(arr2[0], this)
                Log.d("MyLog", "Image width: ${tempList[0]}")
                Log.d("MyLog", "Image height: ${tempList[1]}")
            }

        } else {
            //openChooseImageFragment(imageAdapter.mainArray)
            openChooseImageFragment(null)
            chooseImageFrag?.updateAdapterFromEdit(imageAdapter.mainArray)
        }

    }

    override fun onFragClose(list: ArrayList<Bitmap>) {
        rootElement.scrollViewMain.visibility = View.VISIBLE
        imageAdapter.update(list)
        chooseImageFrag = null
    }

    fun openChooseImageFragment(newList: ArrayList<Uri>?) {
        chooseImageFrag = ImageListFrag(this, newList)
        rootElement.scrollViewMain.visibility = View.GONE
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.place_holder, chooseImageFrag!!)
        fm.commit()
    }

}