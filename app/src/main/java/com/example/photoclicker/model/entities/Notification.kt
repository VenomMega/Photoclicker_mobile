package com.example.photoclicker.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notification(
    @PrimaryKey(autoGenerate = true) val notificationId: Int,
    val userId: Int,
    val message: String
)
