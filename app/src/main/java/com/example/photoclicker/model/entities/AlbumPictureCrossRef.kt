package com.example.photoclicker.model.entities

import androidx.room.Entity

@Entity(primaryKeys = ["albumId","pictureId"])
data class AlbumPictureCrossRef(
    val albumId: Int,
    val pictureId: Int
)
