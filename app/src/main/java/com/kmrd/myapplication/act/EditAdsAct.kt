package com.kmrd.myapplication.act

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.kmrd.myapplication.R
import com.kmrd.myapplication.databinding.ActivityEditAdsBinding
import com.kmrd.myapplication.dialogs.DialogSpinnerHelper
import com.kmrd.myapplication.frag.FragmentCloseInterface
import com.kmrd.myapplication.frag.ImageListFrag
import com.kmrd.myapplication.utils.CityHelper
import com.kmrd.myapplication.utils.ImagePicker
import io.ak1.pix.helpers.PixBus

class EditAdsAct : AppCompatActivity(), FragmentCloseInterface {
    lateinit var arr2: ArrayList<Uri>
    lateinit var rootElement: ActivityEditAdsBinding
    private val dialog = DialogSpinnerHelper()

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

    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            100 -> {
//                ImagePicker.getImages(this)
//            }
//        }
//    }

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
//        ImagePicker.getImages(this, 3)
//        PixBus.results { results -> arr2 = results.data as ArrayList<Uri> }

        rootElement.scrollViewMain.visibility = View.GONE
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.place_holder, ImageListFrag(this, arr2))
        fm.commit()
    }

    override fun onFragClose() {
        rootElement.scrollViewMain.visibility = View.VISIBLE
    }

}