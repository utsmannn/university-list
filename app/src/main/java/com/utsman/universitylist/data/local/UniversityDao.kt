package com.utsman.universitylist.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.utsman.universitylist.data.UniversityEntity

/**
 * Data Access Object for the [UniversityDatabase].
 * Provides methods to interact with the university table.
 */
@Dao
interface UniversityDao {

    /**
     * Inserts a list of universities into the database.
     * Replaces existing entries on conflict.
     *
     * @param universities The list of [UniversityEntity] to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUniversities(universities: List<UniversityEntity>)

    /**
     * Retrieves all universities ordered by name in ascending order.
     *
     * @return A [PagingSource] for loading universities.
     */
    @Query("SELECT * FROM university ORDER BY name ASC")
    fun getAllUniversities(): PagingSource<Int, UniversityEntity>

    /**
     * Searches for universities by name containing the query string.
     *
     * @param query The search query.
     * @return A [PagingSource] for loading matching universities.
     */
    @Query("SELECT * FROM university WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchUniversitiesByName(query: String): PagingSource<Int, UniversityEntity>

    /**
     * Retrieves the count of universities in the database.
     *
     * @return The total number of universities.
     */
    @Query("SELECT COUNT(*) FROM university")
    suspend fun getCount(): Int
}