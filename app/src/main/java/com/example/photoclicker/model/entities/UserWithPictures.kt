package com.example.photoclicker.model.entities

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithPictures(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "authorId"
    )
    val picture: List<Picture>
)
