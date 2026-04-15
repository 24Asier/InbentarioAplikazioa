package com.example.biltegiapp2.Activities.Main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biltegiapp2.Activities.Albaran.AlbaranActivity
import com.example.biltegiapp2.Activities.AppUtils
import com.example.biltegiapp2.Activities.Edit.EditProductActivity
import com.example.biltegiapp2.Activities.Inventory.InventoryActivity
import com.example.biltegiapp2.Activities.RVs.rvProducts.ProductsAdapter
import com.example.biltegiapp2.Activities.RVs.rvUsers.UsersAdapter
import com.example.biltegiapp2.Activities.Profile.ProfileActivity
import com.example.biltegiapp2.DB.Datubasea
import com.example.biltegiapp2.DB.Tablak.Interakzioa
import com.example.biltegiapp2.DB.Tablak.Produktua
import com.example.biltegiapp2.DB.Tablak.Profila
import com.example.biltegiapp2.R
import com.example.biltegiapp2.databinding.ActivityMainBinding
import com.example.biltegiapp2.databinding.DialogAdminBinding
import com.example.biltegiapp2.databinding.DialogAlertBinding
import com.example.biltegiapp2.databinding.DialogCreateProductBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingAddProduct: DialogCreateProductBinding
    private lateinit var bindingAdmin: DialogAdminBinding
    private lateinit var bindingAlert: DialogAlertBinding
    private lateinit var btnMenu: ImageButton
    private lateinit var drawer: DrawerLayout
    private lateinit var imgProduct: ImageView
    private lateinit var rvUsers: RecyclerView
    private lateinit var rvProducts: RecyclerView
    private lateinit var btneditProduct: Button
    private lateinit var btnProfile: Button
    private lateinit var btnAlbaran: Button
    private lateinit var usersList: List<Profila>
    private lateinit var btnInventory: Button
    private lateinit var spinnerQuantity: Spinner
    private lateinit var spinnerDate: Spinner
    private lateinit var spinnerType: Spinner
    private  var selectedUser: Profila?= null
    private  var productList= mutableListOf<Produktua>()
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
            imgProduct.setImageBitmap(imageBitmap)
        }
    }


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
        bindingAddProduct= DialogCreateProductBinding.inflate(layoutInflater)
        spinnerQuantity = binding.filterQuantity
        spinnerDate = binding.filterDate
        spinnerType= bindingAddProduct.spinnerType
        
        spinners()
        rvUsers()
        seeMenu()
        setupSearchView()

    }


    private fun rvUsers() {
        val dB= Datubasea(this@MainActivity)
        val dao= dB.getDAO()

        usersList= dao.getAllProfila()
        val userListEnable=mutableListOf<Profila>()
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
            onItemSelected(user)
        }
        rvUsers.adapter=adapter
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

    private fun addProduct(user: Profila) {
        binding.btnAdd.setOnClickListener {
            val builder= AlertDialog.Builder(this)
            builder.setView(bindingAddProduct.root)
            val alertDialog = builder.create()


            bindingAddProduct.btnBack.setOnClickListener {
                alertDialog.dismiss()
            }
            bindingAddProduct.btnSave.setOnClickListener {
                saveProduct(user, spinnerType, alertDialog)
            }
            alertDialog.show()
            imgProduct= bindingAddProduct.imgProduct
            imgProduct.setOnClickListener {
                checkCameraPermission()

            }
        }
    }
    private fun saveProduct(user: Profila, spinnerType: Spinner, alertDialog: AlertDialog){
        val etName= bindingAddProduct.etName.text.toString()
        val etQuantity= bindingAddProduct.etQuantity.text.toString().toIntOrNull() ?: -1
        val etLessQuantity= bindingAddProduct.etLessQuatity.text.toString().toIntOrNull() ?: -1
        val spinnerTypeValue= spinnerType.selectedItem.toString()
        val cameraImg: String = if (tempBitmap != null) {
            AppUtils.savePhoto(this, tempBitmap!!, etName, "aliment")
        } else {
            "aliment"
        }
        if(etName.isEmpty() || etQuantity<0 || etLessQuantity<0 || spinnerTypeValue == "Guztiak" ){
            Toast.makeText(
                this,
                "Datu guztiak sartu behar dira",
                Toast.LENGTH_SHORT
            ).show()

        }else{
            val dB = Datubasea(this@MainActivity)
            val dao = dB.getDAO()
            val todayDate = AppUtils.todayDate()

            Thread {
                val product = Produktua(
                    izena = etName,
                    img = cameraImg,
                    mota = spinnerTypeValue,
                    gaituta = true,
                    kantitatea = etQuantity,
                    gutxienekoKantitatea = etLessQuantity
                )
                val newProdId = dao.insertProduktua(product)
                val interaction = Interakzioa(
                    profilId = user.profilID,
                    prodId = newProdId.toInt(),
                    dataInter = todayDate
                )
                dao.insertInterakzioa(interaction)
                runOnUiThread {
                    tempBitmap = null
                    alertDialog.dismiss()
                    onItemSelected(user)
                }
            }.start()
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
                if(selectedUser != null ) {
                    val intent = Intent(this, EditProductActivity::class.java)
                    intent.putExtra("logged_user", selectedUser)
                    startActivity(intent)
                }else{
                    bindingAlert= DialogAlertBinding.inflate(layoutInflater)
                    val builder = AlertDialog.Builder(this)
                    builder.setView(bindingAlert.root)
                    val alertDialog = builder.create()
                    bindingAlert.tvMessage.setText(R.string.txtMessageAlert)
                    bindingAlert.btnYes.visibility= View.GONE
                    bindingAlert.btnNo.visibility= View.GONE
                    alertDialog.show()
                }
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
                        intent.putExtra("logged_user", selectedUser)
                        startActivity(intent)
                    }

                    R.id.btnInventory -> {
                        val intent = Intent(this, InventoryActivity::class.java)
                        startActivity(intent)
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    "Email edo pasahitza okerrak",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
            alertDialog.show()
    }

    private fun spinners(){
        val adapterQuantity = ArrayAdapter.createFromResource(
            this,
            R.array.products_quantity_options,
            android.R.layout.simple_spinner_dropdown_item
        )
        adapterQuantity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerQuantity.adapter = adapterQuantity

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
        spinnerQuantity.onItemSelectedListener= listener
        spinnerDate.onItemSelectedListener = listener

        val adapterProductType = ArrayAdapter.createFromResource(
            this,
            R.array.product_type_options,
            android.R.layout.simple_spinner_dropdown_item
        )
        adapterProductType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.adapter = adapterProductType
    }

    private fun applyFilters() {
        if (!::rvProducts.isInitialized || selectedUser == null) return
        val dB = Datubasea(this@MainActivity)
        val dao = dB.getDAO()

        var filterList: List<Produktua> = productList.toList()

        filterList = when (spinnerQuantity.selectedItem.toString()) {
            "Gutxieneko kantitatea baino gehiago" -> filterList.filter { it.kantitatea > it.gutxienekoKantitatea }
            "Gutxieneko kantitatea baino gutxiago" -> filterList.filter { it.kantitatea <= it.gutxienekoKantitatea }
            else -> filterList
        }

        val selectedDateRange = spinnerDate.selectedItem.toString()
        if (selectedDateRange.lowercase(Locale.getDefault()) != "guztiak") {
            val dateFiltered = mutableListOf<Produktua>()
            val today = AppUtils.todayDate()
            val sevenDaysAgo = AppUtils.sevenDaysAgo()

            for (product in filterList) {
                val interactions = dao.getInteractionsForProduct(product.prodId)
                var addProduct = false
                for (interaction in interactions) {
                    if (interaction.profilId == selectedUser?.profilID) {
                        when (selectedDateRange) {
                            "Egun 1" -> if (interaction.dataInter == today) addProduct = true
                            "Aste 1" -> if (interaction.dataInter >= sevenDaysAgo && interaction.dataInter != today) addProduct = true
                            "Hilabete 1" -> if (AppUtils.isWithinRange(interaction.dataInter, selectedDateRange)) addProduct = true
                        }
                    }
                }
                if (addProduct) {
                    dateFiltered.add(product)
                }
            }
            filterList = dateFiltered
        }

        updateAdapter(filterList)
    }

    private fun updateAdapter(filterList: List<Produktua>) {
        val finalList = if (currentQuery.isNotEmpty()) {
            filterList.filter { it.izena.contains(currentQuery, ignoreCase = true) }    } else {
            filterList
        }

        rvProducts.layoutManager = GridLayoutManager(this, 3)
        rvProducts.adapter = ProductsAdapter(finalList) { updateProduct ->
            val dB = Datubasea(this)
            val dAO = dB.getDAO()
            Thread {
                dAO.updateProduktua(updateProduct)
                selectedUser?.let { user ->
                    val interaction = Interakzioa(
                        profilId = user.profilID,
                        prodId = updateProduct.prodId,
                        dataInter = AppUtils.todayDate()
                    )
                    dAO.insertInterakzioa(interaction)

                    runOnUiThread {
                        loadProducts()
                    }
                }
            }.start()
        }
        rvProducts.visibility = View.VISIBLE
    }

    private fun onItemSelected(user: Profila) {
        selectedUser = user
        rvProducts.visibility = View.VISIBLE
        binding.filterQuantity.visibility = View.VISIBLE
        binding.filterDate.visibility = View.VISIBLE
        binding.btnAdd.visibility = View.VISIBLE
        addProduct(user)
        seeMenu()
        loadProducts()
    }

    private fun loadProducts() {
        val user = selectedUser ?: return
        val dB = Datubasea(this@MainActivity)
        val dAO = dB.getDAO()

        Thread {
            val allProducts = dAO.getAllProducts().filter { it.gaituta }
            val interactions = dAO.getInterakzioakByProfilId(user.profilID)

            // Usar set de IDs interactuados para búsqueda eficiente
            val interactedIds = interactions.map { it.prodId }.toSet()

            // Ordenar por interacciones recientes pero asegurando que TODOS los productos aparezcan
            val baseList = allProducts.sortedByDescending { product ->
                interactions.filter { it.prodId == product.prodId }.maxOfOrNull { it.dataInter } ?: ""
            }

            val withLittleStock = baseList.filter { it.kantitatea <= it.gutxienekoKantitatea }
            val withNormalStock = baseList.filter { it.kantitatea > it.gutxienekoKantitatea }
            val finalList= (withLittleStock + withNormalStock).toMutableList()

            runOnUiThread {
                productList.clear()
                productList.addAll(finalList)
                applyFilters()
                binding.txtCount.text = productList.size.toString()
            }
        }.start()
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