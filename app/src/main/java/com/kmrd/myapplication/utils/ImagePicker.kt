package com.kmrd.myapplication.utils

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.kmrd.myapplication.MainActivity
import com.kmrd.myapplication.act.EditAdsAct
import io.ak1.pix.helpers.pixFragment
import io.ak1.pix.models.Flash
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options
import io.ak1.pix.models.Ratio


object ImagePicker {
    const val REQUEST_CODE_GET_IMAGES = 999

    fun getImages(context: AppCompatActivity) {
        val options = Options().apply{
            ratio = Ratio.RATIO_AUTO                                    //Image/video capture ratio
            count = 3                                                   //Number of images to restrict selection count
            spanCount = 4                                               //Number for columns in grid
            path = "Pix/Camera"                                         //Custom Path For media Storage
            isFrontFacing = false                                       //Front Facing camera on start
            //videoDurationLimitInSeconds = 10                            //Duration for video recording
            mode = Mode.Picture                                            //Option to select only pictures or videos or both
            flash = Flash.Auto                                         //Option to select flash type
            preSelectedUrls = ArrayList<Uri>()                          //Pre selected Image Urls
        }

        val pixFragment = pixFragment(options)
        




    }
}