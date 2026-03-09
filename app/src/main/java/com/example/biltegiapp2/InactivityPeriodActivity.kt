package com.example.biltegiapp2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

open class InactivityPeriodActivity : AppCompatActivity() {
    private val handler= Handler(Looper.getMainLooper())

    //2mins
    private val inactivity_time: Long= 120000

    fun gotoMain(){
        if(this !is MainActivity){
            val intent= Intent(this, MainActivity::class.java)
            intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({gotoMain()}, inactivity_time)
    }


    override fun onResume() {
        super.onResume()
        handler.postDelayed({gotoMain()}, inactivity_time)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }
}