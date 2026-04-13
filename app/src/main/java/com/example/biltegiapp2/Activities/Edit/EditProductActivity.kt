package com.example.biltegiapp2.Activities.Edit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biltegiapp2.Activities.AppUtils
import com.example.biltegiapp2.Activities.InactivityPeriodActivity
import com.example.biltegiapp2.Activities.Main.MainActivity
import com.example.biltegiapp2.Activities.RVs.rvEditProduct.EditProductAdapter
import com.example.biltegiapp2.DB.DAO
import com.example.biltegiapp2.DB.Datubasea
import com.example.biltegiapp2.DB.Tablak.Interakzioa
import com.example.biltegiapp2.DB.Tablak.Produktua
import com.example.biltegiapp2.DB.Tablak.Profila
import com.example.biltegiapp2.R
import com.example.biltegiapp2.databinding.ActivityEditProductBinding
import com.example.biltegiapp2.databinding.DialogAlertBinding
import com.example.biltegiapp2.databinding.DialogSeeEditProductBinding
import java.util.Locale


class EditProductActivity: InactivityPeriodActivity() {
    private lateinit var binding: ActivityEditProductBinding
    private lateinit var bindingDialogSeeEditProductBinding: DialogSeeEditProductBinding
    private lateinit var bindingAlertDialog: DialogAlertBinding
    private lateinit var btnBackMain: Button
    private lateinit var rvProduct: RecyclerView
    private lateinit var productsList: List<Produktua>
    private lateinit var interakzioakList: List<Interakzioa>
    private lateinit var spinnerType: Spinner
    private lateinit var spinnerDate: Spinner
    private lateinit var dao: DAO
    private var currentUser: Profila? = null
    private var tempBitmap: Bitmap?= null
    private var currentQuery: String = ""

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            abrirCamara()
        } else {
            Toast.makeText(this, "Kameraren baimena ukatu da", Toast.LENGTH_SHORT).show()
        }
    }
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            tempBitmap = imageBitmap
            if (::bindingDialogSeeEditProductBinding.isInitialized) {
                bindingDialogSeeEditProductBinding.imgProduct.setImageBitmap(imageBitmap)
            }
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        currentUser= intent.getSerializableExtra("logged_user") as? Profila
        rvProduct=binding.rvAliments
        rvProduct.layoutManager= LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvProduct.layoutManager=androidx.recyclerview.widget.GridLayoutManager(this, 4)

        val dB= Datubasea(this)
        dao= dB.getDAO()
        productsList= dao.getAllProducts()
        interakzioakList = dao.getAllInterakzioak()
        backMain()
        spinners()
        setupSearchView()
    }
    private fun backMain() {
        btnBackMain= binding.btnBackMain

        btnBackMain.setOnClickListener {
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun spinners() {
        spinnerType = binding.spinnerType
        spinnerDate = binding.spinerDate

        val adapterType = ArrayAdapter.createFromResource(
            this,
            R.array.product_type_options,
            android.R.layout.simple_spinner_dropdown_item
        )
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.setAdapter(adapterType)

        val adapterDate = ArrayAdapter.createFromResource(
            this,
            R.array.date_filter_options,
            android.R.layout.simple_spinner_dropdown_item
        )
        adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDate.adapter = adapterDate

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                applyFilters()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spinnerType.onItemSelectedListener= listener
        spinnerDate.onItemSelectedListener = listener

    }

    private fun setupSearchView() {
        binding.searchProduct.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                currentQuery = newText.orEmpty()
                applyFilters()
                return true
            }
        })
    }

    private fun applyFilters(){
        if (!::rvProduct.isInitialized) return
        var filterList=productsList

        filterList=when(spinnerType.selectedItem.toString()){
            "Edaria" -> filterList.filter {it.mota.equals("edaria", ignoreCase = true) }
            "Janaria" -> filterList.filter { it.mota.equals("janaria", ignoreCase = true) }
            else -> filterList
        }

        val selectedDateRange = spinnerDate.selectedItem.toString()
        if (selectedDateRange.lowercase(Locale.getDefault()) != "guztiak") {
            filterList = filterList.filter { product ->
                interakzioakList.any { it.prodId == product.prodId && AppUtils.isWithinRange(it.dataInter, selectedDateRange) }
            }
        }

        if (currentQuery.isNotEmpty()) {
            filterList = filterList.filter {
                it.izena.contains(currentQuery, ignoreCase = true)
            }
        }

        rvProduct.adapter = EditProductAdapter(filterList) { product ->
            onItemSelected(product)
        }
    }
    private fun onItemSelected(product: Produktua){
        bindingDialogSeeEditProductBinding= DialogSeeEditProductBinding.inflate(layoutInflater)
        val builder= AlertDialog.Builder(this)
        builder.setView(bindingDialogSeeEditProductBinding.root)
        val alertDialog = builder.create()
        bindingDialogSeeEditProductBinding.btnBackAlbaran.setOnClickListener {
            alertDialog.dismiss()
        }

        val adapterType = ArrayAdapter.createFromResource(
            this,
            R.array.product_type_options,
            android.R.layout.simple_spinner_item
        )
        bindingDialogSeeEditProductBinding.spinnerTypeDialog.adapter = adapterType

        val spinnerPosition = adapterType.getPosition(product.mota)
        bindingDialogSeeEditProductBinding.spinnerTypeDialog.setSelection(spinnerPosition)
        bindingDialogSeeEditProductBinding.etName.setText(product.izena)
        bindingDialogSeeEditProductBinding.etQuantity.setText(product.kantitatea.toString())
        bindingDialogSeeEditProductBinding.etLessQuatity.setText(product.gutxienekoKantitatea.toString())

        val context = binding.root.context
        val imageId = context.resources.getIdentifier(product.img, "drawable", context.packageName)
        if(imageId!=0) {
            bindingDialogSeeEditProductBinding.imgProduct.setImageResource(imageId)
        }else{
            bindingDialogSeeEditProductBinding.imgProduct.setImageResource(R.drawable.aliment)
        }
        if(product.gaituta) {
            bindingDialogSeeEditProductBinding.cbPaid.isChecked=true
        }
        alertDialog.show()
        bindingDialogSeeEditProductBinding.btnSave.setOnClickListener {
            updateProduct(product, alertDialog)
        }
        bindingDialogSeeEditProductBinding.btnDelete.setOnClickListener {
            deleteProduct(product, alertDialog)
        }
        bindingDialogSeeEditProductBinding.imgProduct.setOnClickListener {
            checkCameraPermission()
        }

    }
    private fun updateProduct(product: Produktua, alertDialog: AlertDialog){

        val updateName = bindingDialogSeeEditProductBinding.etName.text.toString()
        val updateEnabled = bindingDialogSeeEditProductBinding.cbPaid.isChecked
        val updateQuantity = bindingDialogSeeEditProductBinding.etQuantity.text.toString().toIntOrNull() ?: -1
        val updateLessQuantity = bindingDialogSeeEditProductBinding.etLessQuatity.text.toString().toIntOrNull() ?: -1
        val updateType= bindingDialogSeeEditProductBinding.spinnerTypeDialog.selectedItem.toString()
        val cameraImg: String = if (tempBitmap != null) {
            AppUtils.savePhoto(this, tempBitmap!!, updateName, "aliment")
        } else {
            product.img
        }
        if(updateName.isEmpty() || updateQuantity<0 || updateLessQuantity<0 || updateType == "Guztiak") {
            Toast.makeText(this, "Bete eremu guztiak", Toast.LENGTH_SHORT).show()
        }else {
            val updateProduct = Produktua(
                prodId = product.prodId,
                izena = updateName,
                img = cameraImg,
                mota = bindingDialogSeeEditProductBinding.spinnerTypeDialog.selectedItem.toString(),
                gaituta = updateEnabled,
                kantitatea = updateQuantity,
                gutxienekoKantitatea= updateLessQuantity
            )
            val todayDate = AppUtils.todayDate()
            val newInteraction= Interakzioa(
                profilId = currentUser?.profilID ?:0,
                prodId = product.prodId,
                dataInter = todayDate
            )
            dao.updateProduktua(updateProduct)
            dao.insertInterakzioa(newInteraction)
            productsList = dao.getAllProducts()
            interakzioakList = dao.getAllInterakzioak()

            applyFilters()
            alertDialog.dismiss()
            Toast.makeText(this, "Gordeta!", Toast.LENGTH_SHORT)
                .show()

        }
    }

    private fun deleteProduct(product: Produktua, alertDialogSeeProduct: AlertDialog){
        bindingAlertDialog= DialogAlertBinding.inflate(layoutInflater)
        val builder= AlertDialog.Builder(this)
        bindingAlertDialog.tvMessage.setText(R.string.txtMessageDelete)
        bindingAlertDialog.btnYes.setText(R.string.txtYes)
        bindingAlertDialog.btnNo.setText(R.string.txtNo)
        builder.setView(bindingAlertDialog.root)
        val alertDialog = builder.create()
        bindingAlertDialog.btnNo.setOnClickListener {
            alertDialog.dismiss()
        }
        bindingAlertDialog.btnYes.setOnClickListener {
            dao.deleteProduktua(product)
            productsList= dao.getAllProducts()
            interakzioakList = dao.getAllInterakzioak()
            applyFilters()
            alertDialog.dismiss()
            alertDialogSeeProduct.dismiss()
            Toast.makeText(this, "Ezabatu da", Toast.LENGTH_SHORT).show()
        }
        alertDialog.show()

    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> {
                abrirCamara()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun abrirCamara() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startForResult.launch(intent)
    }
}