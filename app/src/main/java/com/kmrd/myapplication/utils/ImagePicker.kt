package com.kmrd.myapplication.utils

import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kmrd.myapplication.R
import com.kmrd.myapplication.act.EditAdsAct
import com.kmrd.myapplication.frag.ImageListFrag
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.models.Flash
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options
import io.ak1.pix.models.Ratio


object ImagePicker {
    lateinit var arr: List<Uri>

    fun getImages(context: AppCompatActivity, imageCounter: Int) {

        val options = Options().apply{
            ratio = Ratio.RATIO_AUTO                                    //Image/video capture ratio
            count = imageCounter                                                   //Number of images to restrict selection count
            spanCount = 4                                               //Number for columns in grid
            path = "Pix/Camera"                                         //Custom Path For media Storage
            isFrontFacing = false                                       //Front Facing camera on start
            //videoDurationLimitInSeconds = 10                            //Duration for video recording
            mode = Mode.Picture                                            //Option to select only pictures or videos or both
            flash = Flash.Auto                                         //Option to select flash type
            preSelectedUrls = ArrayList<Uri>()                          //Pre selected Image Urls
        }

        context.addPixToActivity(R.id.place_holder, options) { result ->
            when (result.status) {
                PixEventCallback.Status.SUCCESS ->  {
                    arr = result.data

                    val fList = context.supportFragmentManager.fragments
                    fList.forEach {
                        if(it.isVisible) {
                            context.supportFragmentManager.beginTransaction().remove(it).commit()
                        }
                    }


                }//use results as it.data
                //PixEventCallback.Status.BACK_PRESSED -> // back pressed called
                else -> {

                }
            }
        }




    }
}