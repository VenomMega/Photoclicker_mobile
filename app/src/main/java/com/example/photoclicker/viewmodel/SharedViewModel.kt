package com.example.photoclicker.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.photoclicker.model.daos.*
import com.example.photoclicker.model.entities.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class SharedViewModel(private val albumDao: AlbumDao,
                      private val albumPictureCrossRefDao: AlbumPictureCrossRefDao,
                      private val notificationDao: NotificationDao,
                      private val genreDao: GenreDao,
                      private val pictureDao: PictureDao,
                      private val userDao: UserDao): ViewModel() {

    var user = userDao.getAuthorized().asLiveData()
    var users = userDao.getAll().asLiveData()
    var genres = genreDao.getAll().asLiveData()
    var pictures = pictureDao.getAll().asLiveData()

    var userAlbums = userDao.getUserWithAlbums(0).asLiveData()
    var albumPictures = albumDao.getAlbumWithPictures(0).asLiveData()
    var userNotifications = userDao.getUserWithNotifications(0).asLiveData()
    var genrePictures = genreDao.getGenreWithPictures("").asLiveData()

    var pictureId = 0
    var authorId = 0
    var albumId = 0
    var genreName = ""

    fun addAlbumPictureCross(albumPictureCrossRef: AlbumPictureCrossRef){
        viewModelScope.launch {
            albumPictureCrossRefDao.insert(albumPictureCrossRef)
        }
    }

    fun addNotification(notification: Notification){
        viewModelScope.launch {
            notificationDao.insert(notification)
        }
    }

    fun setGenrePictures(){
        genrePictures = genreDao.getGenreWithPictures(genreName).asLiveData()
    }

    fun setUserNotifications(){
        userNotifications = userDao.getUserWithNotifications(user.value!![0].userId).asLiveData()
    }

    fun setAlbumPictures(){
        albumPictures = albumDao.getAlbumWithPictures(albumId).asLiveData()
    }

    fun setUserAlbums(){
        userAlbums = userDao.getUserWithAlbums(user.value!![0].userId).asLiveData()
    }

    fun findAuthor(authorId: Int): String{
        for(user in users.value!!){
            if(user.userId == authorId) return user.login
        }
        return "Anonymous"
    }

    fun addAlbum(album: Album){
        viewModelScope.launch {
            albumDao.insert(album)
        }
    }

    fun getPicture(): Picture?{
        for(picture in pictures.value!!){
            if(picture.pictureId == pictureId) return picture
        }

        return null
    }

    fun addPicture(picture: Picture){
        viewModelScope.launch {
            pictureDao.insert(picture)
        }
    }

    fun updateUser(user: User){
        viewModelScope.launch {
            userDao.update(user)
        }
    }

    fun addGenre(genre: Genre){
        viewModelScope.launch {
            genreDao.insert(genre)
        }
    }

    fun registerUser(user: User){
        viewModelScope.launch {
            userDao.insert(user)
        }
    }

    fun deletePictures(vararg picture: Picture){
        viewModelScope.launch {
            pictureDao.delete(*picture)
        }
    }

    fun deleteUser(user: User){
        viewModelScope.launch {
            userDao.delete(user)
        }
    }

    fun deleteAlbum(album: Album){
        viewModelScope.launch {
            albumDao.delete(album)
        }
    }

    fun deleteAlbumWithId(albumId: Int){
        viewModelScope.launch {
            albumDao.deleteAlbumWithId(albumId)
        }
    }

    fun deleteAlbumPicture(albumPictureCrossRef: AlbumPictureCrossRef){
        viewModelScope.launch {
            albumPictureCrossRefDao.delete(albumPictureCrossRef)
        }
    }

    fun clearUsers(){
        viewModelScope.launch {
            userDao.clear()
        }
    }

    fun exitUser(user: User){
        updateUser(
            User(
                user.userId,
                user.login,
                user.password,
                user.name,
                user.surname,
                false
            )
        )
    }

    fun checkLogins(login: String): Boolean{
        if(users.value.isNullOrEmpty()){
            return true
        }
        else {
            for (x in users.value!!) {
                if (x.login == login) return false
            }
            return true
        }
    }

    fun checkUser(login: String, password: String): Boolean{
        if(users.value.isNullOrEmpty()){
            return false
        }
        else {
            for (user in users.value!!) {
                if (user.login == login && user.password == password) {
                    updateUser(
                        User(
                            user.userId,
                            user.login,
                            user.password,
                            user.name,
                            user.surname,
                            true
                        )
                    )
                    return true
                }
            }
            return false
        }
    }
}

class SharedViewModelFactory(private val albumDao: AlbumDao,
                             private val albumPictureCrossRefDao: AlbumPictureCrossRefDao,
                             private val notificationDao: NotificationDao,
                             private val genreDao: GenreDao,
                             private val pictureDao: PictureDao,
                             private val userDao: UserDao
                             ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SharedViewModel(albumDao, albumPictureCrossRefDao, notificationDao, genreDao, pictureDao, userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}