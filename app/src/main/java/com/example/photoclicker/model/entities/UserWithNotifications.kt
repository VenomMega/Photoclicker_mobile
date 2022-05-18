package com.example.photoclicker.model.entities

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithNotifications(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val notifications: List<Notification>
)
