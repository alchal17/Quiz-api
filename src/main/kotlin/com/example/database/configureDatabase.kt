package com.example.database

import io.github.cdimascio.dotenv.Dotenv
import org.jetbrains.exposed.sql.Database

fun configureDatabase() {
    val dotenv = Dotenv.load()
    Database.connect(
        url = dotenv["DATABASE_URL"],
        driver = dotenv["DATABASE_DRIVER"],
    )
}