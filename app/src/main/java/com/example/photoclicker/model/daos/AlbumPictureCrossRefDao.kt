package com.example.photoclicker.model.daos

import androidx.room.*
import com.example.photoclicker.model.entities.Album
import com.example.photoclicker.model.entities.AlbumPictureCrossRef
import com.example.photoclicker.model.entities.AlbumWithPictures

@Dao
interface AlbumPictureCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(albumPicture: AlbumPictureCrossRef)

    @Delete
    suspend fun delete(albumPicture: AlbumPictureCrossRef)

    @Update
    suspend fun update(albumPicture: AlbumPictureCrossRef)

    @Query("DELETE FROM albumpicturecrossref")
    suspend fun clear()
}