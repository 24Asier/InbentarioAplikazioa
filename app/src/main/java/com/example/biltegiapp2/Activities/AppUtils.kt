package com.example.biltegiapp2.Activities

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment

import android.widget.ImageView
import com.example.biltegiapp2.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object AppUtils {


    fun uploadImg(imageView: ImageView, imagePathOrName: String, elseImg: String) {
        val file = java.io.File(imagePathOrName)
        if (file.exists()) {
            imageView.setImageURI(android.net.Uri.fromFile(file))
        } else {
            val context = imageView.context
            val imageId =
                context.resources.getIdentifier(imagePathOrName, "drawable", context.packageName)

            if (imageId != 0) {
                imageView.setImageResource(imageId)
            } else {
                imageView.setImageResource(
                    context.resources.getIdentifier(elseImg, "drawable", context.packageName)
                )            }
        }
    }

     fun savePhoto(context: Context, bitmap: Bitmap, name:String, type: String):String{
        val dir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "BiltegiApp2")
         if (!dir.exists()) {
             dir.mkdirs()
         }
         val archiveName ="img_${name.replace(" ", "_")}_${System.currentTimeMillis()}.jpg"
         val archive= File(dir, archiveName)

        try{
            val stream =  java.io.FileOutputStream(archive)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            stream.flush()
            stream.close()
        }catch(e: Exception){
            e.printStackTrace()
            return type
        }
        return archive.absolutePath
    }

    fun todayDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun sevenDaysAgo(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun isWithinRange(dateString: String, range: String): Boolean {
        if (range.equals("guztiak", ignoreCase = true)) return true
        
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            val interactionDate = sdf.parse(dateString) ?: return false
            val interactionTime = interactionDate.time

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val todayStart = calendar.timeInMillis

            val weekAgoCalendar = Calendar.getInstance()
            weekAgoCalendar.set(Calendar.HOUR_OF_DAY, 0)
            weekAgoCalendar.set(Calendar.MINUTE, 0)
            weekAgoCalendar.set(Calendar.SECOND, 0)
            weekAgoCalendar.set(Calendar.MILLISECOND, 0)
            weekAgoCalendar.add(Calendar.DAY_OF_YEAR, -7)
            val weekAgoStart = weekAgoCalendar.timeInMillis

            val monthAgoCalendar = Calendar.getInstance()
            monthAgoCalendar.set(Calendar.HOUR_OF_DAY, 0)
            monthAgoCalendar.set(Calendar.MINUTE, 0)
            monthAgoCalendar.set(Calendar.SECOND, 0)
            monthAgoCalendar.set(Calendar.MILLISECOND, 0)
            monthAgoCalendar.add(Calendar.MONTH, -1)
            val monthAgoStart = monthAgoCalendar.timeInMillis

            when (range.lowercase(Locale.getDefault())) {
                "egun 1" -> {
                    interactionTime >= todayStart
                }
                "aste 1" -> {
                    interactionTime < todayStart && interactionTime >= weekAgoStart
                }
                "hilabete 1" -> {
                    interactionTime < weekAgoStart && interactionTime >= monthAgoStart
                }
                else -> true
            }
        } catch (e: Exception) {
            false
        }
    }
}