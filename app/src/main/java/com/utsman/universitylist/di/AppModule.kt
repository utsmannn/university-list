package com.utsman.universitylist.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.utsman.universitylist.data.local.UniversityDao
import com.utsman.universitylist.data.local.UniversityDatabase
import com.utsman.universitylist.data.local.dataStore
import com.utsman.universitylist.data.remote.UniversityApiService
import com.utsman.universitylist.data.repository.UniversityRepository
import com.utsman.universitylist.domain.api.GetRecentSearchUseCase
import com.utsman.universitylist.domain.impl.GetRecentSearchUseCaseImpl
import dagger.Binds
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

/**
 * Dagger Hilt module for providing application-wide dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    /**
     * Provides the [UniversityDatabase] instance.
     *
     * @param context The application context.
     * @return The [UniversityDatabase] instance.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): UniversityDatabase {
        return UniversityDatabase.getDatabase(context)
    }

    /**
     * Provides the [UniversityDao] instance from the [UniversityDatabase].
     *
     * @param database The [UniversityDatabase] instance.
     * @return The [UniversityDao] instance.
     */
    @Provides
    fun provideUniversityDao(database: UniversityDatabase): UniversityDao {
        return database.universityDao()
    }

    /**
     * Provides the [UniversityApiService] instance using Retrofit.
     *
     * @return The [UniversityApiService] instance.
     */
    @Provides
    @Singleton
    fun provideApiService(): UniversityApiService {
        // Configure logging interceptor for debugging network requests
        val loggingInterceptor = HttpLoggingInterceptor()
            .apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

        // Build OkHttpClient with the logging interceptor
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        // Configure Moshi for JSON serialization/deserialization
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        // Build Retrofit instance with Moshi converter and OkHttpClient
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://universities.hipolabs.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(UniversityApiService::class.java)
    }

    /**
     * Provides the [DataStore] instance for managing preferences.
     *
     * @param context The application context.
     * @return The DataStore<Preferences> instance.
     */
    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.dataStore
    }

    /**
     * Provides the [UniversityRepository] instance.
     *
     * @param universityDao The [UniversityDao] instance.
     * @param universityApiService The [UniversityApiService] instance.
     * @param dataStore The DataStore<Preferences> instance.
     * @return The [UniversityRepository] instance.
     */
    @Provides
    @Singleton
    fun provideRepository(
        universityDao: UniversityDao,
        universityApiService: UniversityApiService,
        dataStore: DataStore<Preferences>
    ): UniversityRepository {
        return UniversityRepository(universityDao, universityApiService, dataStore)
    }
}