package com.example.photoclicker.model.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class AlbumWithPictures(
    @Embedded val album: Album,
    @Relation(
        parentColumn = "albumId",
        entityColumn = "pictureId",
        associateBy = Junction(AlbumPictureCrossRef::class)
    )
    val pictures: List<Picture>
)
