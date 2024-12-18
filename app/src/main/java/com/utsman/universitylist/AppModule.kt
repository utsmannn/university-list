package com.utsman.universitylist

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.utsman.universitylist.data.local.UniversityDao
import com.utsman.universitylist.data.local.UniversityDatabase
import com.utsman.universitylist.data.remote.UniversityApiService
import com.utsman.universitylist.data.repository.UniversityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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

    @Provides
    @Singleton
    fun provideApiService(): UniversityApiService {
        val loggingInterceptor = HttpLoggingInterceptor()
            .apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://universities.hipolabs.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(UniversityApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(
        universityDao: UniversityDao,
        universityApiService: UniversityApiService
    ): UniversityRepository {
        return UniversityRepository(universityDao, universityApiService)
    }
}