package com.example.biltegiapp2.DB.Tablak

import android.icu.text.DateFormat
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Albaran")
data class Albaran (
    @PrimaryKey(autoGenerate = true)
    var albaranId: Int,
    var profilId: Int,
    var izena: String,
    var cif: String,
    var img:String,
    var kantitatea: Int,
    var data: String,
    var ordainduta: Boolean
)