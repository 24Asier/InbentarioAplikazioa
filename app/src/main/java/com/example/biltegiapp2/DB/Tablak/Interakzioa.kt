package com.example.biltegiapp2.DB.Tablak

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Interakzioa")
data class Interakzioa (
    @PrimaryKey(autoGenerate = true)
    var interId: Int,
    var profilId: Int,
    var prodId: Int,
    var dataInter: String
)
