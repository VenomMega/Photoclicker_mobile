package com.example.photoclicker

import android.app.Application

class Application: Application() {
    val database: AppDatabase by lazy { AppDatabase.getInstance(this)}
}