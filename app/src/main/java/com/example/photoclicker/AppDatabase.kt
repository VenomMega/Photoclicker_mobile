package com.example.photoclicker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.photoclicker.converters.BitmapConverter
import com.example.photoclicker.model.daos.*
import com.example.photoclicker.model.entities.*

@Database(entities = [User::class, Album::class, Genre::class, Picture::class, AlbumPictureCrossRef::class, Notification::class], version = 6, exportSchema = true)
@TypeConverters(BitmapConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun albumDao(): AlbumDao
    abstract fun genreDao(): GenreDao
    abstract fun pictureDao(): PictureDao
    abstract fun albumPictureCrossRefDao(): AlbumPictureCrossRefDao
    abstract fun notificationDao(): NotificationDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                instance
            }
        }
    }
}