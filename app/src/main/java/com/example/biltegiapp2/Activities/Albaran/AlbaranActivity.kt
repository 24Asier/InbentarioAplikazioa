package com.example.biltegiapp2.Activities.Albaran

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.widget.Toast
import androidx.core.content.ContextCompat
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biltegiapp2.Activities.AppUtils
import com.example.biltegiapp2.Activities.InactivityPeriodActivity
import com.example.biltegiapp2.Activities.Main.MainActivity
import com.example.biltegiapp2.Activities.RVs.rvAlbaran.AlbaranAdapter
import com.example.biltegiapp2.DB.DAO
import com.example.biltegiapp2.DB.Datubasea
import com.example.biltegiapp2.DB.Tablak.Albaran
import com.example.biltegiapp2.DB.Tablak.Profila
import com.example.biltegiapp2.R
import com.example.biltegiapp2.databinding.ActivityAlbaranBinding
import com.example.biltegiapp2.databinding.DialogSeeEditNewAlbaranBinding
import java.util.Locale

class AlbaranActivity: InactivityPeriodActivity() {

    private lateinit var binding: ActivityAlbaranBinding
    private lateinit var bindingDialogAlbaran: DialogSeeEditNewAlbaranBinding
    private lateinit var rvAlbarans: RecyclerView
    private lateinit var btnAddAlbaran: Button
    private lateinit var btnBackMain: Button
    private lateinit var imgAlbaran: ImageView
    private lateinit var dao: DAO
    private lateinit var spinnerPaid: Spinner
    private lateinit var spinnerDate: Spinner
    private lateinit var albaranList: List<Albaran>
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
            tempBitmap= imageBitmap
            bindingDialogAlbaran.imgAlbaran.setImageBitmap(imageBitmap)
        }
    }

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
        rvAlbarans=binding.rvAlbarans
        rvAlbarans.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvAlbarans.layoutManager=androidx.recyclerview.widget.GridLayoutManager(this, 4)
        val dB= Datubasea(this)
        dao= dB.getDAO()
        albaranList= dao.getAllAlbaran()
        currentUser= intent.getSerializableExtra("logged_user") as? Profila
        newAlbaran()
        backMain()
        spinners()
        setupSearchView()
    }

    private fun spinners() {
        spinnerPaid = binding.spinnerPaid
        spinnerDate = binding.spinnerDate

        val adapterPaid = ArrayAdapter.createFromResource(
            this,
            R.array.albaran_paid_options,
            android.R.layout.simple_spinner_dropdown_item
        )
        adapterPaid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPaid.adapter = adapterPaid

        val adapterDate = ArrayAdapter.createFromResource(
            this,
            R.array.date_filter_options,
            android.R.layout.simple_spinner_dropdown_item
        )
        adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDate.adapter = adapterDate

        val listener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                applyFilters()
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
        spinnerPaid.onItemSelectedListener = listener
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
        if (!::rvAlbarans.isInitialized) return
        var filterList=albaranList

        filterList=when(spinnerPaid.selectedItem.toString()){
            "Ordainduta" -> filterList.filter { it.ordainduta }
            "Ordaindu gabe" -> filterList.filter { !it.ordainduta }
            else -> filterList
        }

        val selectedDateRange = spinnerDate.selectedItem.toString()
        filterList = filterList.filter { AppUtils.isWithinRange(it.data, selectedDateRange) }

        if (currentQuery.isNotEmpty()) {
            filterList = filterList.filter {
                it.izena.contains(currentQuery, ignoreCase = true) ||
                        it.cif.contains(currentQuery, ignoreCase = true)
            }
        }

        Log.i("gfbvxd", filterList.size.toString())
        rvAlbarans.adapter = AlbaranAdapter(filterList) { albaran ->
            onItemSelected(albaran)
        }
    }
    private fun onItemSelected(albaran: Albaran){
        bindingDialogAlbaran= DialogSeeEditNewAlbaranBinding.inflate(layoutInflater)
        val builder= AlertDialog.Builder(this)
        builder.setView(bindingDialogAlbaran.root)
        val alertDialog = builder.create()
        bindingDialogAlbaran.btnBackAlbaran.setOnClickListener {
            alertDialog.dismiss()
        }
        bindingDialogAlbaran.title.setText(R.string.txtseeAlbaran)
        bindingDialogAlbaran.btnDelete.visibility= View.INVISIBLE
        bindingDialogAlbaran.etName.setText(albaran.izena)
        bindingDialogAlbaran.etCif.setText(albaran.cif)
        bindingDialogAlbaran.etQuantity.setText(albaran.kantitatea.toString())
        bindingDialogAlbaran.etDate.setText(albaran.data)
        bindingDialogAlbaran.etDate.visibility= View.VISIBLE
        bindingDialogAlbaran.etDate.isEnabled=false
        AppUtils.uploadImg(bindingDialogAlbaran.imgAlbaran, albaran.img, "albaran")
        if(albaran.ordainduta) {
            bindingDialogAlbaran.cbPaid.isChecked=true
        }
        bindingDialogAlbaran.btnSave.setOnClickListener {
            updateAlbaran(albaran, alertDialog)
        }

        bindingDialogAlbaran.btnDelete.setOnClickListener {
        }
        alertDialog.show()
        imgAlbaran= bindingDialogAlbaran.imgAlbaran
        imgAlbaran.setOnClickListener {
            checkCameraPermission()
        }
    }
    private fun updateAlbaran(albaran: Albaran, alertDialog: AlertDialog){
        val updateName = bindingDialogAlbaran.etName.text.toString()
        val updateQuantity = bindingDialogAlbaran.etQuantity.text.toString()
        val updateCif = bindingDialogAlbaran.etCif.text.toString()
        val updatePaid = bindingDialogAlbaran.cbPaid.isChecked
        val cameraImg: String = if (tempBitmap != null) {
            AppUtils.savePhoto(this, tempBitmap!!, updateName, "albaran")
        } else {
            albaran.img
        }

        if(updateName.isNotEmpty() && updateQuantity.isNotEmpty()) {
            val updateAlbaran = Albaran(
                albaranId = albaran.albaranId,
                profilId = currentUser?.profilID?:0,
                izena = updateName,
                cif = updateCif,
                img = cameraImg,
                kantitatea= updateQuantity.toInt(),
                data= AppUtils.todayDate(),
                ordainduta = updatePaid
            )
            dao.updateAlbaran(updateAlbaran)
            albaranList = dao.getAllAlbaran()
            
            applyFilters()
            alertDialog.dismiss()
            Toast.makeText(this, "Gordeta!", Toast.LENGTH_SHORT)
                .show()
        }else {
            Toast.makeText(this, "Bete eremu guztiak", Toast.LENGTH_SHORT).show()
        }
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
            bindingDialogAlbaran.imgAlbaran.setOnClickListener {
                checkCameraPermission()
            }
            bindingDialogAlbaran.btnBackAlbaran.setOnClickListener {
                alertDialog.dismiss()
            }
            bindingDialogAlbaran.btnSave.setOnClickListener {
                addAlbaran(alertDialog)
            }
            alertDialog.show()
        }
    }
    private fun addAlbaran(alertDialog: AlertDialog){

        val newName = bindingDialogAlbaran.etName.text.toString()
        val newQuantity = bindingDialogAlbaran.etQuantity.text.toString().toIntOrNull() ?: -1
        val newCif = bindingDialogAlbaran.etCif.text.toString()
        val newPaid = bindingDialogAlbaran.cbPaid.isChecked
        val cameraImg: String = if (tempBitmap != null) {
            AppUtils.savePhoto(this, tempBitmap!!, newName, "albaran")
        } else {
            "albaran"
        }
        val todayDate = AppUtils.todayDate()
        val isRepeated= AppUtils.isNotRepeatAlbaran(newName, dao)
        if(newName.isNotEmpty() && newQuantity>0 && !isRepeated) {
            val newAlbaran = Albaran(
                profilId = currentUser?.profilID?:0,
                izena = newName,
                cif = newCif,
                img = cameraImg,
                kantitatea= newQuantity,
                data= todayDate,
                ordainduta = newPaid
            )
            dao.insertAlbaran(newAlbaran)
            albaranList = dao.getAllAlbaran()
            
            applyFilters()
            alertDialog.dismiss()
            Toast.makeText(this, "Gordeta!", Toast.LENGTH_SHORT)
                .show()
        }else {
            Toast.makeText(this, "Bete eremu guztiak", Toast.LENGTH_SHORT).show()
        }
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
