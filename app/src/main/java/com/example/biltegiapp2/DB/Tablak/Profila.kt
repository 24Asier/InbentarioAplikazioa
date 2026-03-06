package com.example.biltegiapp2.DB.Tablak

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Profila")
data class Profila (
    @PrimaryKey(autoGenerate = true)
    var profilID: Int,
    var izena: String,
    var admin: Boolean,
    var img: String,
    var email: String,
    var pasahitza: String,
    var gaituta: Boolean
)