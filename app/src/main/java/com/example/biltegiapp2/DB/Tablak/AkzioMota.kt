package com.example.biltegiapp2.DB.Tablak

enum class AkzioMota(val deskripzioa: String) {
    CREATE("Produktua sortu da"),
    UPDATE("Produktua eguneratu da"),
    DELETE("Produktua ezabatu da"),
    ADD_QUANTITY("Kantitatea gehitu da"),
    REMOVE_QUANTITY("Kantitatea kendu da")
}
