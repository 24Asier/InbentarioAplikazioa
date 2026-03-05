package com.example.biltegiapp2

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.biltegiapp2.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        seeAnimation()
    }

    private fun seeAnimation() {
        binding.imgLogo.postDelayed({
            val animazioa = AnimationUtils.loadAnimation(this, R.anim.bistaratu_logoa)
            binding.imgLogo.startAnimation(animazioa)

            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        }, 500)
    }
}