package com.example.biltegiapp2.DB.Tablak

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Interakzioa", foreignKeys = [
    ForeignKey(entity = Profila::class, parentColumns = ["profilID"], childColumns = ["profilId"], onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Produktua::class, parentColumns = ["prodId"], childColumns = ["prodId"], onDelete = ForeignKey.CASCADE)
])
data class Interakzioa (
    @PrimaryKey(autoGenerate = true)
    var interId: Int=0,
    var profilId: Int,
    var prodId: Int,
    var dataInter: String
)
