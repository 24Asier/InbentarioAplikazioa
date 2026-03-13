package com.example.biltegiapp2.Activities.Profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biltegiapp2.Activities.InactivityPeriodActivity
import com.example.biltegiapp2.Activities.Main.MainActivity
import com.example.biltegiapp2.Activities.RVs.rvUsers.UsersAdapter
import com.example.biltegiapp2.DB.Datubasea
import com.example.biltegiapp2.DB.Datubasea.Companion.invoke
import com.example.biltegiapp2.DB.Tablak.Profila
import com.example.biltegiapp2.databinding.ActivityProfileBinding
import com.example.biltegiapp2.databinding.DialogAddProfileBinding
import com.example.biltegiapp2.databinding.DialogSeeEditProfileBinding
import com.example.biltegiapp2.R
class ProfileActivity: InactivityPeriodActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var bindingDialogProfile: DialogAddProfileBinding
    private lateinit var bindingDialogSeeProfile: DialogSeeEditProfileBinding
    private lateinit var rvProfile: RecyclerView
    private lateinit var btnAdd: Button
    private lateinit var btnBackMain: Button
    private lateinit var spinnerType: Spinner
    private lateinit var spinnerEnabled: Spinner
    private lateinit var imgProfile: ImageView
    private lateinit var usersList: List<Profila>
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

        val DB= Datubasea(this)
        val dao= DB.getDAO()

        usersList= dao.getAllProfila()
        spinners()
        backMain()
        addProfile()
    }

    private fun spinners() {
        spinnerType = binding.spinnerType

        val adapterType = ArrayAdapter.createFromResource(
            this,
            R.array.type_options,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        )

        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerType.setAdapter(adapterType)

        spinnerEnabled = binding.spinnerEnabled

        val adapterEnabled = ArrayAdapter.createFromResource(
            this,
            R.array.enable_options,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
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
        rvProfile.adapter = UsersAdapter(filterList) { user ->
            OnItemSelected(user)
        }
    }

    private fun backMain() {
        btnBackMain= binding.btnBackMain

        btnBackMain.setOnClickListener {
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun OnItemSelected(user: Profila){
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

        bindingDialogSeeProfile.etName.setText(user.izena.toString())
        bindingDialogSeeProfile.etSurname.setText(user.abizena.toString())
        val context = binding.root.context
        val imageId = context.resources.getIdentifier(user.img, "drawable", context.packageName)
        if(imageId!=0) {
            bindingDialogSeeProfile.imgProfile.setImageResource(imageId)
        }else{
            bindingDialogSeeProfile.imgProfile.setImageResource(R.drawable.outline_account_circle_24)
        }
        if(user.gaituta) {
            bindingDialogSeeProfile.checkEnable.isChecked=true
        }
        if(user.admin == true){
            bindingDialogSeeProfile.checkAdmin.isChecked=true

            bindingDialogSeeProfile.etEmail.setText(user.email.toString())
            bindingDialogSeeProfile.etPassword.setText(user.pasahitza.toString())

        }

        alertDialog.show()
        imgProfile= bindingDialogSeeProfile.imgProfile
        imgProfile.setOnClickListener {
            Log.i("IO", "thrbgvfsdcx")

        }
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


}