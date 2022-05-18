package com.example.photoclicker.model.daos

import androidx.room.*
import com.example.photoclicker.model.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(genre: Genre)

    @Delete
    suspend fun delete(vararg genre: Genre)

    @Update
    suspend fun update(genre: Genre)

    @Query("DELETE FROM genres")
    suspend fun clear()

    @Query("SELECT * FROM genres")
    fun getAll(): Flow<List<Genre>>

    @Transaction
    @Query("SELECT * FROM genres WHERE genreName = :genreName")
    fun getGenreWithPictures(genreName: String): Flow<List<GenreWithPictures>>
}