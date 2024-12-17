package com.utsman.universitylist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.utsman.universitylist.data.UniversityEntity


@Database(entities = [UniversityEntity::class], version = 1, exportSchema = false)
abstract class UniversityDatabase : RoomDatabase() {
    abstract fun universityDao(): UniversityDao
}