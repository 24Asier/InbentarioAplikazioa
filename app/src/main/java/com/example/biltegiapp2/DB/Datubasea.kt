package com.example.biltegiapp2.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.biltegiapp2.DB.Tablak.Albaran
import com.example.biltegiapp2.DB.Tablak.Interakzioa
import com.example.biltegiapp2.DB.Tablak.Produktua
import com.example.biltegiapp2.DB.Tablak.Profila

@Database (entities = [Profila::class, Albaran::class, Interakzioa::class, Produktua::class], version = 1)
@TypeConverters(DateTimeConverter::class)
abstract class Datubasea : RoomDatabase(){
    companion object{
        @Volatile
        private var instance : Datubasea? = null

        private val LOCK = Any()

        operator fun invoke (context: Context) = instance?: synchronized(LOCK){
            instance?:buildDatabase (context).also{instance = it}
        }

        private fun buildDatabase (context: Context) = Room.databaseBuilder(context,
            klass = Datubasea::class.java,
            name="myDatabase")
            .build()
    }

    abstract fun getDAO() : DAO
}