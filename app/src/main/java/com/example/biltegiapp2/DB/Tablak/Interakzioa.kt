package com.example.biltegiapp2.DB.Tablak

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Interakzioa")
data class Interakzioa (
    @PrimaryKey(autoGenerate = true)
    var interId: Int = 0,
    var profilId: Int,
    var prodId: Int,
    var dataInter: String,
    var akzioa: AkzioMota
)