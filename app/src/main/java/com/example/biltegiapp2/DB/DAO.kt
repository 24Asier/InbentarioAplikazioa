package com.example.biltegiapp2.DB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.biltegiapp2.DB.Tablak.Albaran
import com.example.biltegiapp2.DB.Tablak.Interakzioa
import com.example.biltegiapp2.DB.Tablak.Produktua
import com.example.biltegiapp2.DB.Tablak.Profila

@Dao
interface DAO {
    @Query("SELECT * FROM Profila")
    fun getAllProfila(): List<Profila>

    @Query("SELECT * FROM Produktua")
    fun getAllProducts(): List<Produktua>
    @Insert
    fun insertProfila(profila: Profila)

    @Query("SELECT * FROM Profila WHERE profilID = :id")
    fun getProfilaById(id: Int): Profila?

    @Insert
    fun insertInterakzioa(interakzioa: Interakzioa)

    @Query("SELECT * FROM Interakzioa WHERE profilId = :profilId")
    fun getInterakzioakByProfilId(profilId: Int): List<Interakzioa>

    @Insert
    fun insertProduktua(produktua: Produktua)

    @Query("SELECT * FROM Produktua WHERE prodId = :id")
    fun getProduktuaById(id: Int): Produktua?

    @Insert
    fun insertAlbaran(albaran: Albaran)

    @Query("SELECT * FROM Albaran WHERE profilId = :profilId")
    fun getAlbaranakByProfilId(profilId: Int): List<Albaran>

    @Update
    fun updateProfila(profila:Profila)
    @Update
    fun updateProduktua(product: Produktua)
    @Update
    fun updateAlbaran(albaran: Albaran)
}