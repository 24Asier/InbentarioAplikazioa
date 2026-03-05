package com.example.biltegiapp2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.Image
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ZoomButtonsController
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.biltegiapp2.databinding.ActivityMainBinding
import com.example.biltegiapp2.databinding.DialogCreateProductBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var btnMenu: ImageButton
    private lateinit var drawer: DrawerLayout
    private lateinit var btnAdd: Button
    private lateinit var bindingAddProduct: DialogCreateProductBinding
    private lateinit var imgProduct: ImageView
    private lateinit var btneditProduct: Button
    private lateinit var btnProfile: Button
    private lateinit var btnAlbaran: Button
    private lateinit var btnInventory:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        seeMenu()
        addProduct()
    }

    private fun addProduct() {
        btnAdd=binding.btnAdd
        btnAdd.setOnClickListener {
            bindingAddProduct= DialogCreateProductBinding.inflate(layoutInflater)
            val builder=androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setView(bindingAddProduct.root)
            val alertDialog = builder.create()
            bindingAddProduct.btnBack.setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
            imgProduct= bindingAddProduct.imgProduct
            imgProduct.setOnClickListener {
                Log.i("IO", "thrbgvfsdcx")
                checkPermissionAndCamera()
            }
        }
    }


    private fun seeMenu() {
        btnMenu=binding.btnMenu
        drawer=binding.drawerLayout
        btnMenu.setOnClickListener {
            drawer.openDrawer(GravityCompat.END)
            btneditProduct=binding.btneditProduct
            btnProfile=binding.btnProfile
            btnAlbaran=binding.btnAlbaran
            btnInventory=binding.btnInventory

            btneditProduct.setOnClickListener {

            }

            btnProfile.setOnClickListener {

            }

            btnAlbaran.setOnClickListener {

            }
            btnInventory.setOnClickListener {

            }
        }
    }
    private fun checkPermissionAndCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
        } else {
            camera()
        }
    }
    private fun camera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent,1)
        }
    }
    override fun onRequestPermissionsResult(requestCode:Int, permissions: Array<out String>, grantResults: IntArray){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            camera()
        }
    }
}




