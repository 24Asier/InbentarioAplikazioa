package com.example.biltegiapp2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View

import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog

import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.example.biltegiapp2.databinding.ActivityProfileBinding
import com.example.biltegiapp2.databinding.DialogAddProfileBinding
import com.example.biltegiapp2.databinding.DialogSeeEditProfileBinding

class ProfileActivity: InactivityPeriodActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var bindingDialogProfile: DialogAddProfileBinding
    private lateinit var bindingDialogSeeProfile: DialogSeeEditProfileBinding

    private lateinit var btnAdd: Button
    private lateinit var btnBackMain: Button
    private lateinit var imgProfile: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        addProfile()
        backMain()
    }

    private fun addProfile() {
        btnAdd= binding.btnAdd
        btnAdd.setOnClickListener {
            bindingDialogProfile= DialogAddProfileBinding.inflate(layoutInflater)
            val builder= AlertDialog.Builder(this)
            builder.setView(bindingDialogProfile.root)
            val alertDialog = builder.create()
            bindingDialogProfile.btnBackProfile.setOnClickListener {
                alertDialog.dismiss()
            }

            bindingDialogProfile.checkAdmin.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    bindingDialogProfile.adminOptions.visibility= View.VISIBLE
                }else{
                    bindingDialogProfile.adminOptions.visibility= View.GONE
                }
            }
            alertDialog.show()
            imgProfile= bindingDialogProfile.imgProfile
            imgProfile.setOnClickListener {
                Log.i("IO", "thrbgvfsdcx")

            }
        }
    }

    private fun seeProfile(){
        bindingDialogSeeProfile= DialogSeeEditProfileBinding.inflate(layoutInflater)
        val builder= AlertDialog.Builder(this)
        builder.setView(bindingDialogSeeProfile.root)
        val alertDialog = builder.create()
        bindingDialogSeeProfile.btnBackProfile.setOnClickListener {
            alertDialog.dismiss()
        }
    }
    private fun backMain() {
        btnBackMain= binding.btnBackMain

        btnBackMain.setOnClickListener {
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}