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
import com.example.biltegiapp2.databinding.ActivityAlbaranBinding
import com.example.biltegiapp2.databinding.DialogSeeEditNewAlbaranBinding


class AlbaranActivity: InactivityPeriodActivity() {

    private lateinit var binding: ActivityAlbaranBinding
    private lateinit var bindingDialogAlbaran: DialogSeeEditNewAlbaranBinding

    private lateinit var btnAddAlbaran: Button
    private lateinit var btnBackMain: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAlbaranBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        newAlbaran()
        backMain()
    }

    private fun backMain() {
        btnBackMain= binding.btnBackMain

        btnBackMain.setOnClickListener {
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun newAlbaran() {
        btnAddAlbaran=binding.btnAdd

        btnAddAlbaran.setOnClickListener {
            bindingDialogAlbaran = DialogSeeEditNewAlbaranBinding.inflate(layoutInflater)
            val builder= AlertDialog.Builder(this)
            builder.setView(bindingDialogAlbaran.root)
            val alertDialog = builder.create()
            bindingDialogAlbaran.title.text= getString(R.string.txtnewAlbaran)
            bindingDialogAlbaran.btnBackAlbaran.setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
        }
    }
}