package com.nutriweek.inclusiverecip.data

import android.content.Context
import androidx.room.Room

object DbProvider {
    lateinit var db: AppDatabase
        private set

    fun init(context: Context) {
        if (!this::db.isInitialized) {
            db = Room.databaseBuilder(context, AppDatabase::class.java, "app.db")
                .allowMainThreadQueries()
                .build()
        }
    }
}
