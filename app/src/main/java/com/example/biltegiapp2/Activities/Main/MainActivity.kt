package com.example.biltegiapp2.Activities.Main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Carousel
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biltegiapp2.Activities.Albaran.AlbaranActivity
import com.example.biltegiapp2.Activities.Edit.EditProductActivity
import com.example.biltegiapp2.Activities.Inventory.InventoryActivity
import com.example.biltegiapp2.Activities.RVs.rvProducts.ProductsAdapter
import com.example.biltegiapp2.Activities.RVs.rvUsers.UsersAdapter
import com.example.biltegiapp2.Activities.Profile.ProfileActivity
import com.example.biltegiapp2.DB.Datubasea
import com.example.biltegiapp2.DB.Tablak.Produktua
import com.example.biltegiapp2.DB.Tablak.Profila
import com.example.biltegiapp2.R
import com.example.biltegiapp2.databinding.ActivityMainBinding
import com.example.biltegiapp2.databinding.DialogAdminBinding
import com.example.biltegiapp2.databinding.DialogCreateProductBinding
import com.example.biltegiapp2.databinding.ItemProductBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingAddProduct: DialogCreateProductBinding
    private lateinit var bindingAdmin: DialogAdminBinding
    private lateinit var bindingItemProduct: ItemProductBinding
    private lateinit var productAdapter: ProductsAdapter
    private lateinit var btnMenu: ImageButton
    private lateinit var drawer: DrawerLayout
    private lateinit var btnAdd: Button
    private lateinit var imgProduct: ImageView
    private lateinit var rvUsers: RecyclerView
    private lateinit var rvProducts: RecyclerView
    private lateinit var btneditProduct: Button
    private lateinit var btnProfile: Button
    private lateinit var btnAlbaran: Button
    private lateinit var usersList: List<Profila>
    private lateinit var btnInventory: Button
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
        rvUsers()
        seeMenu()
        addProduct()
    }

    private fun rvUsers() {
        val DB= Datubasea(this@MainActivity)
        val dao= DB.getDAO()

        usersList= dao.getAllProfila()
        var userListEnable=mutableListOf<Profila>()
        for(user in usersList){
            if(user.gaituta){
                userListEnable.add(user)
            }

        }
        rvUsers= binding.rvUsers
        rvProducts= binding.rvProducts
        rvProducts.visibility= View.INVISIBLE
        rvUsers.layoutManager= LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = UsersAdapter(userListEnable) { user ->
            OnItemSelected(user)
        }
        rvUsers.adapter=adapter
        Log.i("ROI", usersList.size.toString())



    }

    private fun addProduct() {
        btnAdd=binding.btnAdd
        btnAdd.setOnClickListener {
            bindingAddProduct= DialogCreateProductBinding.inflate(layoutInflater)
            val builder= AlertDialog.Builder(this)
            builder.setView(bindingAddProduct.root)
            val alertDialog = builder.create()
            bindingAddProduct.btnBack.setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
            imgProduct= bindingAddProduct.imgProduct
            imgProduct.setOnClickListener {
                Log.i("IO", "thrbgvfsdcx")

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
                val intent = Intent(this, EditProductActivity::class.java)
                startActivity(intent)
            }

            btnProfile.setOnClickListener {
                seeAdminVerification(btnProfile)
            }

            btnAlbaran.setOnClickListener {
                seeAdminVerification(btnAlbaran)
            }
            btnInventory.setOnClickListener {
                seeAdminVerification(btnInventory)
            }
        }
    }

    private fun seeAdminVerification(button: Button) {
        bindingAdmin = DialogAdminBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this)
        builder.setView(bindingAdmin.root)
        val alertDialog = builder.create()

        bindingAdmin.btnBack.setOnClickListener {
            alertDialog.dismiss()
        }
        bindingAdmin.btnCheck.setOnClickListener {
            var isAdmin = false
            for (user in usersList) {
                if (user.email == bindingAdmin.txtEmail.text.toString() && user.pasahitza == bindingAdmin.txtPassword.text.toString()) {
                    isAdmin = true
                }
            }
            if (isAdmin) {
                when (button.id) {
                    R.id.btnProfile -> {
                        val intent = Intent(this, ProfileActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.btnAlbaran -> {
                        val intent = Intent(this, AlbaranActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.btnInventory -> {
                        val intent = Intent(this, InventoryActivity::class.java)
                        startActivity(intent)
                    }
                }
            } else {
                android.widget.Toast.makeText(
                    this,
                    "Email edo pasahitza okerrak",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        }
            alertDialog.show()
    }

    private fun OnItemSelected(user: Profila){
        val DB= Datubasea(this@MainActivity)
        val DAO= DB.getDAO()
       val interactionsList= DAO.getInterakzioakByProfilId(user.profilID)
        val productList= mutableListOf<Produktua>()
        interactionsList.forEach { interaction ->
            val product = DAO.getProduktuaById(interaction.prodId)
            if(product != null) {
                productList.add(product)
            }
        }
        if(productList.size>0) {
            rvProducts.visibility = View.VISIBLE
            rvProducts.layoutManager = LinearLayoutManager(this)
            val adapter = ProductsAdapter(productList) { updateProduct->
            Thread{
                DAO.updateProduktua(updateProduct)
            }.start()
            }
            rvProducts.adapter = adapter
            Log.i("ROI", productList.size.toString())
            var txtCount= binding.txtCount
            txtCount.setText(productList.size.toString())
        }else{
            rvProducts.visibility= View.INVISIBLE
            binding.txtCount.setText("0")
        }

    }


}