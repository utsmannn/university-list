package com.utsman.universitylist.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.utsman.universitylist.data.UniversityEntity


/**
 * Room database for the University app.
 *
 * @property universityDao Provides access to the [UniversityDao].
 */
@Database(entities = [UniversityEntity::class], version = 1, exportSchema = false)
abstract class UniversityDatabase : RoomDatabase() {
    abstract fun universityDao(): UniversityDao

    companion object {
        @Volatile
        private var _instance: UniversityDatabase? = null

        /**
         * Retrieves the singleton instance of [UniversityDatabase].
         *
         * @param context The application context.
         * @return The [UniversityDatabase] instance.
         */
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