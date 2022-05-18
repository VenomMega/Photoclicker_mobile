package com.example.photoclicker.model.daos

import androidx.room.*
import com.example.photoclicker.model.entities.Notification
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(picture: Notification)

    @Delete
    suspend fun delete(vararg picture: Notification)

    @Update
    suspend fun update(picture: Notification)

    @Query("DELETE FROM notification")
    suspend fun clear()

    @Query("SELECT * FROM notification")
    fun getAll(): Flow<List<Notification>>
}