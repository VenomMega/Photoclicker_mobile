package com.example.photoclicker.model.daos

import androidx.room.*
import com.example.photoclicker.model.entities.User
import com.example.photoclicker.model.entities.UserWithAlbums
import com.example.photoclicker.model.entities.UserWithNotifications
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(vararg user: User)

    @Update
    suspend fun update(user: User)

    @Query("DELETE FROM users")
    suspend fun clear()

    @Query("SELECT * FROM users")
    fun getAll(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE authorized = '1'")
    fun getAuthorized(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE login = :login AND password = :password")
    fun getUser(login: String, password: String): Flow<List<User>>

    @Transaction
    @Query("SELECT * FROM users WHERE userId = :userId")
    fun getUserWithAlbums(userId: Int): Flow<List<UserWithAlbums>>

    @Transaction
    @Query("SELECT * FROM users WHERE userId = :userId")
    fun getUserWithNotifications(userId: Int): Flow<List<UserWithNotifications>>
}