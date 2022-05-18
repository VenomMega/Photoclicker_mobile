package com.example.photoclicker.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int,
    val login: String,
    val password: String,
    val name: String,
    val surname: String,
    val authorized: Boolean
)
