package com.example.biltegiapp2.DB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.biltegiapp2.DB.Tablak.Albaran
import com.example.biltegiapp2.DB.Tablak.Interakzioa
import com.example.biltegiapp2.DB.Tablak.Produktua
import com.example.biltegiapp2.DB.Tablak.Profila

@Dao
interface DAO {
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
}