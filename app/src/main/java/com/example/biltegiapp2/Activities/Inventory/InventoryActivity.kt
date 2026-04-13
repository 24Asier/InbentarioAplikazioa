package com.example.biltegiapp2.Activities.Inventory

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.biltegiapp2.Activities.InactivityPeriodActivity
import com.example.biltegiapp2.Activities.Main.MainActivity
import com.example.biltegiapp2.Activities.RVs.rvProducts.InventoryProductsAdapter
import com.example.biltegiapp2.DB.Datubasea
import com.example.biltegiapp2.DB.Tablak.Produktua
import com.example.biltegiapp2.R
import com.example.biltegiapp2.databinding.ActivityInventoryBinding

class InventoryActivity: InactivityPeriodActivity() {

    private lateinit var binding: ActivityInventoryBinding
    private lateinit var btnBackMain: Button
    private var allProducts: List<Produktua> = emptyList()
    private var displayedProducts: MutableList<Produktua> = mutableListOf()
    private var currentQuery: String = ""

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
        
        loadData()
        setupRecyclerView()
        setupSpinners()
        setupSearchView()
        backMain()
    }

    private fun loadData() {
        val db = Datubasea(this)
        val dao = db.getDAO()
        allProducts = dao.getAllProducts() //jarvis dame todos los productos de la base de datos
        displayedProducts = allProducts.toMutableList()
    }

    private fun setupRecyclerView() {
        val rvInventario = binding.rvInventory
        rvInventario.layoutManager = GridLayoutManager(this, 2)
        updateAdapter()
    }

    private fun updateAdapter() {
        binding.rvInventory.adapter = InventoryProductsAdapter(displayedProducts)
    }

    private fun setupSpinners() {
        // Spinner para Tipos de producto
        val typeAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.product_type_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerType.adapter = typeAdapter

        // Spinner para Ordenación (reusando el ID spinerDate del XML)
        val sortAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.inventory_sort_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinerDate.adapter = sortAdapter

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                applyFilterAndSort()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerType.onItemSelectedListener = listener
        binding.spinerDate.onItemSelectedListener = listener
    }

    private fun setupSearchView() {
        binding.searchProduct.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                currentQuery = newText.orEmpty()
                applyFilterAndSort()
                return true
            }
        })
    }

    private fun applyFilterAndSort() {
        val selectedType = binding.spinnerType.selectedItem.toString()
        val selectedSort = binding.spinerDate.selectedItem.toString()

        // Filtra por tipo de producto
        var filteredList = if (selectedType == "Guztiak") {
            allProducts
        } else {
            allProducts.filter { it.mota == selectedType }
        }

        // Filtra por búsqueda
        if (currentQuery.isNotEmpty()) {
            filteredList = filteredList.filter { 
                it.izena.contains(currentQuery, ignoreCase = true) 
            }
        }

        // Ordena la lista
        filteredList = when (selectedSort) {
            "A-Z" -> filteredList.sortedBy { it.izena.lowercase() }
            "Z-A" -> filteredList.sortedByDescending { it.izena.lowercase() }
            "Kantitate gehienez gutxienez" -> filteredList.sortedByDescending { it.kantitatea }
            "Kantitate gutxienez gehienez" -> filteredList.sortedBy { it.kantitatea }
            else -> filteredList
        }

        displayedProducts.clear()
        displayedProducts.addAll(filteredList)
        updateAdapter()
    }

    private fun backMain() {
        btnBackMain = binding.btnBackMain
        btnBackMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
