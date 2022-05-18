package com.example.photoclicker.model.daos

import androidx.room.*
import com.example.photoclicker.model.entities.Picture
import kotlinx.coroutines.flow.Flow

@Dao
interface PictureDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(picture: Picture)

    @Delete
    suspend fun delete(vararg picture: Picture)

    @Update
    suspend fun update(picture: Picture)

    @Query("DELETE FROM pictures")
    suspend fun clear()

    @Query("SELECT * FROM pictures")
    fun getAll(): Flow<List<Picture>>
}