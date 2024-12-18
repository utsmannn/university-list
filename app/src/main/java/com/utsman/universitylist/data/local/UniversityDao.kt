package com.utsman.universitylist.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.utsman.universitylist.data.UniversityEntity

@Dao
interface UniversityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUniversities(universities: List<UniversityEntity>)

    @Query("SELECT * FROM university ORDER BY name ASC")
    fun getAllUniversities(): PagingSource<Int, UniversityEntity>

    @Query("SELECT * FROM university WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchUniversitiesByName(query: String): PagingSource<Int, UniversityEntity>
}