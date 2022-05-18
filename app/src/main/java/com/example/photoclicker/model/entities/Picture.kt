package com.example.photoclicker.model.entities

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pictures")
data class Picture(
    @PrimaryKey(autoGenerate = true) val pictureId: Int,
    val name: String,
    val genreName: String,
    val authorId: Int,
    val authorName: String,
    val photo: Bitmap
)
