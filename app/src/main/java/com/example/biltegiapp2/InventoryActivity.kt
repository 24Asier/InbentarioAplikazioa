package com.example.biltegiapp2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.biltegiapp2.databinding.ActivityInventoryBinding

class InventoryActivity: InactivityPeriodActivity() {

    private lateinit var binding: ActivityInventoryBinding
    private lateinit var btnBackMain: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        backMain()
    }
    private fun backMain() {
        btnBackMain= binding.btnBackMain

        btnBackMain.setOnClickListener {
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}