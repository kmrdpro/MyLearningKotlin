package com.kmrd.myapplication.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.kmrd.myapplication.R
import com.kmrd.myapplication.databinding.ActivityEditAdsBinding
import com.kmrd.myapplication.dialogs.DialogSpinnerHelper
import com.kmrd.myapplication.utils.CityHelper
import com.kmrd.myapplication.utils.ImagePicker

class EditAdsAct : AppCompatActivity() {
    lateinit var rootElement: ActivityEditAdsBinding
    private val dialog = DialogSpinnerHelper()
    private var isImagesPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(rootElement.root)
        init()
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
        ImagePicker.getImages(this)
    }

}