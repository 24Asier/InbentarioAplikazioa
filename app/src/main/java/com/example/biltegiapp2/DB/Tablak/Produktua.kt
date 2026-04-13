package com.example.biltegiapp2.DB.Tablak

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
@Entity(tableName = "Produktua")
data class Produktua (
    @PrimaryKey(autoGenerate = true)
    var prodId: Int=0,
    var izena: String,
    var img: String,
    var mota: String,
    var gaituta: Boolean,
    var kantitatea:Int,
    var gutxienekoKantitatea:Int
)