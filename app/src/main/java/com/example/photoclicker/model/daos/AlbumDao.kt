package com.example.photoclicker.model.daos

import androidx.room.*
import com.example.photoclicker.model.entities.Album
import com.example.photoclicker.model.entities.AlbumWithPictures
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(album: Album)

    @Delete
    suspend fun delete(vararg album: Album)

    @Update
    suspend fun update(album: Album)

    @Query("DELETE FROM albums")
    suspend fun clear()

    @Query("SELECT * FROM albums")
    fun getAll(): Flow<List<Album>>

    @Query("DELETE FROM albums WHERE albumId = :albumId")
    suspend fun deleteAlbumWithId(albumId: Int)

    @Transaction
    @Query("SELECT * FROM albums WHERE albumId = :albumId")
    fun getAlbumWithPictures(albumId: Int): Flow<List<AlbumWithPictures>>
}