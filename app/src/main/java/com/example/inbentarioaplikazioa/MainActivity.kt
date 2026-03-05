package com.example.inbentarioaplikazioa

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.inbentarioaplikazioa.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.imgLogo?.postDelayed({

            val animazioa = AnimationUtils.loadAnimation(this, R.anim.bistaratu_logoa)
            binding.imgLogo?.startAnimation(animazioa)

            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({

                val intent = android.content.Intent(this, ListadoActivity::class.java)
                startActivity(intent)

                finish()

            }, 3000)

        }, 500)
    }

}