package com.example.biltegiapp2.Activities.Profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
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
import com.example.biltegiapp2.Activities.RVs.rvUsers.UsersAdapter
import com.example.biltegiapp2.DB.DAO
import com.example.biltegiapp2.DB.Datubasea
import com.example.biltegiapp2.DB.Tablak.Profila
import com.example.biltegiapp2.databinding.ActivityProfileBinding
import com.example.biltegiapp2.databinding.DialogAddProfileBinding
import com.example.biltegiapp2.databinding.DialogSeeEditProfileBinding
import com.example.biltegiapp2.R
import com.example.biltegiapp2.databinding.DialogAlertBinding

class ProfileActivity: InactivityPeriodActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var bindingDialogProfile: DialogAddProfileBinding
    private lateinit var bindingDialogSeeProfile: DialogSeeEditProfileBinding
    private lateinit var bindingAlertDialog: DialogAlertBinding
    private lateinit var rvProfile: RecyclerView
    private lateinit var btnAdd: Button
    private lateinit var btnBackMain: Button
    private lateinit var spinnerType: Spinner
    private lateinit var spinnerEnabled: Spinner
    private lateinit var imgProfile: ImageView
    private lateinit var usersList: List<Profila>
    private lateinit var dao: DAO
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
            if(::bindingDialogSeeProfile.isInitialized) {
                bindingDialogSeeProfile.imgProfile.setImageBitmap(imageBitmap)
            }
            if(::bindingDialogProfile.isInitialized) {
                bindingDialogProfile.imgProfile.setImageBitmap(imageBitmap)
            }
        }
    }
    private var currentUser: Profila? = null
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
        rvProfile = binding.rvProfile
        rvProfile.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvProfile.layoutManager=androidx.recyclerview.widget.GridLayoutManager(this, 4)
        val dB= Datubasea(this)
        dao= dB.getDAO()

        usersList= dao.getAllProfila()
        spinners()
        setupSearchView()
        backMain()
        btnAddProfile()
    }

    private fun spinners() {
        spinnerType = binding.spinnerType

        val adapterType = ArrayAdapter.createFromResource(
            this,
            R.array.type_options,
            android.R.layout.simple_spinner_dropdown_item
        )

        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerType.setAdapter(adapterType)

        spinnerEnabled = binding.spinnerEnabled

        val adapterEnabled = ArrayAdapter.createFromResource(
            this,
            R.array.enable_options,
            android.R.layout.simple_spinner_dropdown_item
        )

        adapterEnabled.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerEnabled.setAdapter(adapterEnabled)

        val listener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                applyFilters()
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
        spinnerType.onItemSelectedListener= listener
        spinnerEnabled.onItemSelectedListener=listener
    }

    private fun setupSearchView() {
        binding.searchProfile.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        if (!::rvProfile.isInitialized) return
        var filterList=usersList

        filterList=when(spinnerType.selectedItem.toString()){
            "Administratzailea" -> filterList.filter { it.admin }
            "Langilea" -> filterList.filter { !it.admin }
            else -> filterList
        }

        filterList=when(spinnerEnabled.selectedItem.toString()){
            "Gaituta" -> filterList.filter { it.gaituta }
            "Ez gaituta" -> filterList.filter { !it.gaituta }
            else -> filterList
        }

        if (currentQuery.isNotEmpty()) {
            filterList = filterList.filter {
                it.izena.contains(currentQuery, ignoreCase = true) ||
                        it.abizena.contains(currentQuery, ignoreCase = true)
            }
        }

        rvProfile.adapter = UsersAdapter(filterList) { user ->
            onItemSelected(user)
        }
    }

    private fun backMain() {
        btnBackMain= binding.btnBackMain

        btnBackMain.setOnClickListener {
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onItemSelected(user: Profila){
        currentUser= user
        bindingDialogSeeProfile= DialogSeeEditProfileBinding.inflate(layoutInflater)
        val builder= AlertDialog.Builder(this)
        builder.setView(bindingDialogSeeProfile.root)
        val alertDialog = builder.create()
        bindingDialogSeeProfile.btnBackProfile.setOnClickListener {
            alertDialog.dismiss()
        }

        bindingDialogSeeProfile.checkAdmin.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                bindingDialogSeeProfile.adminOptions.visibility= View.VISIBLE
            }else{
                bindingDialogSeeProfile.adminOptions.visibility= View.GONE
            }
        }

        bindingDialogSeeProfile.etName.setText(user.izena)
        bindingDialogSeeProfile.etSurname.setText(user.abizena)

        AppUtils.uploadImg( bindingDialogSeeProfile.imgProfile, user.img, "outline_account_circle_24")

        if(user.gaituta) {
            bindingDialogSeeProfile.checkEnable.isChecked=true
        }
        if(user.admin){
            bindingDialogSeeProfile.checkAdmin.isChecked=true

            bindingDialogSeeProfile.etEmail.setText(user.email)
            bindingDialogSeeProfile.etPassword.setText(user.pasahitza)

        }
        bindingDialogSeeProfile.btnSave.setOnClickListener {
            updateProfile(user, alertDialog)
        }

        bindingDialogSeeProfile.btnDelete.setOnClickListener {
            deleteProfile(user, alertDialog)
        }
        alertDialog.show()
        imgProfile= bindingDialogSeeProfile.imgProfile
        imgProfile.setOnClickListener {
            checkCameraPermission()
        }

    }

    private fun btnAddProfile() {
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
            bindingDialogProfile.btnSave.setOnClickListener {
                saveProfile(alertDialog)
            }
            alertDialog.show()
            imgProfile= bindingDialogProfile.imgProfile
            imgProfile.setOnClickListener {
                checkCameraPermission()
            }
        }
    }
    private fun saveProfile(alertDialog: AlertDialog) {
        val etName = bindingDialogProfile.etName.text.toString()
        val etSurname = bindingDialogProfile.etSurname.text.toString()
        val checkAdmin = bindingDialogProfile.checkAdmin.isChecked
        val etEmail = bindingDialogProfile.etSurname.text.toString()
        val etPassword = bindingDialogProfile.etPassword.text.toString()
        val cameraImg: String = if (tempBitmap != null) {
            AppUtils.savePhoto(this, tempBitmap!!, etName, "outline_account_circle_24")
        } else {
            "outline_account_circle_24"
        }
        if ((etName.isNotEmpty() && etSurname.isNotEmpty() && !checkAdmin) || (checkAdmin && etEmail.isNotEmpty() && etPassword.isNotEmpty() && etName.isNotEmpty() && etSurname.isNotEmpty())) {
            val newProfile = Profila(
                izena = etName,
                abizena = etSurname,
                admin = checkAdmin,
                img = cameraImg,
                email = etEmail,
                pasahitza = etPassword,
                gaituta = true
            )
            tempBitmap = null
            dao.insertProfila(newProfile)
            usersList= dao.getAllProfila()
            applyFilters()
            alertDialog.dismiss()
            Toast.makeText(this, "Gordeta!", Toast.LENGTH_SHORT)
                .show()
        }else{
            Toast.makeText(this, "Bete eremu guztiak!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun updateProfile(user: Profila, alertDialog: AlertDialog){
        val updateName = bindingDialogSeeProfile.etName.text.toString()
        val updateSurname = bindingDialogSeeProfile.etSurname.text.toString()
        val updateGaituta = bindingDialogSeeProfile.checkEnable.isChecked
        val updateAdmin = bindingDialogSeeProfile.checkAdmin.isChecked
        val updateEmail = bindingDialogSeeProfile.etEmail.text.toString()
        val updatePassword = bindingDialogSeeProfile.etPassword.text.toString()
        val cameraImg: String = if (tempBitmap != null) {
            AppUtils.savePhoto(this, tempBitmap!!, updateName, "outline_account_circle_24")
        } else {
            user.img
        }

        if((updateName.isNotEmpty() && updateSurname.isNotEmpty() && !updateAdmin) || (updateName.isNotEmpty() && updateSurname.isNotEmpty() && updateAdmin && updateEmail.isNotEmpty() && updatePassword.isNotEmpty() )) {
            val updateProfile = Profila(
                profilID = user.profilID,
                izena = updateName,
                abizena = updateSurname,
                admin = updateAdmin,
                img = cameraImg,
                email = updateEmail,
                pasahitza = updatePassword,
                gaituta = updateGaituta
            )
            dao.updateProfila(updateProfile)
            usersList = dao.getAllProfila()
            applyFilters()
            alertDialog.dismiss()
            Toast.makeText(this, "Gordeta!", Toast.LENGTH_SHORT)
                .show()
        }else {
           Toast.makeText(this, "Bete eremu guztiak", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteProfile(user: Profila, alertDialogSeeProfile: AlertDialog){
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
            dao.deleteProfila(user)
            usersList= dao.getAllProfila()
            applyFilters()
            alertDialog.dismiss()
            alertDialogSeeProfile.dismiss()
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