package com.utsman.universitylist.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.utsman.universitylist.data.UniversityEntity


@Database(entities = [UniversityEntity::class], version = 1, exportSchema = false)
abstract class UniversityDatabase : RoomDatabase() {
    abstract fun universityDao(): UniversityDao

    companion object {
        @Volatile
        private var _instance: UniversityDatabase? = null

        fun getDatabase(context: Context): UniversityDatabase {
            return _instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UniversityDatabase::class.java,
                    "university_db"
                ).build()

                _instance = instance
                instance
            }
        }
    }
}