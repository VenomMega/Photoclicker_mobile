package com.example.photoclicker.model.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class Album(
    @PrimaryKey(autoGenerate = true) val albumId: Int,
    val name: String,
    val userId: Int
)
