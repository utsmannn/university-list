package com.utsman.universitylist

import android.content.Context
import com.utsman.universitylist.data.local.UniversityDao
import com.utsman.universitylist.data.local.UniversityDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): UniversityDatabase {
        return UniversityDatabase.getDatabase(context)
    }

    @Provides
    fun provideUniversityDao(database: UniversityDatabase): UniversityDao {
        return database.universityDao()
    }
}