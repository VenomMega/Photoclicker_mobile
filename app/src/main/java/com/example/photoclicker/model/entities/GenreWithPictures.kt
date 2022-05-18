package com.example.photoclicker.model.entities

import androidx.room.Embedded
import androidx.room.Relation

data class GenreWithPictures(
    @Embedded val genre: Genre,
    @Relation(
        parentColumn = "genreName",
        entityColumn = "genreName"
    )
    val pictures: List<Picture>
)
