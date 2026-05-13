package com.example.biltegiapp2.DB

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.example.biltegiapp2.DB.Tablak.AkzioMota
import java.time.LocalDateTime

class DateTimeConverter {
    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?): String? {
        return date?.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun fromAkzioMota(value: AkzioMota?): String? {
        return value?.name
    }

    @TypeConverter
    fun toAkzioMota(value: String?): AkzioMota? {
        return value?.let { AkzioMota.valueOf(it) }
    }
}