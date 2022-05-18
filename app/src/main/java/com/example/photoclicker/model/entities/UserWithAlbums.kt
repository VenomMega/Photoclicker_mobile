package com.example.photoclicker.model.entities

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithAlbums(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val albums: List<Album>
)
