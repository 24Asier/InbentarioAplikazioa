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

@Database (entities = [Profila::class, Albaran::class, Interakzioa::class, Produktua::class], version = 2)
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
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration(true)
            .addCallback(DatabaseCallback())
            .build()
    }

    abstract fun getDAO() : DAO

    private class DatabaseCallback: RoomDatabase.Callback(){
        override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
            super.onCreate(db)
            db.execSQL("""
                    INSERT INTO Profila (profilID, izena, abizena, nan, admin, img, email, pasahitza, gaituta) 
                    VALUES (1, 'John', 'Doe','11111111A' , 0, 'imagen.jpg', 'john@gmail.com', 'usuario1', 1)
                """)
            db.execSQL("""
                    INSERT INTO Profila (profilID, izena, abizena, nan,  admin, img, email, pasahitza, gaituta) 
                    VALUES (2, 'Jane', 'Doe', '22222222B' , 1, 'imagen.jpg', 'jane@gmail.com', 'admin1', 1)
                """)
            db.execSQL("""
                    INSERT INTO Albaran (albaranId, profilID, izena, cif, img, kantitatea, data, ordainduta) 
                    VALUES (1, 1, 'albaran1', 'B12345678', 'albaran1.jpg', 100, '01-01-2026', 0)
                """)
            db.execSQL("""
                    INSERT INTO Albaran (albaranId, profilID, izena, cif, img, kantitatea, data, ordainduta) 
                    VALUES (2, 2, 'albaran2', 'A12345678', 'albaran2.jpg', 190, '03-01-2026', 1)
                """)
            db.execSQL("""
                    INSERT INTO Produktua (prodId, izena, img, mota, gaituta, kantitatea, gutxienekoKantitatea) 
                    VALUES (1, 'Produktua1', 'prod1.jpg', 'edaria', 1,  30, 5)
                """)
            db.execSQL("""
                    INSERT INTO Produktua (prodId, izena, img, mota, gaituta, kantitatea, gutxienekoKantitatea) 
                    VALUES (2, 'Produktua2', 'prod2.jpg', 'janaria', 1, 50, 10)
                """)
            db.execSQL("""
                    INSERT INTO Interakzioa (interId, profilId, prodId, dataInter) 
                    VALUES (1, 1, 1, '01-10-2023')
                """)
            db.execSQL("""
                    INSERT INTO Interakzioa (interId, profilId, prodId, dataInter) 
                    VALUES (2, 2, 2, '01-10-2023')
                """)
        }
    }
}