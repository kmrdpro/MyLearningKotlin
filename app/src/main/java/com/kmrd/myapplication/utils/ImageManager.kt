package com.kmrd.myapplication.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.kmrd.myapplication.act.EditAdsAct
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream


object ImageManager {
    const val MAX_IMAGE_SIZE = 1000
    const val WIDTH = 0
    const val HEIGHT = 1

    fun getImageSize(uri: Uri, act: Activity): List<Int> {
        val inStream = act.contentResolver.openInputStream(uri)
        val fTemp = File(act.cacheDir, "temp.tmp")
        if (inStream != null) {
            fTemp.copyInStreamToFile(inStream)
        }
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(fTemp.path, options)
        return if (imageRotation(fTemp) == 90) listOf(options.outHeight, options.outWidth)
        else listOf(options.outWidth, options.outHeight)
    }

    private fun imageRotation(imageFile: File): Int {
        val rotation: Int
        val exif = ExifInterface(imageFile.absolutePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        rotation = if (orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            90
        } else {
            0
        }
        return rotation
    }

    fun chooseScaleType(im: ImageView, bitMap: Bitmap) {
        if (bitMap.width > bitMap.height) {
            im.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            im.scaleType = ImageView.ScaleType.CENTER_INSIDE
        }
    }

    private fun File.copyInStreamToFile(inStream: InputStream) {
        this.outputStream().use {
            out -> inStream.copyTo(out)
        }
    }

    suspend fun imageResize(uris: List<Uri>, act: Activity): List<Bitmap> = withContext(Dispatchers.IO) {
        val tempList = ArrayList<List<Int>>()
        val bitmapList = ArrayList<Bitmap>()

        for (n in uris.indices) {
            val size = getImageSize(uris[n], act)
            val imageRatio = size[WIDTH].toFloat() / size[HEIGHT].toFloat()
            if (imageRatio > 1) {
                if (size[WIDTH] > MAX_IMAGE_SIZE) {
                    tempList.add(listOf(MAX_IMAGE_SIZE, (MAX_IMAGE_SIZE/imageRatio).toInt()))
                } else {
                    tempList.add(listOf(size[WIDTH], size[HEIGHT]))
                }
            } else {
                if (size[WIDTH] > MAX_IMAGE_SIZE) {
                    tempList.add(listOf((MAX_IMAGE_SIZE*imageRatio).toInt(), MAX_IMAGE_SIZE))
                } else {
                    tempList.add(listOf(size[WIDTH], size[HEIGHT]))
                }
            }


        }

        for (i in uris.indices) {
            kotlin.runCatching {
                bitmapList.add(Picasso.get().load(uris[i]).resize(tempList[i][WIDTH], tempList[i][HEIGHT]).get())
            }

        }


        return@withContext bitmapList

    }



}