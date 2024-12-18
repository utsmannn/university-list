package com.utsman.universitylist.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.utsman.universitylist.data.UniversityEntity
import com.utsman.universitylist.data.local.UniversityDao

/**
 * Fake implementation of UniversityDao for testing and preview purposes.
 */
class FakeUniversityDao(private val universities: List<UniversityEntity> = emptyList()) : UniversityDao {

    private val universityList = universities.toMutableList()

    override suspend fun insertUniversities(universities: List<UniversityEntity>) {
        this.universityList.addAll(universities)
    }

    override fun getAllUniversities(): PagingSource<Int, UniversityEntity> {
        return object : PagingSource<Int, UniversityEntity>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UniversityEntity> {
                val page = params.key ?: 0
                val pageSize = params.loadSize
                val start = page * pageSize
                val end = minOf(start + pageSize, universityList.size)
                return if (start < end) {
                    LoadResult.Page(
                        data = universityList.subList(start, end),
                        prevKey = if (page == 0) null else page - 1,
                        nextKey = if (end < universityList.size) page + 1 else null
                    )
                } else {
                    LoadResult.Page(
                        data = emptyList(),
                        prevKey = if (page == 0) null else page - 1,
                        nextKey = null
                    )
                }
            }

            override fun getRefreshKey(state: PagingState<Int, UniversityEntity>): Int? {
                return state.anchorPosition?.let { anchor ->
                    state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                        ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
                }
            }
        }
    }

    override fun searchUniversitiesByName(query: String): PagingSource<Int, UniversityEntity> {
        val filtered = universityList.filter { it.name.contains(query, ignoreCase = true) }
        return object : PagingSource<Int, UniversityEntity>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UniversityEntity> {
                val page = params.key ?: 0
                val pageSize = params.loadSize
                val start = page * pageSize
                val end = minOf(start + pageSize, filtered.size)
                return if (start < end) {
                    LoadResult.Page(
                        data = filtered.subList(start, end),
                        prevKey = if (page == 0) null else page - 1,
                        nextKey = if (end < filtered.size) page + 1 else null
                    )
                } else {
                    LoadResult.Page(
                        data = emptyList(),
                        prevKey = if (page == 0) null else page - 1,
                        nextKey = null
                    )
                }
            }

            override fun getRefreshKey(state: PagingState<Int, UniversityEntity>): Int? {
                return state.anchorPosition?.let { anchor ->
                    state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                        ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
                }
            }
        }
    }

    override suspend fun getCount(): Int {
        return universityList.size
    }
}