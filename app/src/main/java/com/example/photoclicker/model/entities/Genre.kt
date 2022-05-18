package com.example.photoclicker.model.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres")
data class Genre(
    @PrimaryKey() val genreName: String,
    val genreImg: Bitmap
)
